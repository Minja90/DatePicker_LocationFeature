package com.etf.telekomunikacije.datepicker;

//importing necessary packages
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Random;


// Creating Alarm Receiver, who will listen upcoming events
public class AlarmReceiver extends BroadcastReceiver {

    //Creating method on receive. When intnet is received on front screen we should see notification
    @Override
    public void onReceive(Context context, Intent intent) {

        //variable values from three edit text fields
        String name = intent.getStringExtra(MainActivity.EVENT_NAME);
        String description = intent.getStringExtra(MainActivity.EVENT_DESCRIPTION);
        String location = intent.getStringExtra(MainActivity.EVENT_LOCATION);


        //setig up RingtonManger who will notify user about received notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //defining get direction part in notification
        String addrNew = location;
        String url = "http://maps.google.com/maps?daddr="+addrNew;
        Intent getDirection = new Intent(Intent.ACTION_VIEW,  Uri.parse(url));
        getDirection.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, getDirection, 0);
        String directionName = context.getString(R.string.getDirection);

        //Handling the notifications, and attributes which will be displayed on front screen
        int color=context.getResources().getColor(R.color.LightSeaGreen);

        if (location.trim().length() == 0 ) {

            String detail = description;

            //Handling the notification for Smart Watch
            NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender();
            extender.setBackground(BitmapFactory.decodeResource(context.getResources(), (R.drawable.screen)));

            //Handling notification for phone
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle(name)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(detail))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.slika3)
                    .setSound(soundUri)
                    .setColor(color)
                    .extend(extender);

            NotificationManager nm = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Random random = new Random();

            nm.notify(random.nextInt(), builder.build());

        }
            else
        {
            String detail = location + '\n' + description;

            NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender();
            extender.setBackground(BitmapFactory.decodeResource(context.getResources(), (R.drawable.screen)));

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle(name)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(detail))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.slika3)
                    .setSound(soundUri)
                    .setColor(color)
                    .extend(extender)
                    .addAction(R.drawable.notification_location_icon, directionName, pIntent);

            NotificationManager nm = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Random random = new Random();

            nm.notify(random.nextInt(), builder.build());

        }

    }

}
