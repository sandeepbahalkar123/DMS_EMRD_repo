package com.scorg.dms.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.scorg.dms.model.chat.MQTTMessage;
import com.scorg.dms.model.chat.StatusInfo;
import com.scorg.dms.model.patient.add_new_patient.PatientDetail;
import com.scorg.dms.model.patient.doctor_patients.PatientList;
import com.scorg.dms.model.patient.doctor_patients.sync_resp.PatientUpdateDetail;
import com.scorg.dms.model.request_patients.FilterParams;
import com.scorg.dms.model.request_patients.RequestSearchPatients;
import com.scorg.dms.util.CommonMethods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.scorg.dms.services.MQTTService.DOCTOR;
import static com.scorg.dms.services.MQTTService.PATIENT;
import static com.scorg.dms.util.DMSConstants.FILE_STATUS.FAILED;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.REACHED;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.READ;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.SEEN;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.SENT;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.UNREAD;

public class AppDBHelper extends SQLiteOpenHelper {

    private final String TAG = "DrRescribe/AppDBHelper";

    private static final String DATABASE_NAME = "MyRescribe.sqlite";
    private static final String DB_PATH_SUFFIX = "/data/data/com.scorg.dms/databases/"; // Change
    private static final int DB_VERSION = 2;
    public static final String APP_DATA_TABLE = "PrescriptionData";
    public static final String COLUMN_ID = "dataId";
    public static final String COLUMN_DATA = "data";

    private static AppDBHelper instance = null;
    private Context mContext;

