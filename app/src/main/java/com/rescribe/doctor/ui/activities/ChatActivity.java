package com.rescribe.doctor.ui.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.chat.ChatAdapter;
import com.rescribe.doctor.broadcast_receivers.ReplayBroadcastReceiver;
import com.rescribe.doctor.helpers.database.AppDBHelper;
import com.rescribe.doctor.model.chat.MQTTMessage;
import com.rescribe.doctor.model.chat.StatusInfo;
import com.rescribe.doctor.model.patient.patient_connect.PatientData;
import com.rescribe.doctor.notification.MessageNotification;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.services.MQTTService;
import com.rescribe.doctor.singleton.Device;
import com.rescribe.doctor.ui.activities.my_patients.patient_history.PatientHistoryActivity;
import com.rescribe.doctor.ui.customesViews.CircularImageView;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.Config;
import com.rescribe.doctor.util.KeyboardEvent;
import com.rescribe.doctor.util.RescribeConstants;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import ng.max.slideview.SlideView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE;
import static com.rescribe.doctor.services.MQTTService.DOCTOR;
import static com.rescribe.doctor.services.MQTTService.MESSAGE_STATUS_TOPIC;
import static com.rescribe.doctor.services.MQTTService.MESSAGE_TOPIC;
import static com.rescribe.doctor.services.MQTTService.NOTIFY;
import static com.rescribe.doctor.services.MQTTService.PATIENT;
import static com.rescribe.doctor.services.MQTTService.REPLY_ACTION;
import static com.rescribe.doctor.services.MQTTService.TOPIC;
import static com.rescribe.doctor.services.MQTTService.USER_STATUS_TOPIC;
import static com.rescribe.doctor.services.MQTTService.USER_TYPING_STATUS_TOPIC;
import static com.rescribe.doctor.ui.activities.PatientConnectActivity.FREE;
import static com.rescribe.doctor.util.CommonMethods.toCamelCase;
import static com.rescribe.doctor.util.RescribeConstants.FILE.AUD;
import static com.rescribe.doctor.util.RescribeConstants.FILE.DOC;
import static com.rescribe.doctor.util.RescribeConstants.FILE.IMG;
import static com.rescribe.doctor.util.RescribeConstants.FILE_STATUS.COMPLETED;
import static com.rescribe.doctor.util.RescribeConstants.FILE_STATUS.FAILED;
import static com.rescribe.doctor.util.RescribeConstants.FILE_STATUS.UPLOADING;
import static com.rescribe.doctor.util.RescribeConstants.MESSAGE_STATUS.PENDING;
import static com.rescribe.doctor.util.RescribeConstants.MESSAGE_STATUS.REACHED;
import static com.rescribe.doctor.util.RescribeConstants.MESSAGE_STATUS.SEEN;
import static com.rescribe.doctor.util.RescribeConstants.MESSAGE_STATUS.SENT;
import static com.rescribe.doctor.util.RescribeConstants.SALUTATION;
import static com.rescribe.doctor.util.RescribeConstants.USER_STATUS.ONLINE;
import static com.rescribe.doctor.util.RescribeConstants.USER_STATUS.TYPING;

@RuntimePermissions
public class ChatActivity extends AppCompatActivity implements ChatAdapter.ItemListener {

    // Audio
    private static final String LOG_TAG = "AudioRecordTest";
    public static final String SEARCHED_TEXT = "searched_string";
    private static String mFileName = null;
    @BindView(R.id.consultationLayout)
    CustomTextView consultationLayout;
    @BindView(R.id.bookAppointmentLayout)
    LinearLayout bookAppointmentLayout;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private ImageView audioIcon;
    private boolean isPlaying = false;
    private int prePosition = -1;
    // Audio End
    private static final int MAX_ATTACHMENT_COUNT = 10;
    private static final String RESCRIBE_FILES = "/DrRescribe/Files/";
    private static final String RESCRIBE_PHOTOS = "/DrRescribe/Photos/";
    private static final String RESCRIBE_AUDIO = "/DrRescribe/Audios/";
    private static final String RESCRIBE_UPLOAD_FILES = "/DrRescribe/SentFiles/";
    private static final String RESCRIBE_UPLOAD_PHOTOS = "/DrRescribe/SentPhotos/";
    private static final String RESCRIBE_UPLOAD_AUDIO = "/DrRescribe/SentAudios/";
    private String filesFolder;
    private String photosFolder;
    private String audioFolder;
    private String filesUploadFolder;
    private String photosUploadFolder;
    private String audioUploadFolder;

