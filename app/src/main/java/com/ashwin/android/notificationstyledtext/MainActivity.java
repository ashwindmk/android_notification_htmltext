package com.ashwin.android.notificationstyledtext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.text.HtmlCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 1025;
    private static final String CHANNEL_ID = "my_channel";
    private static final String CHANNEL_NAME = "My Channel";
    private static final String CHANNEL_DESCRIPTION = "This is a default channel";

    private static final String title = "Colored Notification";
    private static final String text = "This is collapsed content-text";

    private static final String html = "<span style=\"color: #C20000;\"> This is in red </span> expanded <b> Big Text Message </b>"
            + "<br/>"
            + "<i>Thanks for expanding the notification!</i>";

    private Spanned bigText = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView sampleTextView = (TextView) findViewById(R.id.sample_textview);
        sampleTextView.setText(bigText);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void notify(View view) {
        createNotificationChannel();

        boolean useCompat = false;

        if (useCompat) {
            notifyCompat(title, text, bigText);
        } else {
            notifyNative(title, text, bigText);
        }
    }

    private void notifyNative(CharSequence title, CharSequence text, CharSequence bigText) {
        final int icon = R.mipmap.ic_launcher;

        Notification.Builder builder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        builder.setSmallIcon(icon);
        builder.setContentTitle(title);
        builder.setContentText(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setStyle(new Notification.BigTextStyle().bigText(bigText));
        }

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }

        manager.notify(NOTIFICATION_ID, notification);
    }

    private void notifyCompat(CharSequence title, CharSequence text, CharSequence bigText) {
        final int icon = R.mipmap.ic_launcher;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(icon);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(bigText));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
