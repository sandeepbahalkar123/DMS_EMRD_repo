package com.scorg.dms.services;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.scorg.dms.R;
import com.scorg.dms.broadcast_receivers.ReplayBroadcastReceiver;
import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.model.chat.InternetConnect;
import com.scorg.dms.model.chat.MQTTMessage;
import com.scorg.dms.model.chat.StatusInfo;
import com.scorg.dms.notification.MessageNotification;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.ui.activities.ChatActivity;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;
import com.scorg.dms.util.NetworkUtil;
import com.scorg.dms.util.rxnetwork.RxNetwork;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.scorg.dms.broadcast_receivers.ReplayBroadcastReceiver.MESSAGE_LIST;
import static com.scorg.dms.util.Config.BROKER;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.PENDING;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.REACHED;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.READ;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.SEEN;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.SENT;

public class MQTTService extends Service {

    public static final String KEY_REPLY = "key_replay";
    public static final String REPLY_ACTION = "com.scorg.dms.REPLY_ACTION"; // Change
    public static final String SEND_MESSAGE = "send_message";
    public static final String STATUS_INFO = "status_info";
    public static final String TOPIC_KEY = "topic_key";

    private static int currentChatUser = -1;
    private static final String TAG = "MQTTService";
    public static final String MESSAGE = "message";
    public static final String NOTIFY = "com.scorg.dms.NOTIFY"; // Change
    public static final String MESSAGE_ID = "message_id";

    public static final int MESSAGE_TOPIC = 0;
    public static final int USER_STATUS_TOPIC = 1;
    public static final int MESSAGE_STATUS_TOPIC = 2;
    public static final int INTERNET_TOPIC = 3;
    public static final int USER_TYPING_STATUS_TOPIC = 4;

    public static final String[] TOPIC = {"chat/connect/message", "chat/connect/userStatus", "chat/connect/messageStatus", "chat/connect/internet", "chat/connect/userTypingStatus"};
    public static final String DELIVERED = "delivered";

    public static final String DOCTOR = "user1";
    public static final String PATIENT = "user2";

    private MqttAsyncClient mqttClient;

    private Gson gson = new Gson();

    private Subscription sendStateSubscription;
    private int[] qos;

    private AppDBHelper appDBHelper;
    private MqttConnectOptions connOpts = new MqttConnectOptions();
    private Context mContext;
    private SyncOfflineRecords syncOfflineRecords = new SyncOfflineRecords();
    private SyncOfflinePatients syncOfflinePatients = new SyncOfflinePatients();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        syncOfflineRecords.onCreate(mContext);
        syncOfflinePatients.onCreate(mContext);
        initRxNetwork();
        appDBHelper = new AppDBHelper(mContext);

