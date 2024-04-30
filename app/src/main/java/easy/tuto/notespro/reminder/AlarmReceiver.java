package easy.tuto.notespro.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if the received intent has the ALARM_ACTION action
        if (intent != null && intent.getAction() != null && intent.getAction().equals("ALARM_ACTION")) {
            // Extract reminder title from the intent extras
            String reminderTitle = intent.getStringExtra("title");

            // Handle the alarm action, for example, display a notification
            // This is where you would implement the behavior you want when the alarm triggers
        }
    }
}