    @BindView(R.id.backButton)
    ImageView backButton;
    @BindView(R.id.profilePhoto)
    CircularImageView profilePhoto;
    @BindView(R.id.onlineStatusIcon)
    ImageView onlineStatusIcon;
    @BindView(R.id.receiverName)
    CustomTextView receiverName;
    @BindView(R.id.onlineStatus)
    CustomTextView dateTime;
    @BindView(R.id.titleLayout)
    RelativeLayout titleLayout;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.chatList)
    RecyclerView chatRecyclerView;
    @BindView(R.id.messageType)
    EditText messageType;
    @BindView(R.id.attachmentButton)
    ImageButton attachmentButton;
    @BindView(R.id.cameraButton)
    ImageButton cameraButton;
    @BindView(R.id.buttonLayout)
    LinearLayout buttonLayout;
    @BindView(R.id.messageTypeSubLayout)
    RelativeLayout messageTypeSubLayout;
    @BindView(R.id.sendButton)
    ImageView sendButton;
    @BindView(R.id.messageTypeLayout)
    RelativeLayout messageTypeLayout;
    @BindView(R.id.audioSlider)
    SlideView audioSlider;
    @BindView(R.id.dateTextView)
    TextView dateTextView;
    @BindView(R.id.reveal_items)
    CardView mRevealView;
    @BindView(R.id.exitRevealDialog)
    FrameLayout exitRevealDialog;

    @BindView(R.id.mainRelativeLayout)
    RelativeLayout mainRelativeLayout;

    // Check Typing
    private boolean isCallFromPatientList;
    final int TYPING_TIMEOUT = 3000; // 5 seconds timeout
    private static final String TYPING_MESSAGE = "typing...";
    final Handler timeoutHandler = new Handler();
    private boolean isTyping;

    final Runnable typingTimeout = new Runnable() {
        public void run() {
            isTyping = false;
            typingStatus();
        }
    };

    private boolean mPressed = false;
    private SupportAnimator mAnimator;
    private boolean hidden = true;
    private String searchedMessageString;
    private int scrollPosition = -1;
    private String patientName = "";
    private int mHospitalPatId;
    private int preSize;
    private int mClinicId;

    private void typingStatus() {
        StatusInfo statusInfo = new StatusInfo();
        String generatedId = TYPING + mqttMessages.size() + "_" + System.nanoTime();
        statusInfo.setMsgId(generatedId);
        statusInfo.setDocId(Integer.parseInt(docId));
        statusInfo.setPatId(chatList.getId());
        statusInfo.setSender(PATIENT);
        statusInfo.setTypeStatus(isTyping);
        if (mqttService != null)
            mqttService.typingStatus(statusInfo);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null) {

                if (intent.getAction().equals(NOTIFY)) {

                    String topic = intent.getStringExtra(MQTTService.TOPIC_KEY);
                    topic = topic == null ? "" : topic;

                    if (intent.getBooleanExtra(MQTTService.DELIVERED, false)) {

                        Log.d(TAG, "Delivery Complete");
                        Log.d(TAG + " MESSAGE_ID", intent.getStringExtra(MQTTService.MESSAGE_ID));

                    } else if (topic.equals(TOPIC[MESSAGE_TOPIC])) {

                        // User message
                        CommonMethods.Log(TAG, "User message");

                        MQTTMessage message = intent.getParcelableExtra(MQTTService.MESSAGE);
                        if (message.getPatId() == chatList.getId()) {
                            if (chatAdapter != null) {
                                mqttMessages.add(message);
                                chatAdapter.notifyItemInserted(mqttMessages.size() - 1);
                                chatRecyclerView.smoothScrollToPosition(mqttMessages.size() - 1);
                            }
                        } else {
                            // Other user message
                            CommonMethods.Log(TAG, "Other user message");
                        }
                    } else if (topic.equals(TOPIC[MESSAGE_STATUS_TOPIC])) {

                        // Message Status
                        CommonMethods.Log(TAG, "message status");
                        StatusInfo statusInfo = intent.getParcelableExtra(MQTTService.MESSAGE);
                        if (statusInfo.getPatId() == chatList.getId() && Integer.parseInt(docId) == statusInfo.getDocId()) {

                            switch (statusInfo.getMessageStatus()) {
                                case SEEN:
                                    // change message status as a read
                                    for (MQTTMessage mqttMessage : mqttMessages) {
                                        if (!mqttMessage.getMsgStatus().equals(SEEN))
                                            mqttMessage.setMsgStatus(SEEN);
                                    }

                                    chatAdapter.notifyDataSetChanged();

                                    break;
                                case REACHED:
                                    // change message status as a reached
                                    for (MQTTMessage mqttMessage : mqttMessages) {
                                        if (mqttMessage.getMsgStatus().equals(SENT) || mqttMessage.getMsgStatus().equals(PENDING))
                                            mqttMessage.setMsgStatus(REACHED);
                                    }

                                    chatAdapter.notifyDataSetChanged();
                                    break;
                                case SENT:
                                    // change message status as a read
                                    for (MQTTMessage mqttMessage : mqttMessages) {
                                        if (mqttMessage.getMsgStatus().equals(PENDING))
                                            mqttMessage.setMsgStatus(SENT);
                                    }

                                    chatAdapter.notifyDataSetChanged();

                                    break;
                            }
                        } else {
                            // Other use message
                            CommonMethods.Log(TAG, "Other user status");
                        }
                    } else if (topic.equals(TOPIC[USER_TYPING_STATUS_TOPIC])) {

                        // Typing Status
                        CommonMethods.Log(TAG, "typing status");
                        StatusInfo statusInfo = intent.getParcelableExtra(MQTTService.MESSAGE);
                        if (statusInfo.getPatId() == chatList.getId() && Integer.parseInt(docId) == statusInfo.getDocId()) {

                            if (statusInfo.isTyping()) {
                                dateTime.setText(TYPING_MESSAGE);
                                dateTime.setTextColor(Color.WHITE);
                            } else {
                                dateTime.setText(chatList.getOnlineStatus());
                                //setUserStatusColor(chatList.getOnlineStatus());
                            }
                        }
                    } else if (topic.equals(TOPIC[USER_STATUS_TOPIC])) {
                        // User Status
                        CommonMethods.Log(TAG, "User status");
                    }
                } else if (intent.getAction().equals(ACTION_DOWNLOAD_COMPLETE)) {
                    checkDownloaded();
                }
            } else CommonMethods.Log(TAG, "null action");
        }
    };