    public AppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.mContext = context;
        checkDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // db.execSQL("CREATE TABLE IF NOT EXISTS " + APP_DATA_TABLE + "(dataId integer, data text)");
        // db.execSQL("CREATE TABLE IF NOT EXISTS " + PREFERENCES_TABLE + "(userId integer, breakfastTime text, lunchTime text, dinnerTime text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.e(TAG, "Updating table from " + oldVersion + " to " + newVersion);
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.
        for (int i = oldVersion; i < newVersion; ++i) {
            String migrationName = String.format("from_%d_to_%d.sql", i, (i + 1));
            Log.e(TAG, "Looking for migration file: " + migrationName);
            readAndExecuteSQLScript(db, mContext, migrationName);
        }
    }

    //-----------
    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            Log.e(TAG, "SQL script file name is empty");
            return;
        }

        Log.e(TAG, "Script found. Executing...");
        AssetManager assetManager = ctx.getAssets();
        BufferedReader reader = null;

        try {
            InputStream is = assetManager.open(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            executeSQLScript(db, reader);
        } catch (IOException e) {
            Log.e(TAG, "IOException:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException:", e);
                }
            }
        }

    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                String s = statement.toString();
                CommonMethods.Log(TAG, s);
                db.execSQL(s);
                statement = new StringBuilder();
            }
        }
    }
    //-----------

    public static synchronized AppDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AppDBHelper(context);
        }
        return instance;
    }

    public boolean insertData(String dataId, String data) {
        if (dataTableNumberOfRows(dataId) == 0) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("dataId", dataId);
            contentValues.put("data", data);

            db.insert(APP_DATA_TABLE, null, contentValues);
        } else {
            updateData(dataId, data);
        }
        return true;
    }

    public Cursor getData(String dataId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + APP_DATA_TABLE + " where dataId=" + dataId + "", null);
    }

    public int dataTableNumberOfRows(String dataId) {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, APP_DATA_TABLE, "dataId = ? ", new String[]{dataId});
    }

    private boolean updateData(String dataId, String data) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", data);

        db.update(APP_DATA_TABLE, contentValues, "dataId = ? ", new String[]{dataId});
        return true;
    }

    public Integer deleteData(Integer dataId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(APP_DATA_TABLE,
                "dataId = ? ",
                new String[]{Integer.toString(dataId)});
    }

    private void copyDataBase() {

        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try {
            myInput = mContext.getAssets().open(DATABASE_NAME);
            // transfer bytes from the inputfile to the
            // outputfile

            // check if databases folder exists, if not create one and its subfolders
            File databaseFile = new File(DB_PATH_SUFFIX);
            if (!databaseFile.exists()) {
                databaseFile.mkdir();
            }

            String path = databaseFile.getAbsolutePath() + "/" + DATABASE_NAME;

            myOutput = new FileOutputStream(path);

            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            CommonMethods.Log(TAG,
                    "New database has been copied to device!");
        } catch (IOException e) {
            CommonMethods.Log(TAG,
                    "Failed to copy database");
            e.printStackTrace();
        }
    }

    private void checkDatabase() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        CommonMethods.Log(TAG, "FILENAME " + dbFile.getAbsolutePath() + "|" + dbFile.getName());
        if (!dbFile.exists()) {
            copyDataBase();
        }
    }

    public void deleteDatabase() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        dbFile.delete();

        CommonMethods.Log("DeletedOfflineDatabase", "APP_DATA , PREFERENCES TABLE, INVESTIGATION");
    }

    public int updateMessageUploadStatus(String messageId, int msgUploadStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHAT_MESSAGES.UPLOAD_STATUS, msgUploadStatus);
        return db.update(CHAT_MESSAGES.CHAT_MESSAGES_TABLE, contentValues, CHAT_MESSAGES.MSG_ID + " = ? AND " + CHAT_MESSAGES.SENDER + " = ?", new String[]{messageId, DOCTOR});
    }

    public MQTTMessage getChatMessageByMessageId(String messageId) {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + CHAT_MESSAGES.CHAT_MESSAGES_TABLE + " where " + CHAT_MESSAGES.MSG_ID + " = '" + messageId + "'";
        Cursor cursor = db.rawQuery(countQuery, null);

        MQTTMessage mqttMessage = null;

        if (cursor.moveToFirst()) {
            mqttMessage = new MQTTMessage();

            mqttMessage.setMsgId(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_ID)));
            mqttMessage.setMsg(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG)));
            mqttMessage.setMsgTime(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_TIME)));
            mqttMessage.setSender(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER)));
            mqttMessage.setPatId(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.USER2ID)));
            mqttMessage.setDocId(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.USER1ID)));
            mqttMessage.setSenderName(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER_NAME)));

            mqttMessage.setSpecialization(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SPECIALITY)));
            mqttMessage.setMsgStatus(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_STATUS)));
            mqttMessage.setSenderImgUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER_IMG_URL)));
            mqttMessage.setFileUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.FILE_URL)));
            mqttMessage.setFileType(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.FILE_TYPE)));

            mqttMessage.setSalutation(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.SALUTATION)));
            mqttMessage.setReceiverName(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.RECEIVER_NAME)));
            mqttMessage.setReceiverImgUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.RECEIVER_IMG_URL)));

            mqttMessage.setUploadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.UPLOAD_STATUS)));
            mqttMessage.setDownloadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.DOWNLOAD_STATUS)));
            mqttMessage.setReadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.READ_STATUS)));
        }
        cursor.close();
        db.close();

        return mqttMessage;
    }

    public ArrayList<MQTTMessage> getChatMessageByMessageStatus(String messageStatus) {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + CHAT_MESSAGES.CHAT_MESSAGES_TABLE + " where " + CHAT_MESSAGES.MSG_STATUS + " = '" + messageStatus + "' AND " + CHAT_MESSAGES.SENDER + " = '" + DOCTOR + "'";
        Cursor cursor = db.rawQuery(countQuery, null);
        ArrayList<MQTTMessage> chatDoctors = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MQTTMessage mqttMessage = new MQTTMessage();

                mqttMessage.setMsgId(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_ID)));
                mqttMessage.setMsg(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG)));
                mqttMessage.setMsgTime(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_TIME)));
                mqttMessage.setSender(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER)));
                mqttMessage.setPatId(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.USER2ID)));
                mqttMessage.setDocId(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.USER1ID)));
                mqttMessage.setSenderName(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER_NAME)));

                mqttMessage.setSpecialization(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SPECIALITY)));
                mqttMessage.setMsgStatus(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_STATUS)));
                mqttMessage.setSenderImgUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER_IMG_URL)));
                mqttMessage.setFileUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.FILE_URL)));
                mqttMessage.setFileType(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.FILE_TYPE)));

                mqttMessage.setSalutation(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.SALUTATION)));
                mqttMessage.setReceiverName(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.RECEIVER_NAME)));
                mqttMessage.setReceiverImgUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.RECEIVER_IMG_URL)));

                mqttMessage.setUploadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.UPLOAD_STATUS)));
                mqttMessage.setDownloadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.DOWNLOAD_STATUS)));
                mqttMessage.setReadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.READ_STATUS)));

                chatDoctors.add(mqttMessage);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return chatDoctors;
    }

    public void updateChatMessageStatus(StatusInfo statusInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHAT_MESSAGES.MSG_STATUS, statusInfo.getMessageStatus());

        if (statusInfo.getMessageStatus().equals(SEEN))
            db.update(CHAT_MESSAGES.CHAT_MESSAGES_TABLE, contentValues, CHAT_MESSAGES.USER2ID + " = ? AND " + CHAT_MESSAGES.MSG_STATUS + " = ? OR " + CHAT_MESSAGES.MSG_STATUS + " = ?", new String[]{String.valueOf(statusInfo.getPatId()), SENT, REACHED});

        if (statusInfo.getMessageStatus().equals(REACHED))
            db.update(CHAT_MESSAGES.CHAT_MESSAGES_TABLE, contentValues, CHAT_MESSAGES.USER2ID + " = ? AND " + CHAT_MESSAGES.MSG_STATUS + " = ?", new String[]{String.valueOf(statusInfo.getPatId()), SENT});
    }

    public Cursor getChatUsers() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + CHAT_MESSAGES.CHAT_MESSAGES_TABLE + " GROUP BY " + CHAT_MESSAGES.USER2ID + " ORDER BY " + CHAT_MESSAGES.MSG_TIME + " DESC";
        return db.rawQuery(sql, null);
    }

    public Cursor getRecordUploads() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + MY_RECORDS.MY_RECORDS_TABLE + " WHERE " + MY_RECORDS.UPLOAD_STATUS + " = " + FAILED;
        return  db.rawQuery(sql, null);
    }

    public void insertRecordUploads(String uploadId, String patientId, int docId, String visitDate, String mOpdtime, String opdId, String mHospitalId, String mHospitalPatId, String mLocationId, String parentCaption, String imagePath, int mAptId) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MY_RECORDS.UPLOAD_ID, uploadId);
        contentValues.put(MY_RECORDS.PATIENT_ID, patientId);
        contentValues.put(MY_RECORDS.DOC_ID, docId);
        contentValues.put(MY_RECORDS.VISIT_DATE, visitDate);
        contentValues.put(MY_RECORDS.OPD_TIME, mOpdtime);
        contentValues.put(MY_RECORDS.OPD_ID, opdId);
        contentValues.put(MY_RECORDS.HOSPITAL_ID, mHospitalId);
        contentValues.put(MY_RECORDS.HOSPITAL_PAT_ID, mHospitalPatId);
        contentValues.put(MY_RECORDS.LOCATION_ID, mLocationId);
        contentValues.put(MY_RECORDS.PARENT_CAPTION, parentCaption);
        contentValues.put(MY_RECORDS.IMAGE_PATH, imagePath);
        contentValues.put(MY_RECORDS.UPLOAD_STATUS, FAILED);
        contentValues.put(MY_RECORDS.APT_ID,mAptId);

        db.insert(MY_RECORDS.MY_RECORDS_TABLE, null, contentValues);

        db.close();
    }

    public void updateRecordUploads(String uploadId, int uploadStatus) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MY_RECORDS.UPLOAD_STATUS, uploadStatus);

        db.update(MY_RECORDS.MY_RECORDS_TABLE, contentValues, MY_RECORDS.UPLOAD_ID + " = ?", new String[]{uploadId});
        db.close();
    }

    public interface MY_RECORDS {
        String MY_RECORDS_TABLE = "my_records";
        String ID = "id";
        String UPLOAD_ID = "uploadId";
        String PATIENT_ID = "patientId";
        String DOC_ID = "docId";
        String VISIT_DATE = "visitDate";
        String OPD_TIME = "opdTime";
        String OPD_ID = "opdId";
        String HOSPITAL_PAT_ID = "hospitalPatId";
        String HOSPITAL_ID = "hospitalId";
        String LOCATION_ID = "locationId";
        String PARENT_CAPTION = "parentCaption";
        String IMAGE_PATH = "imagePath";
        String UPLOAD_STATUS = "uploadStatus";
        String APT_ID = "aptId";
    }

    // All About Chat

    public interface CHAT_MESSAGES {
        String CHAT_MESSAGES_TABLE = "chat_messages";

        String ID = "id";
        String MSG_ID = "msgId";
        String MSG = "msg";
        String MSG_TIME = "msgTime";
        String SENDER = "sender";
        String USER2ID = "user2id";
        String USER1ID = "user1id";
        String SENDER_NAME = "senderName";
        String SPECIALITY = "speciality";
        String MSG_STATUS = "msgStatus";
        String SENDER_IMG_URL = "senderImgUrl";
        String FILE_URL = "fileUrl";
        String FILE_TYPE = "fileType";
        String UPLOAD_STATUS = "uploadStatus";
        String DOWNLOAD_STATUS = "downloadStatus";
        String READ_STATUS = "readStatus";

        String SALUTATION = "salutation";
        String RECEIVER_NAME = "receiverName";
        String RECEIVER_IMG_URL = "receiverImgUrl";
    }

    public interface ADD_NEW_PATIENT {
        String _ID = "dataId";
        String PATIENT_ID = "patientId";
        String TABLE_NAME = "addNewPatient";
        String SALUTATION = "salutation";
        String FIRST_NAME = "firstName";
        String MIDDLE_NAME = "middleName";
        String LAST_NAME = "lastName";
        String IMAGE_URL = "imageURL";
        String DOB = "dob";
        String OUTSTANDING_AMT = "outstandingAmount";
        String EMAIL = "email";
        String MOBILE_NO = "mobileNo";
        String AGE = "age";
        String GENDER = "gender";
        String REFERENCE_ID = "referenceID";
        String IS_SYNC = "isSync";
        String CREATED_TIME_STAMP = "createdTimeStamp";
        String HOSPITALPATID = "hospitalPatId";
        String CLINIC_ID = "clinicID";
        String CITY_ID = "cityId";
        String CITY_NAME = "city";
        String LOCATION_ID = "locationID";
        String DOC_ID = "docID";
        Integer IS_SYNC_WITH_SERVER = 1;
        Integer IS_NOT_SYNC_WITH_SERVER = 0;
    }

    public ArrayList<MQTTMessage> insertChatMessage(MQTTMessage mqttMessage) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CHAT_MESSAGES.MSG_ID, mqttMessage.getMsgId());
        contentValues.put(CHAT_MESSAGES.MSG, mqttMessage.getMsg());
        contentValues.put(CHAT_MESSAGES.MSG_TIME, mqttMessage.getMsgTime());
        contentValues.put(CHAT_MESSAGES.SENDER, mqttMessage.getSender());
        contentValues.put(CHAT_MESSAGES.USER2ID, mqttMessage.getPatId());
        contentValues.put(CHAT_MESSAGES.USER1ID, mqttMessage.getDocId());
        contentValues.put(CHAT_MESSAGES.SENDER_NAME, mqttMessage.getSenderName());
        contentValues.put(CHAT_MESSAGES.SPECIALITY, mqttMessage.getSpecialization());
        contentValues.put(CHAT_MESSAGES.MSG_STATUS, mqttMessage.getMsgStatus());
        contentValues.put(CHAT_MESSAGES.SENDER_IMG_URL, mqttMessage.getSenderImgUrl());
        contentValues.put(CHAT_MESSAGES.FILE_URL, mqttMessage.getFileUrl());
        contentValues.put(CHAT_MESSAGES.FILE_TYPE, mqttMessage.getFileType());
        contentValues.put(CHAT_MESSAGES.UPLOAD_STATUS, mqttMessage.getUploadStatus());
        contentValues.put(CHAT_MESSAGES.DOWNLOAD_STATUS, mqttMessage.getDownloadStatus());
        contentValues.put(CHAT_MESSAGES.READ_STATUS, mqttMessage.getReadStatus());

        contentValues.put(CHAT_MESSAGES.SALUTATION, mqttMessage.getSalutation());
        contentValues.put(CHAT_MESSAGES.RECEIVER_NAME, mqttMessage.getReceiverName());
        contentValues.put(CHAT_MESSAGES.RECEIVER_IMG_URL, mqttMessage.getReceiverImgUrl());

        if (getChatMessageCountByMessageId(mqttMessage.getMsgId()) == 0)
            db.insert(CHAT_MESSAGES.CHAT_MESSAGES_TABLE, null, contentValues);
        else
            db.update(CHAT_MESSAGES.CHAT_MESSAGES_TABLE, contentValues, CHAT_MESSAGES.MSG_ID + " = ?", new String[]{mqttMessage.getMsgId()});

        return getChatUnreadMessagesByPatientId(mqttMessage.getPatId());
    }

    private long getChatMessageCountByMessageId(String msgId) {
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, CHAT_MESSAGES.CHAT_MESSAGES_TABLE, CHAT_MESSAGES.MSG_ID + " = '" + msgId + "'");
    }

    public void markAsAReadChatMessageByPatientId(int patientId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHAT_MESSAGES.READ_STATUS, READ);
        db.update(CHAT_MESSAGES.CHAT_MESSAGES_TABLE, contentValues, CHAT_MESSAGES.USER2ID + " = ? AND " + CHAT_MESSAGES.READ_STATUS + " = ? AND " + CHAT_MESSAGES.SENDER + " = ?", new String[]{String.valueOf(patientId), String.valueOf(UNREAD), PATIENT});
        db.close();
    }

    public long unreadChatMessageCountByPatientId(int patientId) {
        // Return Total Count
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, CHAT_MESSAGES.CHAT_MESSAGES_TABLE, CHAT_MESSAGES.USER2ID + " = " + patientId + " AND " + CHAT_MESSAGES.READ_STATUS + " = " + UNREAD + " AND " + CHAT_MESSAGES.SENDER + " = '" + PATIENT + "'");
    }

    public ArrayList<MQTTMessage> getChatUnreadMessagesByPatientId(int user2Id) {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + CHAT_MESSAGES.CHAT_MESSAGES_TABLE + " where " + CHAT_MESSAGES.USER2ID + " = " + user2Id + " AND " + CHAT_MESSAGES.READ_STATUS + " = " + UNREAD + " AND " + CHAT_MESSAGES.SENDER + " = '" + PATIENT + "'";
        Cursor cursor = db.rawQuery(countQuery, null);
        ArrayList<MQTTMessage> chatDoctors = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MQTTMessage mqttMessage = new MQTTMessage();

                mqttMessage.setMsgId(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_ID)));
                mqttMessage.setMsg(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG)));
                mqttMessage.setMsgTime(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_TIME)));
                mqttMessage.setSender(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER)));
                mqttMessage.setPatId(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.USER2ID)));
                mqttMessage.setDocId(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.USER1ID)));
                mqttMessage.setSenderName(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER_NAME)));

                mqttMessage.setSpecialization(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SPECIALITY)));
                mqttMessage.setMsgStatus(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_STATUS)));
                mqttMessage.setSenderImgUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER_IMG_URL)));
                mqttMessage.setFileUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.FILE_URL)));
                mqttMessage.setFileType(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.FILE_TYPE)));

                mqttMessage.setSalutation(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.SALUTATION)));
                mqttMessage.setReceiverName(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.RECEIVER_NAME)));
                mqttMessage.setReceiverImgUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.RECEIVER_IMG_URL)));

                mqttMessage.setUploadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.UPLOAD_STATUS)));
                mqttMessage.setDownloadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.DOWNLOAD_STATUS)));
                mqttMessage.setReadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.READ_STATUS)));

                chatDoctors.add(mqttMessage);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return chatDoctors;
    }

    public Cursor searchChatMessagesByChars(String chars) {
        SQLiteDatabase db = getReadableDatabase();
        if (chars != null && !chars.isEmpty()) {
            String sql = "SELECT * FROM " + CHAT_MESSAGES.CHAT_MESSAGES_TABLE + " WHERE " + CHAT_MESSAGES.MSG + " LIKE '%" + chars + "%' ORDER BY " + CHAT_MESSAGES.MSG_TIME + " DESC LIMIT 50";
            return db.rawQuery(sql, null);
        } else return null;
    }

    public MQTTMessage getLastChatMessagesByPatientId(int patientId) {
        SQLiteDatabase db = getReadableDatabase();

        String countQuery = "SELECT * FROM " + CHAT_MESSAGES.CHAT_MESSAGES_TABLE + " WHERE " + CHAT_MESSAGES.USER2ID + " = " + patientId + " ORDER BY " + CHAT_MESSAGES.MSG_TIME + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(countQuery, null);

        MQTTMessage mqttMessage = null;

        if (cursor.moveToFirst()) {
            mqttMessage = new MQTTMessage();

            mqttMessage.setMsgId(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_ID)));
            mqttMessage.setMsg(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG)));
            mqttMessage.setMsgTime(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_TIME)));
            mqttMessage.setSender(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER)));
            mqttMessage.setPatId(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.USER2ID)));
            mqttMessage.setDocId(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.USER1ID)));
            mqttMessage.setSenderName(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER_NAME)));

            mqttMessage.setSpecialization(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SPECIALITY)));
            mqttMessage.setMsgStatus(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.MSG_STATUS)));
            mqttMessage.setSenderImgUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.SENDER_IMG_URL)));
            mqttMessage.setFileUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.FILE_URL)));
            mqttMessage.setFileType(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.FILE_TYPE)));

            mqttMessage.setSalutation(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.SALUTATION)));
            mqttMessage.setReceiverName(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.RECEIVER_NAME)));
            mqttMessage.setReceiverImgUrl(cursor.getString(cursor.getColumnIndex(CHAT_MESSAGES.RECEIVER_IMG_URL)));

            mqttMessage.setUploadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.UPLOAD_STATUS)));
            mqttMessage.setDownloadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.DOWNLOAD_STATUS)));
            mqttMessage.setReadStatus(cursor.getInt(cursor.getColumnIndex(CHAT_MESSAGES.READ_STATUS)));
        }
        cursor.close();
        db.close();

        return mqttMessage;
    }

    public Cursor getChatMessagesByPatientId(int user2Id) {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + CHAT_MESSAGES.CHAT_MESSAGES_TABLE + " where " + CHAT_MESSAGES.USER2ID + " = " + user2Id;
        return db.rawQuery(countQuery, null);
    }

    //-----store patient in db : start----
    public long addNewPatient(PatientList newPatient) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ADD_NEW_PATIENT.PATIENT_ID, newPatient.getPatientId());
        contentValues.put(ADD_NEW_PATIENT.SALUTATION, newPatient.getSalutation());
        String patientName = newPatient.getPatientName().trim();

        if (!patientName.isEmpty()) {
            String[] split = patientName.split(" ");
            contentValues.put(ADD_NEW_PATIENT.FIRST_NAME, split[0]);
            if (split.length > 1) {
                if (split[1].trim().equalsIgnoreCase("|"))
                    contentValues.put(ADD_NEW_PATIENT.MIDDLE_NAME, "");
                else
                    contentValues.put(ADD_NEW_PATIENT.MIDDLE_NAME, split[1].trim());
            }
            if (split.length > 2)
                contentValues.put(ADD_NEW_PATIENT.LAST_NAME, split[2]);
        }

        contentValues.put(ADD_NEW_PATIENT.MOBILE_NO, newPatient.getPatientPhone());
        contentValues.put(ADD_NEW_PATIENT.AGE, newPatient.getAge());
        contentValues.put(ADD_NEW_PATIENT.GENDER, newPatient.getGender());
        contentValues.put(ADD_NEW_PATIENT.REFERENCE_ID, newPatient.getReferenceID());
        contentValues.put(ADD_NEW_PATIENT.CLINIC_ID, newPatient.getClinicId());
        contentValues.put(ADD_NEW_PATIENT.CITY_ID, newPatient.getPatientCityId());
        contentValues.put(ADD_NEW_PATIENT.CITY_NAME, newPatient.getPatientCity());
        contentValues.put(ADD_NEW_PATIENT.DOB, newPatient.getDateOfBirth());
        contentValues.put(ADD_NEW_PATIENT.OUTSTANDING_AMT, newPatient.getOutStandingAmount());
        contentValues.put(ADD_NEW_PATIENT.IMAGE_URL, newPatient.getPatientImageUrl());
        contentValues.put(ADD_NEW_PATIENT.EMAIL, newPatient.getPatientEmail());
        contentValues.put(ADD_NEW_PATIENT.IS_SYNC, newPatient.isOfflinePatientSynced() ? ADD_NEW_PATIENT.IS_SYNC_WITH_SERVER : ADD_NEW_PATIENT.IS_NOT_SYNC_WITH_SERVER);
        contentValues.put(ADD_NEW_PATIENT.CREATED_TIME_STAMP, newPatient.getCreationDate());
        contentValues.put(ADD_NEW_PATIENT.HOSPITALPATID, newPatient.getHospitalPatId());

        if (checkIsPatientExist(newPatient.getPatientId()) > 0)
            return db.update(ADD_NEW_PATIENT.TABLE_NAME, contentValues, ADD_NEW_PATIENT.PATIENT_ID + " = ?", new String[]{String.valueOf(newPatient.getPatientId())});
        else return db.insert(ADD_NEW_PATIENT.TABLE_NAME, null, contentValues);
    }

    private long checkIsPatientExist(int patientId) {
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, ADD_NEW_PATIENT.TABLE_NAME, ADD_NEW_PATIENT.PATIENT_ID + " = " + patientId);
    }

    public void updateOfflinePatientANDRecords(ArrayList<PatientUpdateDetail> patientUpdateDetail) {
        SQLiteDatabase db = getWritableDatabase();

        for (PatientUpdateDetail patientUpdate : patientUpdateDetail) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(ADD_NEW_PATIENT.IS_SYNC, ADD_NEW_PATIENT.IS_SYNC_WITH_SERVER);
            contentValues.put(ADD_NEW_PATIENT.PATIENT_ID, patientUpdate.getPatientId());
            contentValues.put(ADD_NEW_PATIENT.HOSPITALPATID, patientUpdate.getHospitalPatId());

            db.update(ADD_NEW_PATIENT.TABLE_NAME, contentValues, ADD_NEW_PATIENT.PATIENT_ID + " = ?  AND " + ADD_NEW_PATIENT.IS_SYNC + " = " + ADD_NEW_PATIENT.IS_NOT_SYNC_WITH_SERVER, new String[]{patientUpdate.getMobilePatientId()});

            ContentValues contentValuesMyRecords = new ContentValues();
            contentValuesMyRecords.put(MY_RECORDS.PATIENT_ID, patientUpdate.getPatientId());
            contentValuesMyRecords.put(MY_RECORDS.HOSPITAL_PAT_ID, patientUpdate.getHospitalPatId());
            db.update(MY_RECORDS.MY_RECORDS_TABLE, contentValuesMyRecords, MY_RECORDS.PATIENT_ID + " = ?", new String[]{patientUpdate.getMobilePatientId()});
        }

        db.close();
    }

    public ArrayList<PatientList> getOfflineAddedPatients(RequestSearchPatients mRequestSearchPatients, int pageNumber, String searchText) {
        SQLiteDatabase db = getReadableDatabase();
        int numberOfRows = 30;

        searchText = searchText.trim();
        FilterParams filterParams = mRequestSearchPatients.getFilterParams();
        String sortOrder = mRequestSearchPatients.getSortOrder();

        String ageLimitQuery = "";
        String cityQuery = "";
        String genderQuery = "";
        String where = "";
        String and = "";

        boolean isFiltered = false;

        if (filterParams != null) {

            if (!filterParams.getGender().isEmpty()) {
                String gender = filterParams.getGender();
                genderQuery = " " + ADD_NEW_PATIENT.GENDER + " like '%" + gender + "' " + ((filterParams.getCityIds().isEmpty() && filterParams.getAge().isEmpty()) ? "" : " AND ");
                isFiltered = true;
            }

            if (!filterParams.getCityIds().isEmpty()) {
                ArrayList<Integer> city = filterParams.getCityIds();
                StringBuilder cities = new StringBuilder();
                for (int c : city) {
                    if (city.indexOf(c) == (city.size() - 1))
                        cities.append(String.valueOf(c));
                    else cities.append(String.valueOf(c)).append(", ");
                }
                cityQuery = " " + ADD_NEW_PATIENT.CITY_ID + " IN (" + cities + ") " + (filterParams.getAge().isEmpty() ? "" : " AND ");
                isFiltered = true;
            }

            if (!filterParams.getAge().isEmpty()) {
                String[] split = filterParams.getAge().split("-");
                String fromAge = split[0];
                String toAge = split[1];
                ageLimitQuery = " " + ADD_NEW_PATIENT.AGE + " >= " + fromAge + " AND " + ADD_NEW_PATIENT.AGE + " <= " + toAge + " ";
                isFiltered = true;
            }
        }

        if (isFiltered) {
            where = " where ";
            and = " AND ";
        }

        String sortOrderQuery;
        if (!sortOrder.isEmpty())
            sortOrderQuery = " ORDER BY " + ADD_NEW_PATIENT.OUTSTANDING_AMT + " " + sortOrder + " ";
        else
            sortOrderQuery = " ORDER BY " + ADD_NEW_PATIENT.CREATED_TIME_STAMP + " DESC ";

        String countQuery;
        if (searchText.isEmpty())
            countQuery = "select * from " + ADD_NEW_PATIENT.TABLE_NAME + where + genderQuery + cityQuery + ageLimitQuery + sortOrderQuery + " LIMIT " + numberOfRows + " OFFSET " + (pageNumber * numberOfRows);
        else
            countQuery = "select * from " + ADD_NEW_PATIENT.TABLE_NAME + " WHERE (" + ADD_NEW_PATIENT.FIRST_NAME + " LIKE '%" + searchText + "%' OR " + ADD_NEW_PATIENT.MIDDLE_NAME + " LIKE '%" + searchText + "%' OR " + ADD_NEW_PATIENT.LAST_NAME + " LIKE '%" + searchText + "%' OR " + ADD_NEW_PATIENT.HOSPITALPATID + " LIKE '%" + searchText + "%' OR " + ADD_NEW_PATIENT.MOBILE_NO + " LIKE '%" + searchText + "%' OR " + ADD_NEW_PATIENT.REFERENCE_ID + " LIKE '%" + searchText + "%'  ) " + and + genderQuery + cityQuery + ageLimitQuery + sortOrderQuery + " LIMIT " + numberOfRows + " OFFSET " + (pageNumber * numberOfRows);


        CommonMethods.Log(TAG + " PATIENT", countQuery);

        Cursor cursor = db.rawQuery(countQuery, null);
        ArrayList<PatientList> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                PatientList patient = new PatientList();

                patient.setPatientId(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.PATIENT_ID)));

                String name = "";
                name += cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.FIRST_NAME));
                if (cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.MIDDLE_NAME)) != null)
                    name += " " + cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.MIDDLE_NAME));

                if (cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.LAST_NAME)) != null)
                    name += " " + cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.LAST_NAME));

                patient.setPatientName(name);

                patient.setSalutation(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.SALUTATION)));
                patient.setPatientPhone(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.MOBILE_NO)));
                patient.setAge(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.AGE)));
                patient.setGender(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.GENDER)));
                patient.setReferenceID(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.REFERENCE_ID)));
                patient.setClinicId(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.CLINIC_ID)));
                patient.setPatientCity(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.CITY_NAME)));
                patient.setPatientCityId(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.CITY_ID)));
                patient.setDateOfBirth(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.DOB)));
                patient.setOutStandingAmount(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.OUTSTANDING_AMT)));
                patient.setPatientImageUrl(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.IMAGE_URL)));
                patient.setPatientEmail(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.EMAIL)));

                //----------
                int anInt = cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.IS_SYNC));
                patient.setOfflinePatientSynced(anInt == ADD_NEW_PATIENT.IS_SYNC_WITH_SERVER);
                //----------
                patient.setCreationDate(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.CREATED_TIME_STAMP)));
                patient.setHospitalPatId(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.HOSPITALPATID)));

                list.add(patient);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return list;
    }

