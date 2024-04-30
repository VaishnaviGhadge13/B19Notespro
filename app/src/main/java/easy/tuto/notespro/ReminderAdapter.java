package easy.tuto.notespro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    private Context mContext;
    private List<Reminder> mReminderList;

    public ReminderAdapter(Context context, List<Reminder> reminderList) {
        super(context, 0, reminderList);
        mContext = context;
        mReminderList = reminderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }

        final Reminder currentReminder = mReminderList.get(position);

        TextView titleTextView = listItem.findViewById(R.id.text_title);
        titleTextView.setText(currentReminder.getTitle());

        // Set click listener on titleTextView
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddReminderActivity and pass reminder title as extra
                Intent intent = new Intent(mContext, AddReminderActivity.class);
                intent.putExtra("reminder_title", currentReminder.getTitle());
                mContext.startActivity(intent);
            }
        });

        TextView dateTextView = listItem.findViewById(R.id.text_date);
        dateTextView.setText(currentReminder.getDate());

        TextView timeTextView = listItem.findViewById(R.id.text_time);
        timeTextView.setText(currentReminder.getTime());

        return listItem;
    }


}