        //MQTT client id to use for the device. "" will generate a client id automatically
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            mqttClient = new MqttAsyncClient(BROKER, MqttAsyncClient.generateClientId(), persistence);
            initMqttCallback();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void initRxNetwork() {
        final Observable<InternetState> sendStateStream =
                RxNetwork.stream(this).map(new Func1<Boolean, InternetState>() {
                    @Override
                    public InternetState call(Boolean hasInternet) {
                        if (hasInternet)
                            return new InternetState("Online", true);
                        return new InternetState("Offline", false);
                    }
                });

        sendStateSubscription =
                sendStateStream.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<InternetState>() {
                            @Override
                            public void call(InternetState internetState) {
                                // do stuff here for UI
                                if (internetState.isEnabled) {

                                    try {
                                        if (!mqttClient.isConnected()) {
                                            mqttClient.reconnect();
                                        } else Log.d(TAG, "Connected");
                                    } catch (MqttException ignored) {
                                        ignored.getStackTrace();
                                    }

                                    // checking pending uploads
                                    if (DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.LOGIN_STATUS, mContext).equals(DMSConstants.YES)) {
                                        syncOfflineRecords.check();
                                        syncOfflinePatients.check();
                                    }
                                }
                            }
                        });
    }

    IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return mBinder;
    }

    public void setCurrentChatUser(int currentChatUser) {
        MQTTService.currentChatUser = currentChatUser;
    }

    public void checkPendingRecords() {
        syncOfflineRecords.check();
    }

    public class LocalBinder extends Binder {
        public MQTTService getServerInstance() {
            return MQTTService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getBooleanExtra(SEND_MESSAGE, false))
                passMessage((MQTTMessage) intent.getParcelableExtra(MESSAGE_LIST));
        }
        return START_STICKY;
    }

    void initMqttCallback() {

        qos = new int[TOPIC.length];
        for (int index = 0; index < TOPIC.length; index++)
            qos[index] = 1;

        try {
            mqttClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    CommonMethods.Log("MqttCallbackExtended", String.valueOf(reconnect));

                    try {
                        mqttClient.subscribe(TOPIC, qos);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                    passInternetConnect();
                }

                public void messageArrived(final String topic, final MqttMessage msg) {
                    String payloadString = new String(msg.getPayload());
                    Log.d(TAG + "Received:", topic + " " + payloadString);

                    try {
                        if (!msg.isDuplicate()) {
                            String myid = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext);
                            String userLogin = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.LOGIN_STATUS, mContext);

                            if (userLogin.equals(DMSConstants.YES)) {
                                if (topic.equals(TOPIC[MESSAGE_TOPIC])) {
                                    MQTTMessage messageL = gson.fromJson(payloadString, MQTTMessage.class);
                                    if (myid.equals(String.valueOf(messageL.getDocId()))) { // Change
                                        messageL.setTopic(topic);
                                        if (!messageL.getSender().equals(MQTTService.DOCTOR)) {

                                            // change
                                            StatusInfo statusInfo = new StatusInfo();
                                            statusInfo.setMsgId(messageL.getMsgId());
                                            statusInfo.setDocId(messageL.getDocId());
                                            statusInfo.setPatId(messageL.getPatId());
                                            statusInfo.setSender(messageL.getSender());

                                            if (currentChatUser != messageL.getPatId()) {

                                                ArrayList<MQTTMessage> messagesTemp = new ArrayList<>();
                                                ArrayList<MQTTMessage> messages = appDBHelper.insertChatMessage(messageL); // Change

                                                if (messages.size() > 6) {
                                                    for (int index = messages.size() - 6; index < messages.size(); index++)
                                                        messagesTemp.add(messages.get(index));
                                                } else messagesTemp.addAll(messages);

                                                // change
                                                statusInfo.setMessageStatus(REACHED);

                                                MessageNotification.notify(mContext, messagesTemp, messageL.getSenderName(), getProfilePhotoBitmap(messageL), (int) appDBHelper.unreadChatMessageCountByPatientId(messageL.getPatId()), getReplyPendingIntent(messageL), messageL.getPatId()); // Change
                                            } else {
                                                // change
                                                messageL.setReadStatus(READ);
                                                appDBHelper.insertChatMessage(messageL); // Change
                                                statusInfo.setMessageStatus(SEEN);
                                            }

                                            Intent intent = new Intent(NOTIFY);
                                            intent.putExtra(MESSAGE, messageL);
                                            intent.putExtra(TOPIC_KEY, topic);
                                            sendBroadcast(intent);

                                            messageStatus(statusInfo);

                                        } else Log.d(TAG + " DOCTOR_MES", payloadString);
                                    } else Log.d(TAG + " OTHERS_MES", payloadString);
                                } else
                                    broadcastStatus(payloadString, topic);
                            }
                        } else Log.d(TAG + " LOGOUT_MES", payloadString);
                    } catch (JsonSyntaxException e) {
                        Log.d(TAG + " MESSAGE", "JSON_EXCEPTION" + payloadString);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Delivery Complete");
                    Log.d(TAG + " MESSAGE_ID", String.valueOf(token.getMessageId()));

                    Intent intent = new Intent(NOTIFY);
                    intent.putExtra(MESSAGE_ID, String.valueOf(token.getMessageId()));
                    intent.putExtra(DELIVERED, true);
                    sendBroadcast(intent);
                }

                @Override
                public void connectionLost(Throwable arg0) {
                    Log.d(TAG, "Connection Lost");
                }
            });

            connOpts.setCleanSession(false);
            connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            connOpts.setAutomaticReconnect(true);