//    public ArrayList<PatientList> getOfflineAddedPatients() {
//        SQLiteDatabase db = getReadableDatabase();
//        String countQuery = "select * from " + ADD_NEW_PATIENT.TABLE_NAME + " where " + ADD_NEW_PATIENT.IS_SYNC + " = " + ADD_NEW_PATIENT.IS_NOT_SYNC_WITH_SERVER;
//
//        Cursor cursor = db.rawQuery(countQuery, null);
//        ArrayList<PatientList> list = new ArrayList<>();
//
//        if (cursor.moveToFirst()) {
//            while (!cursor.isAfterLast()) {
//                PatientList patient = new PatientList();
//
//                patient.setPatientId(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.PATIENT_ID)));
//
//                String name = "";
//                name += cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.FIRST_NAME));
//                if (cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.MIDDLE_NAME)) != null)
//                    name += " " + cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.MIDDLE_NAME));
//
//                if (cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.LAST_NAME)) != null)
//                    name += " " + cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.LAST_NAME));
//
//                patient.setPatientName(name);
//
//                patient.setSalutation(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.SALUTATION)));
//                patient.setPatientPhone(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.MOBILE_NO)));
//                patient.setAge(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.AGE)));
//                patient.setGender(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.GENDER)));
//                patient.setOfflineReferenceID(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.REFERENCE_ID)));
//                patient.setClinicId(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.CLINIC_ID)));
//                patient.setPatientCity(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.CITY_NAME)));
//                patient.setPatientCityId(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.CITY_ID)));
//                patient.setDateOfBirth(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.DOB)));
//                patient.setOutStandingAmount(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.OUTSTANDING_AMT)));
//                patient.setPatientImageUrl(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.IMAGE_URL)));
//                patient.setPatientEmail(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.EMAIL)));
//
//                //----------
//                int anInt = cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.IS_SYNC));
//                patient.setOfflinePatientSynced(anInt == ADD_NEW_PATIENT.IS_SYNC_WITH_SERVER);
//                //----------
//                patient.setCreationDate(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.CREATED_TIME_STAMP)));
//                patient.setHospitalPatId(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.HOSPITALPATID)));
//
//                list.add(patient);
//                cursor.moveToNext();
//            }
//        }
//        cursor.close();
//        db.close();
//
//        return list;
//    }

    public ArrayList<PatientDetail> getOfflinePatientsToUpload() {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + ADD_NEW_PATIENT.TABLE_NAME + " where " + ADD_NEW_PATIENT.IS_SYNC + " like '%" + ADD_NEW_PATIENT.IS_NOT_SYNC_WITH_SERVER + "%'";
        Cursor cursor = db.rawQuery(countQuery, null);
        CommonMethods.Log(TAG, "getOfflinePatientsToUpload" + countQuery);
        ArrayList<PatientDetail> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                PatientDetail patient = new PatientDetail();

                patient.setMobilePatientId(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.PATIENT_ID)));

                patient.setPatientFname(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.FIRST_NAME)));
                patient.setPatientMname(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.MIDDLE_NAME)));
                patient.setPatientLname(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.LAST_NAME)));
                patient.setPatientPhone(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.MOBILE_NO)));
                patient.setPatientAge(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.AGE)));
                patient.setPatientGender(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.GENDER)));
                patient.setOfflineReferenceID(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.REFERENCE_ID)));

//                cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.REFERENCE_ID));

                patient.setClinicId(cursor.getInt(cursor.getColumnIndex(ADD_NEW_PATIENT.CLINIC_ID)));
                patient.setPatientDob(cursor.getString(cursor.getColumnIndex(ADD_NEW_PATIENT.DOB)));

                list.add(patient);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return list;
    }

    public boolean isPatientSynced(String patientId) {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "select * from " + ADD_NEW_PATIENT.TABLE_NAME + " where " + ADD_NEW_PATIENT.PATIENT_ID + " = " + patientId + " AND " + ADD_NEW_PATIENT.IS_SYNC + " = " + ADD_NEW_PATIENT.IS_SYNC_WITH_SERVER;
        Cursor cursor = db.rawQuery(countQuery, null);
        boolean isPatientSynced = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isPatientSynced;
    }

    //-----store patient in db : end----
}