/*
    private void setUserStatusColor(String onlineStatus) {
        if (onlineStatus.equalsIgnoreCase(ONLINE)) {
            onlineStatusIcon.setVisibility(View.VISIBLE);
            dateTime.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.green_light));
        } else if (onlineStatus.equalsIgnoreCase(IDLE)) {
            dateTime.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.range_yellow));
            onlineStatusIcon.setVisibility(View.INVISIBLE);
        } else if (onlineStatus.equalsIgnoreCase(OFFLINE)) {
            dateTime.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.grey_500));
            onlineStatusIcon.setVisibility(View.INVISIBLE);
        }
    }*/

    void checkDownloaded() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
        Cursor c = downloadManager.query(query);

        if (c.moveToFirst()) {
            do {
                String fileUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                String fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                for (int index = mqttMessages.size() - 1; index >= 0; index--) {
                    if (mqttMessages.get(index).getMsg().equals(fileName)) {
                        mqttMessages.get(index).setDownloadStatus(COMPLETED);
                        mqttMessages.get(index).setFileUrl(fileUri);
                        chatAdapter.notifyItemChanged(index);
                        break;
                    }
                    Log.i(TAG, "downloaded file " + fileUri);
                }
            } while (c.moveToNext());
        }
    }

    private static final String TAG = "ChatActivity";
    private ChatAdapter chatAdapter;
    private ArrayList<MQTTMessage> mqttMessages = new ArrayList<>();

    private String docId;
    private TextDrawable mSelfDrawable;
    private TextDrawable mReceiverDrawable;

    // load more
    int next = 1;

    private AppDBHelper appDBHelper;
    private String docName;
    private String imageUrl = "";
    private String speciality = "";

    private PatientData chatList;

    // Uploading
    private Device device;
    private String Url;
    private String authorizationString;
    private UploadNotificationConfig uploadNotificationConfig;
    private DownloadManager downloadManager;
    private String mPatientsDetails = "";

    @Override
    public void onBackPressed() {
        if (mPressed) {
            openBottomSheetMenu();
        } else {
            if (isCallFromPatientList) {
                if (mqttMessages.isEmpty())
                    setResult(Activity.RESULT_CANCELED);
                else {
                    if (preSize != mqttMessages.size()) {
                        Intent in = new Intent();
                        chatList.setLastChatTime(mqttMessages.get(mqttMessages.size() - 1).getMsgTime());
                        in.putExtra(RescribeConstants.CHAT_USERS, chatList);
                        setResult(Activity.RESULT_OK, in);
                    } else setResult(Activity.RESULT_CANCELED);
                }
            } else setResult(Activity.RESULT_CANCELED);
            super.onBackPressed();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        mRevealView.setVisibility(View.INVISIBLE);
        exitRevealDialog.setVisibility(View.GONE);

        appDBHelper = new AppDBHelper(this);

        docId = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, this);
        docName = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, this);
        imageUrl = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.PROFILE_PHOTO, this);
        speciality = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SPECIALITY, this);

        downloadInit();
        uploadInit();
        audioSliderInit();

        new KeyboardEvent(mainRelativeLayout, new KeyboardEvent.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                bookAppointmentLayout.setVisibility(View.GONE);
            }

            @Override
            public void onKeyboardClose() {
                bookAppointmentLayout.setVisibility(View.VISIBLE);
            }
        });

        Intent gotIntent = getIntent();

            mPatientsDetails = gotIntent.getStringExtra(RescribeConstants.PATIENT_DETAILS);
            mHospitalPatId = gotIntent.getIntExtra(RescribeConstants.PATIENT_HOS_PAT_ID, 0);
            mClinicId = gotIntent.getIntExtra(RescribeConstants.CLINIC_ID, 0);

        if (gotIntent.getAction() != null) {
            if (gotIntent.getAction().equals(REPLY_ACTION)) {
                chatList = new PatientData();
                MQTTMessage mqttMessage = gotIntent.getParcelableExtra(ReplayBroadcastReceiver.MESSAGE_LIST);
                chatList.setPatientName(mqttMessage.getSenderName());
                chatList.setId(mqttMessage.getPatId());
                chatList.setOnlineStatus(ONLINE); // hardcoded
                chatList.setUnreadMessages(0);
                chatList.setSalutation(mqttMessage.getSalutation());
                chatList.setImageUrl(mqttMessage.getSenderImgUrl());

                searchedMessageString = gotIntent.getStringExtra(SEARCHED_TEXT);
            } else
                chatList = gotIntent.getParcelableExtra(RescribeConstants.PATIENT_INFO);
        } else
            chatList = gotIntent.getParcelableExtra(RescribeConstants.PATIENT_INFO);
        isCallFromPatientList = getIntent().getBooleanExtra(RescribeConstants.IS_CALL_FROM_MY_PATIENTS, false);

        String salutation;
        if (chatList.getSalutation() != 0)
            salutation = SALUTATION[chatList.getSalutation() - 1];
        else salutation = "";

        receiverName.setText(salutation + chatList.getPatientName());
        String patientName = chatList.getPatientName();

        // Need to add self profile photo

        if (patientName != null) {
            if (!patientName.isEmpty()) {
                int color2 = ColorGenerator.MATERIAL.getColor(patientName);
                mReceiverDrawable = TextDrawable.builder()
                        .beginConfig()
                        .width(Math.round(getResources().getDimension(R.dimen.dp40)))  // width in px
                        .height(Math.round(getResources().getDimension(R.dimen.dp40))) // height in px
                        .endConfig()
                        .buildRound(("" + patientName.charAt(0)).toUpperCase(), color2);
            }
        }

        if (!chatList.getImageUrl().equals("")) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.override(CommonMethods.convertDpToPixel(40), CommonMethods.convertDpToPixel(40));
            requestOptions.placeholder(mReceiverDrawable);
            requestOptions.error(mReceiverDrawable);

            Glide.with(ChatActivity.this)
                    .load(chatList.getImageUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(profilePhoto);
        } else profilePhoto.setImageDrawable(mReceiverDrawable);


        String doctorName = docName;
        if (doctorName != null) {
            doctorName = doctorName.replace("Dr. ", "");
            int color2 = ColorGenerator.MATERIAL.getColor(doctorName);
            mSelfDrawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(getResources().getDimension(R.dimen.dp40)))  // width in px
                    .height(Math.round(getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + doctorName.charAt(0)).toUpperCase(), color2);
        }

        dateTime.setText("" + chatList.getOnlineStatus());

        // this online status feature removed for now. :START ;;;;;
        //setUserStatusColor(chatList.getOnlineStatus());

        /*   if (chatList.getOnlineStatus().equalsIgnoreCase(ONLINE))
            onlineStatusIcon.setVisibility(View.VISIBLE);
        else
            onlineStatusIcon.setVisibility(View.GONE);*/
        // this online status feature removed for now.END ;;;;;

