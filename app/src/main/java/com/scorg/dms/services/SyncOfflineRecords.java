package com.scorg.dms.services;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

import com.scorg.dms.R;
import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.Device;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import static com.scorg.dms.services.ChatBackUpService.STATUS;
import static com.scorg.dms.util.DMSConstants.FILE_STATUS.COMPLETED;
import static com.scorg.dms.util.DMSConstants.FILE_STATUS.FAILED;
import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.PROGRESS;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;

/**
 * Created by ganeshs on 21/03/18.
 */

public class SyncOfflineRecords {
    public static final String DOC_UPLOAD = "com.scorg.dms.DOC_UPLOAD";
    public static final String UPLOAD_INFO = "upload_info";
    private Context context;
    private AppDBHelper appDBHelper;

    SyncOfflineRecords() {
    }

    void check() {

        Cursor cursor = appDBHelper.getRecordUploads();

        if (cursor.getCount() > 0) {

            String Url = Config.BASE_URL + Config.MY_RECORDS_UPLOAD;
            String authorizationString = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.AUTHTOKEN, context);
            Device device = Device.getInstance(context);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    int uploadStatus = cursor.getInt(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.UPLOAD_STATUS));
                    String patientId = cursor.getString(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.PATIENT_ID));

                    if (uploadStatus == FAILED && appDBHelper.isPatientSynced(patientId)) {

                        UploadNotificationConfig uploadConfig = getUploadConfig(context);

                        String uploadId = cursor.getString(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.UPLOAD_ID));

                        appDBHelper.updateRecordUploads(uploadId, uploadStatus);

                        String mOpdtime = cursor.getString(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.OPD_TIME));
                        String visitDate = cursor.getString(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.VISIT_DATE));

                        int docId = cursor.getInt(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.DOC_ID));
                        String opdId = cursor.getString(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.OPD_ID));
                        String mHospitalId = cursor.getString(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.HOSPITAL_ID));
                        String mHospitalPatId = cursor.getString(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.HOSPITAL_PAT_ID));
                        String mLocationId = cursor.getString(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.LOCATION_ID));
                        String imagePath = cursor.getString(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.IMAGE_PATH));
                        String caption = cursor.getString(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.PARENT_CAPTION));
                        String aptId = cursor.getString(cursor.getColumnIndex(AppDBHelper.MY_RECORDS.APT_ID));

                        String currentOpdTime;

                        if (mOpdtime.equals(""))
                            currentOpdTime = CommonMethods.getCurrentTimeStamp(DMSConstants.DATE_PATTERN.HH_mm_ss);
                        else
                            currentOpdTime = mOpdtime;

                        String visitDateToPass = CommonMethods.getFormattedDate(visitDate, DMSConstants.DATE_PATTERN.DD_MM_YYYY, DMSConstants.DATE_PATTERN.YYYY_MM_DD);

                        try {

                            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(context, uploadId, Url)
                                    .setUtf8Charset()

                                    .setMaxRetries(DMSConstants.MAX_RETRIES)
                                    .addHeader(DMSConstants.AUTHORIZATION_TOKEN, authorizationString)
                                    .addHeader(DMSConstants.DEVICEID, device.getDeviceId())
                                    .addHeader(DMSConstants.OS, device.getOS())
                                    .addHeader(DMSConstants.OSVERSION, device.getOSVersion())
                                    .addHeader(DMSConstants.DEVICE_TYPE, device.getDeviceType())

                                    .addHeader("patientid", patientId)
                                    .addHeader("docid", String.valueOf(docId))
                                    .addHeader("opddate", visitDateToPass)
                                    .addHeader("opdtime", currentOpdTime)
                                    .addHeader("opdid", opdId)
                                    .addHeader("hospitalid", mHospitalId)
                                    .addHeader("hospitalpatid", mHospitalPatId)
                                    .addHeader("locationid", mLocationId)
                                    .addHeader("aptid", String.valueOf(aptId))
                                    .addFileToUpload(imagePath, "attachment");

                            String docCaption;

                            if (!caption.isEmpty()) {
                                docCaption = caption;
                            } else
                                docCaption = CommonMethods.stripExtension(CommonMethods.getFileNameFromPath(imagePath));

                            uploadRequest.addHeader("captionname", docCaption);

                            uploadConfig.getProgress().message = uploadConfig.getProgress().message.replace("record", docCaption);
                            uploadRequest.setNotificationConfig(uploadConfig);

                            uploadRequest.startUpload();

                        } catch (MalformedURLException | FileNotFoundException fe) {
                            fe.printStackTrace();
                        }
                    }

                    cursor.moveToNext();
                }
            }
        }

        cursor.close();
        appDBHelper.close();
    }

    public static UploadNotificationConfig getUploadConfig(Context context) {
        UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();
        uploadNotificationConfig.setIconColorForAllStatuses(context.getResources().getColor(R.color.tagColor));

        uploadNotificationConfig.setTitleForAllStatuses(context.getString(R.string.app_name))
                .setRingToneEnabled(true);

        uploadNotificationConfig.getProgress().message = "Uploading record at " + UPLOAD_RATE + " - " + PROGRESS;
//        uploadNotificationConfig.getProgress().iconResourceID = R.drawable.ic_file_upload_white_24dp;
        uploadNotificationConfig.getProgress().iconColorResourceID = context.getResources().getColor(R.color.tagColor);

        uploadNotificationConfig.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
//        uploadNotificationConfig.getCompleted().iconResourceID = R.drawable.ic_file_upload_white_24dp;
        uploadNotificationConfig.getCompleted().iconColorResourceID = Color.GREEN;

        uploadNotificationConfig.getError().message = "Error while uploading";
//        uploadNotificationConfig.getError().iconResourceID = R.drawable.ic_file_upload_white_24dp;
        uploadNotificationConfig.getError().iconColorResourceID = Color.RED;

        uploadNotificationConfig.getCancelled().message = "Upload has been cancelled";
//        uploadNotificationConfig.getCancelled().iconResourceID = R.drawable.ic_file_upload_white_24dp;
        uploadNotificationConfig.getCancelled().iconColorResourceID = Color.YELLOW;

        return uploadNotificationConfig;
    }

    void onCreate(Context mContext) {
        this.context = mContext;
        appDBHelper = new AppDBHelper(context);
        if (DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.LOGIN_STATUS, mContext).equals(DMSConstants.YES))
            check();
        broadcastReceiver.register(context);
    }

    void onDestroy() {
        broadcastReceiver.unregister(context);
    }

    private UploadServiceBroadcastReceiver broadcastReceiver = new UploadServiceBroadcastReceiver() {
        @Override
        public void onProgress(Context context, UploadInfo uploadInfo) {
            // your implementation
        }

        @Override
        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
            // your implementation
            if (DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.LOGIN_STATUS, context).equals(DMSConstants.YES)) {
                appDBHelper.updateRecordUploads(uploadInfo.getUploadId(), FAILED);
                Intent intent = new Intent(DOC_UPLOAD);
                intent.putExtra(UPLOAD_INFO, uploadInfo);
                intent.putExtra(STATUS, FAILED);
                context.sendBroadcast(intent);
            }
        }

        @Override
        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
            // your implementation

            if (DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.LOGIN_STATUS, context).equals(DMSConstants.YES)) {
                appDBHelper.updateRecordUploads(uploadInfo.getUploadId(), COMPLETED);
                Intent intent = new Intent(DOC_UPLOAD);
                intent.putExtra(UPLOAD_INFO, uploadInfo);
                intent.putExtra(STATUS, COMPLETED);
                context.sendBroadcast(intent);
            }
        }

        @Override
        public void onCancelled(Context context, UploadInfo uploadInfo) {
            // your implementation
        }
    };
}
