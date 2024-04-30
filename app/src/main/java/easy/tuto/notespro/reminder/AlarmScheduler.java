package easy.tuto.notespro.reminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmScheduler {

    @SuppressLint("ScheduleExactAlarm")
    public static void setAlarm(Context context, long timeInMillis, String reminderTitle) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("ALARM_ACTION"); // Set the action for the intent
        intent.putExtra("title", reminderTitle);

        // Add FLAG_IMMUTABLE for all Android versions
        int flags = PendingIntent.FLAG_IMMUTABLE;

        // Add FLAG_MUTABLE for Android S+ (version 31 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags = PendingIntent.FLAG_MUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flags);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    public static void cancelAlarm(Context context, String reminderTitle) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("ALARM_ACTION"); // Set the action for the intent

        // Add FLAG_IMMUTABLE for Android S+
        int flags = PendingIntent.FLAG_IMMUTABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_MUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flags);

        alarmManager.cancel(pendingIntent);
    }

}
/*
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmScheduler {

    public static void setAlarm(Context context, long timeInMillis, String reminderTitle) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("ALARM_ACTION"); // Set the action for the intent
        intent.putExtra("title", reminderTitle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }


    public static void cancelAlarm(Context context, String reminderTitle) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("ALARM_ACTION"); // Set the action for the intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

}*/