//        chatHelper = new ChatHelper(this, this);

        loadMessageFromDatabase();

        preSize = mqttMessages.size();

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(mLayoutManager);

        // off recyclerView Animation
        RecyclerView.ItemAnimator animator = chatRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

        chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int positionView = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (prePosition != positionView) {
                    MQTTMessage mqttMessage = ChatActivity.this.mqttMessages.get(positionView);
                    String timeText = CommonMethods.getDayFromDateTime(mqttMessage.getMsgTime(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE_PATTERN.DD_MMMM_YYYY, null);
                    dateTextView.setText(timeText);

                    prePosition = positionView;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:

                        dateTextView.animate()
                                .alpha(0.0f)
                                .setDuration(1000)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        dateTextView.setVisibility(View.GONE);
                                    }
                                });

                        CommonMethods.Log(TAG, "The RecyclerView is not scrolling");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:

                        dateTextView.animate()
                                .alpha(1.0f)
                                .setDuration(300)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        dateTextView.setVisibility(View.VISIBLE);
                                    }
                                });

                        CommonMethods.Log(TAG, "Scrolling now");
                        break;
                }
            }
        });

        chatAdapter = new ChatAdapter(mqttMessages, mSelfDrawable, mReceiverDrawable, ChatActivity.this, searchedMessageString);
        chatRecyclerView.setAdapter(chatAdapter);

        if (!mqttMessages.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (scrollPosition == -1)
                        chatRecyclerView.smoothScrollToPosition(mqttMessages.size() - 1);
                    else chatRecyclerView.smoothScrollToPosition(scrollPosition);
                }
            }, mqttMessages.size() > 500 ? 200 : 100);
        }

        messageType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // reset the timeout
                timeoutHandler.removeCallbacks(typingTimeout);
                if (messageType.getText().toString().trim().length() > 0) {

                    audioSlider.setVisibility(View.INVISIBLE);
                    sendButton.setVisibility(View.VISIBLE);
                    cameraButton.setVisibility(View.GONE);
                    // Typing status
                    // schedule the timeout
                    timeoutHandler.postDelayed(typingTimeout, TYPING_TIMEOUT);
                    if (!isTyping) {
                        isTyping = true;
                        typingStatus();
                    }
                    // End Typing status
                } else {

                    audioSlider.setVisibility(View.VISIBLE);
                    sendButton.setVisibility(View.INVISIBLE);
                    cameraButton.setVisibility(View.VISIBLE);
                    // Typing status
                    isTyping = false;
                    typingStatus();
                    // End typing status
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //----------
    }

    private void loadMessageFromDatabase() {
        mqttMessages.clear();
        Cursor cursor = appDBHelper.getChatMessagesByPatientId(chatList.getId());
        int count = 0;

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MQTTMessage mqttMessage = new MQTTMessage();

                mqttMessage.setMsgId(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG_ID)));
                mqttMessage.setMsg(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG)));
                mqttMessage.setMsgTime(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG_TIME)));
                mqttMessage.setSender(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER)));
                mqttMessage.setPatId(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.USER2ID)));
                mqttMessage.setDocId(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.USER1ID)));
                mqttMessage.setSenderName(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER_NAME)));

                mqttMessage.setSpecialization(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SPECIALITY)));

                mqttMessage.setMsgStatus(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG_STATUS)) == null ? SENT : cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG_STATUS)));

                mqttMessage.setSenderImgUrl(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER_IMG_URL)));
                mqttMessage.setFileUrl(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.FILE_URL)));
                mqttMessage.setFileType(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.FILE_TYPE)));

                mqttMessage.setSalutation(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SALUTATION)));
                mqttMessage.setReceiverName(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.RECEIVER_NAME)));
                mqttMessage.setReceiverImgUrl(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.RECEIVER_IMG_URL)));

                mqttMessage.setUploadStatus(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.UPLOAD_STATUS)));
                mqttMessage.setDownloadStatus(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.DOWNLOAD_STATUS)));
                mqttMessage.setReadStatus(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.READ_STATUS)));


                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
                Cursor cu = downloadManager.query(query);

                mqttMessage.setUploadStatus(COMPLETED);

                if (mqttMessage.getSender().equals(DOCTOR)) {
                    if (mqttMessage.getFileType().equals(AUD))
                        mqttMessage.setFileUrl(audioUploadFolder + mqttMessage.getMsg());
                    else if (mqttMessage.getFileType().equals(DOC))
                        mqttMessage.setFileUrl(filesUploadFolder + mqttMessage.getMsg());
                }

                // Check Download
                if (mqttMessage.getFileType().equals(DOC) || mqttMessage.getFileType().equals(AUD)) {
                    if (cu.moveToFirst()) {
                        do {
                            String fileUri = cu.getString(cu.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            String fileName = cu.getString(cu.getColumnIndex(DownloadManager.COLUMN_TITLE));
                            if (mqttMessage.getMsg().equals(fileName)) {
                                mqttMessage.setDownloadStatus(COMPLETED);
                                mqttMessage.setFileUrl(fileUri);
                            }
                        } while (cu.moveToNext());
                    }
                }

                if (searchedMessageString != null) {
                    String messageLowerCase = mqttMessage.getMsg().toLowerCase();
                    if (messageLowerCase.contains(searchedMessageString))
                        scrollPosition = count;
                }

                count += 1;
                mqttMessages.add(mqttMessage);
                cursor.moveToNext();
            }
        }
        cursor.close();
        appDBHelper.close();
    }

    // Audio Code

    private void audioSliderInit() {
        // Get Audio Permission

        // Record to the external cache directory for visibility
        mFileName = audioUploadFolder;

        ChatActivityPermissionsDispatcher.getAudioPermissionWithCheck(ChatActivity.this);

        Drawable leftDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_mic_red_24dp);
        audioSlider.getTextView().setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);

        audioSlider.getTextView().setCompoundDrawablePadding(CommonMethods.convertDpToPixel(5));
        audioSlider.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                messageTypeSubLayout.setVisibility(View.VISIBLE);
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(100);
                }
                cntr_aCounter.cancel();
                stopRecording(false);
                File file = new File(mFileName);
                boolean deleted = file.delete();
                mFileName = audioUploadFolder;
            }
        });

        audioSlider.setOnActionDownListener(new SlideView.OnActionDownListener() {
            @Override
            public void OnActionDown(SlideView slideView) {
                Log.d("Start", "Track");
                messageTypeSubLayout.setVisibility(View.INVISIBLE);
                mFileName += "Aud_" + System.nanoTime() + ".mp3";
                cntr_aCounter.start();
                startRecording();
            }
        });

        audioSlider.setOnActionUpListener(new SlideView.OnActionUpListener() {
            @Override
            public void OnActionUp(SlideView slideView) {
                Log.d("Stop", "Track");
                messageTypeSubLayout.setVisibility(View.VISIBLE);
                cntr_aCounter.cancel();
                stopRecording(audioCounter > 2);
            }
        });
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    void getAudioPermission() {
        CommonMethods.Log(TAG, "asked permission");
    }

    private void startPlaying(String path) {
        mPlayer = new MediaPlayer();
        try {
            audioIcon.setImageResource(R.drawable.ic_stop_white_24dp);
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                audioIcon.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                isPlaying = false;
            }
        });
    }

    private void stopPlaying() {
        audioIcon.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        try {
            mPlayer.release();
            mPlayer = null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            isPlaying = false;
        }

    }

    private int audioCounter = 0;
    CountDownTimer cntr_aCounter = new CountDownTimer(60_000, 1_000) {
        public void onTick(long millisUntilFinished) {
            // recodeing code
            NumberFormat f = new DecimalFormat("00");
            String time = "00:" + f.format(audioCounter) + "  " + getResources().getString(R.string.timing);
            audioSlider.getTextView().setText(time);
            audioCounter += 1;
        }

        public void onFinish() {
            //finish action
            try {
                stopRecording(true);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        try {
            mRecorder.start();
        } catch (Exception e) {
            Log.e(LOG_TAG, "prepare() start");
        }

    }

    private void stopRecording(boolean isSend) {
        CommonMethods.Log("isCanceled Recording : " + audioCounter, String.valueOf(isSend));
        audioCounter = 0;
        try {
            mRecorder.stop();
            mRecorder.release();
        } catch (RuntimeException ex) {
            //Ignore
        }
        mRecorder = null;

        if (isSend) {
            ArrayList<String> audioFile = new ArrayList<String>();
            audioFile.add(mFileName);
            uploadFiles(audioFile, RescribeConstants.FILE.AUD);
            mFileName = audioUploadFolder;
        }
    }

    // End Audio Code

    private void downloadInit() {
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        File sdCard = Environment.getExternalStorageDirectory();
        filesFolder = sdCard.getAbsolutePath() + RESCRIBE_FILES;
        photosFolder = sdCard.getAbsolutePath() + RESCRIBE_PHOTOS;
        audioFolder = sdCard.getAbsolutePath() + RESCRIBE_AUDIO;

        File dirFilesFolder = new File(filesFolder);
        if (!dirFilesFolder.exists()) {
            if (dirFilesFolder.mkdirs()) {
                Log.i(TAG, filesFolder + " Directory Created");
            }
        }
        File dirPhotosFolder = new File(photosFolder);
        if (!dirPhotosFolder.exists()) {
            if (dirPhotosFolder.mkdirs()) {
                Log.i(TAG, photosFolder + " Directory Created");
            }
        }
        File dirAudioFolder = new File(audioFolder);
        if (!dirAudioFolder.exists()) {
            if (dirAudioFolder.mkdirs()) {
                Log.i(TAG, audioFolder + " Directory Created");
            }
        }
    }

    private void uploadInit() {

        File sdCard = Environment.getExternalStorageDirectory();
        filesUploadFolder = sdCard.getAbsolutePath() + RESCRIBE_UPLOAD_FILES;
        photosUploadFolder = sdCard.getAbsolutePath() + RESCRIBE_UPLOAD_PHOTOS;
        audioUploadFolder = sdCard.getAbsolutePath() + RESCRIBE_UPLOAD_AUDIO;

        File dirFilesFolder = new File(filesUploadFolder);
        if (!dirFilesFolder.exists()) {
            if (dirFilesFolder.mkdirs()) {
                Log.i(TAG, filesUploadFolder + " Directory Created");
            }
        }
        File dirPhotosFolder = new File(photosUploadFolder);
        if (!dirPhotosFolder.exists()) {
            if (dirPhotosFolder.mkdirs()) {
                Log.i(TAG, photosUploadFolder + " Directory Created");
            }
        }
        File dirAudioFolder = new File(audioUploadFolder);
        if (!dirAudioFolder.exists()) {
            if (dirAudioFolder.mkdirs()) {
                Log.i(TAG, audioUploadFolder + " Directory Created");
            }
        }

        // Uploading
        device = Device.getInstance(ChatActivity.this);
        Url = Config.BASE_URL + Config.CHAT_FILE_UPLOAD;
        authorizationString = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.AUTHTOKEN, ChatActivity.this);
        uploadNotificationConfig = new UploadNotificationConfig();
        uploadNotificationConfig.setTitleForAllStatuses("File Uploading");
        uploadNotificationConfig.setIconColorForAllStatuses(Color.parseColor("#04abdf"));
        uploadNotificationConfig.setClearOnActionForAllStatuses(true);
        UploadService.UPLOAD_POOL_SIZE = 10;
    }

    @OnClick({R.id.backButton, R.id.attachmentButton, R.id.cameraButton, R.id.sendButton, R.id.exitRevealDialog, R.id.camera, R.id.document, R.id.location, R.id.consultationLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.camera:
                ChatActivityPermissionsDispatcher.onPickPhotoWithCheck(ChatActivity.this);
                openBottomSheetMenu();
                break;

            case R.id.document:
                ChatActivityPermissionsDispatcher.onPickDocWithCheck(ChatActivity.this);
                openBottomSheetMenu();
                break;

            case R.id.location:

                Intent locationPicker = new Intent(ChatActivity.this, LocationPickerActivity.class);
                startActivityForResult(locationPicker, LocationPickerActivity.LOCATION_REQUEST);

                openBottomSheetMenu();
                break;

            case R.id.exitRevealDialog:
                if (mPressed)
                    openBottomSheetMenu();
                break;
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.attachmentButton:
                openBottomSheetMenu();
                break;
            case R.id.cameraButton:
                ChatActivityPermissionsDispatcher.onPickPhotoWithCheck(ChatActivity.this);
                break;
            case R.id.consultationLayout:
                if (chatList.getSalutation() != 0)
                    patientName = RescribeConstants.SALUTATION[chatList.getSalutation() - 1] + toCamelCase(chatList.getPatientName());
                else patientName = toCamelCase(chatList.getPatientName());

                Bundle b = new Bundle();
                b.putString(RescribeConstants.PATIENT_NAME, patientName);
                b.putString(RescribeConstants.PATIENT_INFO, mPatientsDetails);
                b.putString(RescribeConstants.PATIENT_ID, String.valueOf(chatList.getId()));
                b.putString(RescribeConstants.PATIENT_HOS_PAT_ID, String.valueOf(mHospitalPatId));
                b.putInt(RescribeConstants.CLINIC_ID, mClinicId);
                Intent intent = new Intent(this, PatientHistoryActivity.class);
                intent.putExtra(RescribeConstants.PATIENT_INFO, b);
                startActivity(intent);
                break;
            case R.id.sendButton:
                // SendButton
                String message = messageType.getText().toString();
                message = message.trim();
                if (!message.equals("")) {

                    MQTTMessage messageL = new MQTTMessage();
                    messageL.setTopic(TOPIC[MESSAGE_TOPIC]);
                    messageL.setMsg(message);

                    String generatedId = docId + "_" + mqttMessages.size() + System.nanoTime();

                    messageL.setMsgId(generatedId);

                    messageL.setDocId(Integer.parseInt(docId));
                    messageL.setPatId(chatList.getId());
                    messageL.setSenderName(docName);
                    messageL.setOnlineStatus(RescribeConstants.USER_STATUS.ONLINE);
                    messageL.setSenderImgUrl(imageUrl);

                    messageL.setSalutation(chatList.getSalutation());
                    messageL.setReceiverName(chatList.getPatientName());
                    messageL.setReceiverImgUrl(chatList.getImageUrl());

                    messageL.setSpecialization(speciality);
                    messageL.setPaidStatus(FREE);

                    messageL.setSender(DOCTOR);

                    messageL.setFileUrl("");
                    messageL.setFileType("");
                    messageL.setMsgStatus(SENT);

                    // 2017-10-13 13:08:07
                    String msgTime = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN);
                    messageL.setMsgTime(msgTime);

                    // send msg by mqtt

                    if (chatAdapter != null) {
                        mqttService.passMessage(messageL);
                        messageType.setText("");
                        mqttMessages.add(messageL);
                        chatAdapter.notifyItemInserted(mqttMessages.size() - 1);
                        chatRecyclerView.smoothScrollToPosition(mqttMessages.size() - 1);
                    }
                }
                break;
        }
    }

    private void openBottomSheetMenu() {

        if (!mPressed) {
            mPressed = true;

            int cx = (mRevealView.getLeft() + mRevealView.getRight());
            int cy = mRevealView.getBottom();
            int endradius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());
            mAnimator = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, endradius);
            mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimator.setDuration(300);

            if (hidden) {
                mRevealView.setVisibility(View.VISIBLE);
                exitRevealDialog.setVisibility(View.VISIBLE);
                mAnimator.start();
                hidden = false;
            }
        } else {
            if (mAnimator != null && !mAnimator.isRunning()) {
                mAnimator = mAnimator.reverse();
                mAnimator.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        mRevealView.setVisibility(View.INVISIBLE);
                        exitRevealDialog.setVisibility(View.GONE);
                        hidden = true;
                        mPressed = false;
                    }

                    @Override
                    public void onAnimationCancel() {
                    }

                    @Override
                    public void onAnimationRepeat() {
                    }
                });
                mAnimator.start();
            }
        }
    }

    // File Selecting

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPickPhoto() {
        FilePickerBuilder.getInstance().setMaxCount(MAX_ATTACHMENT_COUNT)
                .setSelectedFiles(new ArrayList<String>())
                .setActivityTheme(R.style.AppTheme)
                .enableVideoPicker(false)
                .enableCameraSupport(true)
                .openCameraDirect(false)
                .enableCameraMultiplePhotos(false)
                .showGifs(false)
                .showFolderView(true)
                .enableOrientation(true)
                .pickPhoto(this);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPickDoc() {
        String[] documents = {".doc", ".docx", ".odt", ".pdf", ".xls", ".xlsx", ".ods", ".ppt", ".pptx"};
        FilePickerBuilder.getInstance().setMaxCount(MAX_ATTACHMENT_COUNT)
                .setSelectedFiles(new ArrayList<String>())
                .setActivityTheme(R.style.AppTheme)
                .addFileSupport(documents)
                .enableDocSupport(false)
                .enableOrientation(true)
                .pickFile(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChatActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
                if (!data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).isEmpty()) {
                    uploadPhotos(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                }
            } else if (requestCode == FilePickerConst.REQUEST_CODE_DOC) {
                if (!data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).isEmpty()) {
                    uploadFiles(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS), RescribeConstants.FILE.DOC);
                }
            } else if (requestCode == LocationPickerActivity.LOCATION_REQUEST) {
                LatLng latLng = data.getParcelableExtra(LocationPickerActivity.LAT_LONG);
                sendLocation(latLng.latitude + ", " + latLng.longitude);
            }
        }
    }

    private void sendLocation(String latlong) {

        MQTTMessage messageL = new MQTTMessage();
        messageL.setTopic(TOPIC[MESSAGE_TOPIC]);
        messageL.setMsg("");
        messageL.setFileUrl(latlong);

        String generatedId = docId + "_" + mqttMessages.size() + System.nanoTime();

        messageL.setMsgId(generatedId);
        messageL.setDocId(Integer.parseInt(docId));
        messageL.setPatId(chatList.getId());

        messageL.setSenderName(docName);
        messageL.setOnlineStatus(ONLINE);
        messageL.setSenderImgUrl(imageUrl);

        messageL.setSalutation(chatList.getSalutation());
        messageL.setReceiverName(chatList.getPatientName());
        messageL.setReceiverImgUrl(chatList.getImageUrl());

        messageL.setFileType(RescribeConstants.FILE.LOC);
        messageL.setSpecialization("");
        messageL.setPaidStatus(FREE);
        messageL.setUploadStatus(COMPLETED);
        messageL.setMsgStatus(SENT);

        messageL.setSender(DOCTOR);

        // 2017-10-13 13:08:07
        String msgTime = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN);
        messageL.setMsgTime(msgTime);

        // send msg by mqtt
        if (chatAdapter != null) {
            mqttService.passMessage(messageL);
            mqttMessages.add(messageL);
            chatAdapter.notifyItemInserted(mqttMessages.size() - 1);
            chatRecyclerView.smoothScrollToPosition(mqttMessages.size() - 1);
        }
    }

    private void uploadFiles(ArrayList<String> files, String fileType) {
        int startPosition = mqttMessages.size() + 1;
        for (String file : files) {

            String fileForUpload = copyFile(CommonMethods.getFilePath(file), CommonMethods.getFileNameFromPath(file), filesUploadFolder);

            MQTTMessage messageL = new MQTTMessage();
            messageL.setTopic(TOPIC[MESSAGE_TOPIC]);

            String fileName = fileForUpload.substring(fileForUpload.lastIndexOf("/") + 1);

            messageL.setMsg(fileName);

            String generatedId = docId + "_" + mqttMessages.size() + System.nanoTime();

            messageL.setMsgId(generatedId);

            messageL.setFileUrl(fileForUpload);
            messageL.setFileType(fileType);

            messageL.setDocId(Integer.parseInt(docId));
            messageL.setPatId(chatList.getId());
            messageL.setSenderName(docName);
            messageL.setOnlineStatus(RescribeConstants.USER_STATUS.ONLINE);
            messageL.setSenderImgUrl(imageUrl);

            messageL.setSalutation(chatList.getSalutation());
            messageL.setReceiverName(chatList.getPatientName());
            messageL.setReceiverImgUrl(chatList.getImageUrl());

            messageL.setSpecialization(speciality);
            messageL.setPaidStatus(FREE);

            messageL.setMsgStatus(PENDING);

            messageL.setSender(DOCTOR);

            // 2017-10-13 13:08:07
            String msgTime = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN);
            messageL.setMsgTime(msgTime);

            mqttMessages.add(messageL);

            uploadFile(messageL);
        }

        if (chatAdapter != null) {
            chatAdapter.notifyItemRangeInserted(startPosition, files.size());
            chatRecyclerView.smoothScrollToPosition(mqttMessages.size() - 1);
        }
    }

    private void uploadPhotos(ArrayList<String> files) {
        int startPosition = mqttMessages.size() + 1;
        for (String file : files) {

            String fileForUpload = copyFile(CommonMethods.getFilePath(file), CommonMethods.getFileNameFromPath(file), photosUploadFolder);

            MQTTMessage messageL = new MQTTMessage();
            messageL.setTopic(TOPIC[MESSAGE_TOPIC]);
            messageL.setMsg("");

            String generatedId = docId + "_" + mqttMessages.size() + System.nanoTime();

            messageL.setMsgId(generatedId);
            messageL.setDocId(Integer.parseInt(docId));
            messageL.setPatId(chatList.getId());
            messageL.setSenderName(docName);
            messageL.setOnlineStatus(RescribeConstants.USER_STATUS.ONLINE);
            messageL.setSenderImgUrl(imageUrl);

            messageL.setSalutation(chatList.getSalutation());
            messageL.setReceiverName(chatList.getPatientName());
            messageL.setReceiverImgUrl(chatList.getImageUrl());

            messageL.setSpecialization(speciality);
            messageL.setPaidStatus(FREE);

            messageL.setFileUrl(fileForUpload);
            messageL.setFileType(IMG);

            messageL.setSender(DOCTOR);
            messageL.setUploadStatus(UPLOADING);
            messageL.setMsgStatus(PENDING);

            // 2017-10-13 13:08:07
            String msgTime = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN);
            messageL.setMsgTime(msgTime);

            mqttMessages.add(messageL);

            uploadFile(messageL);
        }

        if (chatAdapter != null) {
            chatAdapter.notifyItemRangeInserted(startPosition, files.size());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatRecyclerView.smoothScrollToPosition(mqttMessages.size() - 1);
                    CommonMethods.Log(TAG, "Scrolled");
                }
            }, 100);
        }
    }

    // End File Selecting

    boolean mBounded;
    MQTTService mqttService;

    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            mqttService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
