package easy.tuto.notespro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import easy.tuto.notespro.data.AlarmReminderContract;

public class AlarmCursorAdapter extends CursorAdapter {

    private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;
    private ImageView mActiveImage , mThumbnailImage;
    private List<Reminder> reminderList;

    public AlarmCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        reminderList = new ArrayList<>();
    }

    // Method to add a new reminder to both cursor and list
    public void addReminder(Reminder reminder) {
        // Add reminder to the list
        reminderList.add(reminder);
        // Notify adapter that data has changed
        notifyDataSetChanged();
    }

    /*public AlarmCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 );
    }*/

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.alarm_items, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mTitleText = view.findViewById(R.id.recycle_title);
        mDateAndTimeText = view.findViewById(R.id.recycle_date_time);
        mRepeatInfoText = view.findViewById(R.id.recycle_repeat_info);
        mActiveImage = view.findViewById(R.id.active_image);
        mThumbnailImage = view.findViewById(R.id.thumbnail_image);

        // Your existing code to retrieve data from the cursor...

        // Set reminder title
        // Set reminder title
        // Get the index of the KEY_TITLE column
        int titleIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
        if (titleIndex != -1) {
            // The column exists, so retrieve the title
            String title = cursor.getString(titleIndex);
            // Set the title to the appropriate view
            setReminderTitle(title);
        } else {
            // Handle the case where the column doesn't exist
            // For example, log an error message or set a default value for the title
        }


        // Set reminder date and time
        @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_DATE));
        @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TIME));
        if (date != null && time != null) {
            String dateTime = date + " " + time;
            setReminderDateTime(dateTime);
        } else {
            mDateAndTimeText.setText("Date not set");
        }

        // Set reminder repeat info
        @SuppressLint("Range") String repeat = cursor.getString(cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT));
        @SuppressLint("Range") String repeatNo = cursor.getString(cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO));
        @SuppressLint("Range") String repeatType = cursor.getString(cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE));
        setReminderRepeatInfo(repeat, repeatNo, repeatType);

        // Set active image
        @SuppressLint("Range") String active = cursor.getString(cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE));
        setActiveImage(active);
    }

    // Set reminder title view
    private void setReminderTitle(String title) {
        mTitleText.setText(title);
        String letter = "A";
        if (title != null && !title.isEmpty()) {
            letter = title.substring(0, 1);
        }
        Bitmap bitmap = createCircularIcon(letter, Color.BLUE);
        mThumbnailImage.setImageBitmap(bitmap);
    }

    // Set date and time views
    private void setReminderDateTime(String datetime) {
        mDateAndTimeText.setText(datetime);
    }

    // Set repeat views
    private void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType) {
        if ("true".equals(repeat)) {
            mRepeatInfoText.setText("Every " + repeatNo + " " + repeatType + "(s)");
        } else {
            mRepeatInfoText.setText("Repeat Off");
        }
    }

    // Set active image as on or off
    private void setActiveImage(String active) {
        if ("true".equals(active)) {
            mActiveImage.setImageResource(R.drawable.ic_notifications_on_white_24dp);
        } else {
            mActiveImage.setImageResource(R.drawable.ic_notifications_off_grey600_24dp);
        }
    }

    // Create circular icon with text
    private Bitmap createCircularIcon(String text, int color) {
        int size = 100; // Size of the circular icon
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40); // Text size
        paint.setTextAlign(Paint.Align.CENTER);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        float x = size / 2f;
        float y = (size - bounds.height()) / 2f - bounds.top;
        canvas.drawText(text, x, y, paint);
        return bitmap;
    }

}