//            connOpts.setWill(TOPIC[MESSAGE_STATUS_TOPIC], "Message Reached".getBytes(), 1, true);
//            connOpts.setWill(TOPIC[USER_STATUS_TOPIC], "TypeStatus Reached".getBytes(), 1, true);
//            connOpts.setKeepAliveInterval(120);
//            connOpts.setUserName("ganesh");
//            String password = "windows10";
//            connOpts.setPassword(password.toCharArray());

            IMqttActionListener mqttConnect = new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    CommonMethods.Log(TAG, "Connected");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Not Connected");
                    try {
                        mqttClient.reconnect();
                        Log.d(TAG, "ReConnecting");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            };
            mqttClient.connect(connOpts, mContext, mqttConnect);

        } catch (MqttException me) {
            Log.e(TAG + "reason ", "" + me.getReasonCode());
            Log.e(TAG + "msg ", "" + me.getMessage());
            Log.e(TAG + "loc ", "" + me.getLocalizedMessage());
            Log.e(TAG + "cause ", "" + me.getCause());
            Log.e(TAG + "excep ", "" + me);
            me.printStackTrace();
        }
    }

    // change
    private void broadcastStatus(String payloadString, String topic) {
        StatusInfo statusInfo = gson.fromJson(payloadString, StatusInfo.class);
        if (statusInfo.getSender().equals(MQTTService.DOCTOR)) {

            if (TOPIC[MESSAGE_STATUS_TOPIC].equals(topic))
                appDBHelper.updateChatMessageStatus(statusInfo);

            Intent intent = new Intent(NOTIFY);
            intent.putExtra(MESSAGE, statusInfo);
            intent.putExtra(TOPIC_KEY, topic);
            sendBroadcast(intent);
        }
    }

    private void passInternetConnect() {
        try {
            String myId = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext);

            InternetConnect internetConnect = new InternetConnect();
            internetConnect.setUserId(Integer.parseInt(myId));
            internetConnect.setSender(DOCTOR);

            String content = gson.toJson(internetConnect, InternetConnect.class);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(1);
            message.setRetained(true);
            if (mqttClient.isConnected() && DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.LOGIN_STATUS, mContext).equals(DMSConstants.YES)) {
                mqttClient.publish(TOPIC[INTERNET_TOPIC], message);
                ArrayList<MQTTMessage> chatMessageByMessageStatus = appDBHelper.getChatMessageByMessageStatus(PENDING);
                for (MQTTMessage mqttMessage : chatMessageByMessageStatus)
                    passMessage(mqttMessage);
            } else
                mqttClient.reconnect();
            CommonMethods.Log("passInternetStatus: ", content);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // change
    public void messageStatus(StatusInfo statusInfo) {
        try {
            // 2017-10-13 13:08:07
            String msgTime = CommonMethods.getCurrentTimeStamp(DMSConstants.DATE_PATTERN.UTC_PATTERN);
            statusInfo.setMsgTime(msgTime);
            String content = gson.toJson(statusInfo, StatusInfo.class);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(1);
            message.setRetained(true);
            if (mqttClient.isConnected())
                mqttClient.publish(TOPIC[MESSAGE_STATUS_TOPIC], message);
            else
                mqttClient.reconnect();
            CommonMethods.Log("passMessageStatus: ", content);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void typingStatus(StatusInfo statusInfo) {
        try {
            // 2017-10-13 13:08:07
            String msgTime = CommonMethods.getCurrentTimeStamp(DMSConstants.DATE_PATTERN.UTC_PATTERN);
            statusInfo.setMsgTime(msgTime);
            String content = gson.toJson(statusInfo, StatusInfo.class);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(1);
            message.setRetained(true);
            if (mqttClient.isConnected())
                mqttClient.publish(TOPIC[USER_TYPING_STATUS_TOPIC], message);
            else
                mqttClient.reconnect();
            CommonMethods.Log("passTypeStatus: ", content);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void passMessage(MQTTMessage mqttMessage) {

        if (NetworkUtil.getConnectivityStatusBoolean(this)) {
            try {
                mqttMessage.setDateVisible(false);
                String content = gson.toJson(mqttMessage, MQTTMessage.class);
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(1);
                message.setRetained(true);
                if (mqttClient.isConnected()) {
                    mqttMessage.setMsgStatus(SENT);
                    mqttClient.publish(TOPIC[MESSAGE_TOPIC], message);
                } else {
                    mqttClient.reconnect();
                    CommonMethods.Log("passMessage: ", content);
                    mqttMessage.setMsgStatus(PENDING);
                }
            } catch (MqttException e) {
                mqttMessage.setMsgStatus(PENDING);
            } finally {
                appDBHelper.insertChatMessage(mqttMessage);
            }
        } else {
            mqttMessage.setMsgStatus(PENDING);
            appDBHelper.insertChatMessage(mqttMessage);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
                CommonMethods.Log(TAG, "disconnect");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else CommonMethods.Log(TAG, "Not Connected 1");
        sendStateSubscription.unsubscribe();
        sendStateSubscription = null;
        syncOfflineRecords.onDestroy();
    }


    private static class InternetState {
        final boolean isEnabled;
        final String state;

        InternetState(String state, boolean isEnabled) {
            this.isEnabled = isEnabled;
            this.state = state;
        }
    }
// new code

    private PendingIntent getReplyPendingIntent(MQTTMessage mqttMessage) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // start a
            // (i)  broadcast receiver which runs on the UI thread or
            // (ii) service for a background task to b executed , but for the purpose of this codelab, will be doing a broadcast receiver
            intent = ReplayBroadcastReceiver.getReplyMessageIntent(this, mqttMessage);
            return PendingIntent.getBroadcast(getApplicationContext(), mqttMessage.getDocId(), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            // start your activity for Android M and below
            intent = new Intent(mContext, ChatActivity.class);
            intent.setAction(REPLY_ACTION);
            intent.putExtra(MESSAGE_LIST, mqttMessage);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return PendingIntent.getActivity(this, mqttMessage.getDocId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public static CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_REPLY);
        }
        return null;
    }

    // getBitmap from message

    @SuppressLint("CheckResult")
    private Bitmap getProfilePhotoBitmap(final MQTTMessage messageL) {

        TextDrawable mReceiverDrawable = null;
        String doctorName = messageL.getSenderName();
        String doctorPhoto = messageL.getSenderImgUrl();

        if (!doctorName.isEmpty()) {
            doctorName = doctorName.replace("Dr. ", "");
            int color2 = ColorGenerator.MATERIAL.getColor(doctorName);
            mReceiverDrawable = TextDrawable.builder()
                    .beginConfig()
                    .toUpperCase()
                    .width(Math.round(getResources().getDimension(R.dimen.dp40)))  // width in px
                    .height(Math.round(getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + doctorName.charAt(0)), color2);
        }

        if (doctorPhoto != null) {
            if (!doctorPhoto.isEmpty()) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();
                requestOptions.circleCrop();
                requestOptions.placeholder(mReceiverDrawable);
                requestOptions.error(mReceiverDrawable);

                try {
                    return Glide.with(this)
                            .asBitmap()
                            .load(doctorPhoto)
                            .apply(requestOptions)
                            .submit()
                            .get();

                } catch (Exception e) {
                    return drawableToBitmap(mReceiverDrawable);
                }
            } else return drawableToBitmap(mReceiverDrawable);
        } else return drawableToBitmap(mReceiverDrawable);
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        else
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