//            Toast.makeText(ChatActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            MQTTService.LocalBinder mLocalBinder = (MQTTService.LocalBinder) service;
            mqttService = mLocalBinder.getServerInstance();

            // set Current Chat User
            mqttService.setCurrentChatUser(chatList.getId()); // Change

            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setMsgId("all");
            statusInfo.setDocId(Integer.parseInt(docId));
            statusInfo.setPatId(chatList.getId());
            statusInfo.setSender(PATIENT);
            statusInfo.setMessageStatus(SEEN);

            mqttService.messageStatus(statusInfo);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, MQTTService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }

        if (mRecorder != null) {
            try {
                mRecorder.release();
            } catch (Exception e) {
                // ignore
            }
            mRecorder = null;
        }

        if (mPlayer != null) {
            try {
                mPlayer.release();
            } catch (Exception e) {
                // ignore
            }
            mPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MessageNotification.cancel(this, chatList.getId());
        appDBHelper.markAsAReadChatMessageByPatientId(chatList.getId());

        broadcastReceiver.register(this);
        registerReceiver(receiver, new IntentFilter(
                MQTTService.NOTIFY));

        registerReceiver(receiver, new IntentFilter(
                ACTION_DOWNLOAD_COMPLETE));

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Type status
        // reset the timeout
        timeoutHandler.removeCallbacks(typingTimeout);
        isTyping = false;
        typingStatus();
        // Type status End

        broadcastReceiver.unregister(this);
        unregisterReceiver(receiver);
        if (mqttService != null)
            mqttService.setCurrentChatUser(0);
    }

    // Uploading

    @Override
    public void uploadFile(MQTTMessage mqttMessage) {

        try {

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(ChatActivity.this, String.valueOf(mqttMessage.getMsgId()), Url)
                    .setNotificationConfig(uploadNotificationConfig)
                    .setMaxRetries(RescribeConstants.MAX_RETRIES)

                    .addHeader(RescribeConstants.AUTHORIZATION_TOKEN, authorizationString)
                    .addHeader(RescribeConstants.DEVICEID, device.getDeviceId())
                    .addHeader(RescribeConstants.OS, device.getOS())
                    .addHeader(RescribeConstants.OSVERSION, device.getOSVersion())
                    .addHeader(RescribeConstants.DEVICE_TYPE, device.getDeviceType())

                    .addFileToUpload(mqttMessage.getFileUrl(), "chatDoc");

            uploadRequest.startUpload();

        } catch (FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        } finally {
            mqttMessage.setMsgStatus(PENDING);
            mqttMessage.setUploadStatus(UPLOADING);
            appDBHelper.insertChatMessage(mqttMessage);
        }
    }

    // Download File

    @Override
    public long downloadFile(MQTTMessage mqttMessage) {
        long downloadReference;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mqttMessage.getFileUrl()));
        //Setting title of request
        request.setTitle(mqttMessage.getMsg());
        //Setting description of request
        request.setDescription("Rescribe File Downloading");
        request.allowScanningByMediaScanner();
        //Set the local destination for the downloaded file to a path
        //within the application's external files directory
        request.setDestinationInExternalPublicDir(RESCRIBE_FILES, CommonMethods.getFileNameFromPath(mqttMessage.getFileUrl()));
        // Keep notification after complete
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

    @Override
    public void openFile(MQTTMessage message, ImageView senderFileIcon) {

        Uri uriTemp = Uri.parse(message.getFileUrl());

        if (message.getFileType().equals(DOC)) {

            File file;
            if (uriTemp.toString().contains("file://"))
                file = new File(uriTemp.getPath());
            else file = new File(uriTemp.toString());

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".droidninja.filepicker.provider", file);
            } else {
                uri = Uri.fromFile(createImageFile(uriTemp));
            }

            // Check what kind of file you are trying to open, by comparing the uri with extensions.
            // When the if condition is matched, plugin sets the correct intent (mime) type,
            // so Android knew what application to use to open the file
            if (uri.toString().contains(".doc") || uri.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (uri.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (uri.toString().contains(".ppt") || uri.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (uri.toString().contains(".xls") || uri.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (uri.toString().contains(".zip") || uri.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (uri.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (uri.toString().contains(".wav") || uri.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (uri.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (uri.toString().contains(".jpg") || uri.toString().contains(".jpeg") || uri.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (uri.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (uri.toString().contains(".3gp") || uri.toString().contains(".mpg") || uri.toString().contains(".mpeg") || uri.toString().contains(".mpe") || uri.toString().contains(".mp4") || uri.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                //if you want you can also define the intent type for any other file

                //additionally use else clause below, to manage other unknown extensions
                //in this case, Android will show all applications installed on the device
                //so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                CommonMethods.showToast(ChatActivity.this, getResources().getString(R.string.doc_viewer_not_found));
            }
        } else if (message.getFileType().equals(AUD)) {

            if (this.audioIcon != null) {
                this.audioIcon.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                senderFileIcon.setImageResource(R.drawable.ic_stop_white_24dp);
            } else {
                senderFileIcon.setImageResource(R.drawable.ic_stop_white_24dp);
            }

            this.audioIcon = senderFileIcon;

            if (!isPlaying)
                startPlaying(message.getFileUrl());
            else {
                stopPlaying();
                startPlaying(message.getFileUrl());
            }
        }
    }

    private File createImageFile(Uri uriTemp) {
        return new File(filesFolder, CommonMethods.getFileNameFromPath(uriTemp.toString()));
    }

    // Broadcast

    private UploadServiceBroadcastReceiver broadcastReceiver = new UploadServiceBroadcastReceiver() {
        @Override
        public void onProgress(Context context, UploadInfo uploadInfo) {
        }

        @Override
        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

            CommonMethods.Log(TAG, "FailedUpload");

            String prefix[] = uploadInfo.getUploadId().split("_");
            if (prefix[0].equals(docId)) {
                appDBHelper.updateMessageUploadStatus(uploadInfo.getUploadId(), FAILED);
                int position = getPositionById(uploadInfo.getUploadId());
                mqttMessages.get(position).setUploadStatus(FAILED);
                chatAdapter.notifyItemChanged(position);
            }

        }

        private int getPositionById(String id) {
            int pos = 0;
            for (int position = mqttMessages.size() - 1; position >= 0; position--) {
                if (id.equals(mqttMessages.get(position).getMsgId())) {
                    pos = position;
                    break;
                }
            }
            return pos;
        }

        @Override
        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

            CommonMethods.Log(TAG, "onCompleted " + serverResponse.getBodyAsString());

            String prefix[] = uploadInfo.getUploadId().split("_");
            if (prefix[0].equals(docId)) {
                int position = getPositionById(uploadInfo.getUploadId());
                mqttMessages.get(position).setUploadStatus(COMPLETED);
                chatAdapter.notifyItemChanged(position);
            }
        }

        @Override
        public void onCancelled(Context context, UploadInfo uploadInfo) {
        }
    };

    private String copyFile(String inputPath, String inputFile, String outputPath) {
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file (You have now copied the file)
            out.flush();
            out.close();

        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        return outputPath + inputFile;
    }

}