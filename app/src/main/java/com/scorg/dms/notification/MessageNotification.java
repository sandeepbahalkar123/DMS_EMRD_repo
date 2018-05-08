package com.scorg.dms.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;

import com.scorg.dms.R;
import com.scorg.dms.model.chat.MQTTMessage;
import com.scorg.dms.services.MQTTService;
import com.scorg.dms.ui.activities.ChatActivity;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;

import static com.scorg.dms.broadcast_receivers.ReplayBroadcastReceiver.MESSAGE_LIST;
import static com.scorg.dms.services.MQTTService.REPLY_ACTION;
import static com.scorg.dms.util.DMSConstants.FILE.AUD;
import static com.scorg.dms.util.DMSConstants.FILE.DOC;
import static com.scorg.dms.util.DMSConstants.FILE.IMG;
import static com.scorg.dms.util.DMSConstants.FILE.LOC;
import static com.scorg.dms.util.DMSConstants.FILE.VID;
import static com.scorg.dms.util.DMSConstants.SALUTATION;

/**
 * Helper class for showing and canceling new message
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class MessageNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "RescribeMessage";
    private static final String GROUP = "RescribeMessages";

    public static void notify(final Context context, final ArrayList<MQTTMessage> messageContent,
                              final String userName, Bitmap picture, final int unread, PendingIntent replyPendingIntent, final int notificationId) {

        MQTTMessage lastMessage = messageContent.get(messageContent.size() - 1);
        String content = getContent(lastMessage);

        String salutation = "";
        if (lastMessage.getSalutation() != 0)
            salutation = SALUTATION[lastMessage.getSalutation() - 1];

        String title;
        if (unread > 1)
            title = salutation + userName + " (" + unread + " messages)";
        else title = salutation + userName;

        // start your activity for Android M and below
        Intent resultIntent = new Intent(context, ChatActivity.class);
        resultIntent.setAction(REPLY_ACTION);
        resultIntent.putExtra(MESSAGE_LIST, lastMessage);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        lastMessage.getDocId(),
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                .setBigContentTitle(title)
                .setSummaryText("New Message");

        for (MQTTMessage message : messageContent)
            inboxStyle.addLine(getContent(message));

// Create the RemoteInput specifying above key
        RemoteInput remoteInput = new RemoteInput.Builder(MQTTService.KEY_REPLY)
                .setLabel("Reply")
                .build();

        // Add to your action, enabling Direct Reply
        NotificationCompat.Action mAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_action_stat_reply, "Reply", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .setAllowGeneratedReplies(true)
                        .build();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)

                // Bundle Notification

                .setGroupSummary(true)
                .setGroup(GROUP)

                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.logosmall)
                .setContentTitle(title)
                .setContentText(content)

                // Set Color
                .setColor(ContextCompat.getColor(context, R.color.tagColor))

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                // Provide a large icon, shown with the notification in the
                // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(picture)

                // Set ticker text (preview) information for this notification.
                .setTicker(title)

                // Show a number. This is useful when stacking notifications of
                // a single type.
                .setNumber(unread)

                // Set Style and Action
                .setStyle(inboxStyle)
                .addAction(mAction)

                // Click Event on notification
                .setContentIntent(resultPendingIntent)

                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        notify(context, builder.build(), notificationId);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification, int notificationId) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_TAG, notificationId, notification);
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context, int notificationId) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_TAG, notificationId);
    }

    private static String getContent(MQTTMessage mqttMessage) {
        String content;

        if (mqttMessage.getFileType() != null) {
            switch (mqttMessage.getFileType()) {
                case DOC:
                    content = DMSConstants.FILE_EMOJI.DOC_FILE;
                    break;
                case AUD:
                    content = DMSConstants.FILE_EMOJI.AUD_FILE;
                    break;
                case VID:
                    content = DMSConstants.FILE_EMOJI.VID_FILE;
                    break;
                case LOC:
                    content = DMSConstants.FILE_EMOJI.LOC_FILE;
                    break;
                case IMG:
                    content = DMSConstants.FILE_EMOJI.IMG_FILE;
                    break;
                default:
                    content = mqttMessage.getMsg();
                    break;
            }
        } else content = mqttMessage.getMsg();

        return content;
    }
}
