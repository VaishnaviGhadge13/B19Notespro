package easy.tuto.notespro;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;

import easy.tuto.notespro.data.AlarmReminderContract;
import easy.tuto.notespro.data.FirestoreHelper;

public class MainActivity2 extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton mAddReminderButton;
    private AlarmCursorAdapter mCursorAdapter;
    private FirestoreHelper firestoreHelper;
    private ListView reminderListView;
    private ProgressDialog prgDialog;
    private TextView reminderText;
    private Toolbar mToolbar;
    private String alarmTitle = "";

    private static final int VEHICLE_LOADER = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        Intent intent = getIntent();
        String noteId = intent.getStringExtra("NOTE_ID");

        firestoreHelper = new FirestoreHelper(noteId);
        reminderListView = findViewById(R.id.list);
        mCursorAdapter = new AlarmCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);
        View emptyView = findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);
        reminderText = findViewById(R.id.reminderText);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Reminder clickedReminder = (Reminder) adapterView.getItemAtPosition(position);
                if (clickedReminder != null) {
                    String reminderId = clickedReminder.getId(); // Get the ID of the clicked reminder
                    String reminderTitle = clickedReminder.getTitle();

                    // Pass both reminder ID and title to the AddReminderActivity
                    Intent intent = new Intent(MainActivity2.this, AddReminderActivity.class);
                    intent.putExtra("NOTE_ID", noteId);
                    intent.putExtra("REMINDER_ID", reminderId); // Pass reminder ID
                    intent.putExtra("REMINDER_TITLE", reminderTitle);
                    startActivity(intent);
                }
            }
        });



        mAddReminderButton = findViewById(R.id.fab);
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReminderTitle(noteId);
            }
        });

        fetchRemindersFromFirestore(noteId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE
        };

        return new CursorLoader(this,   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            mCursorAdapter.swapCursor(cursor);
            reminderText.setVisibility(cursor.getCount() > 0 ? View.VISIBLE : View.INVISIBLE);
        } else {
            // Handle case where cursor is null
            // For example, display an error message or take appropriate action
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void fetchRemindersFromFirestore(String noteId) {
        ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "Fetching reminders...");
        firestoreHelper.getReminders(noteId, new FirestoreHelper.ReminderFetchListener() {
            @Override
            public void onReminderFetchSuccess(List<Reminder> reminders) {
                progressDialog.dismiss();
                populateListView(reminders);
            }

            @Override
            public void onReminderFetchFailure(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity2.this, "Failed to fetch reminders: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateListView(List<Reminder> reminders) {
        ListView listView = findViewById(R.id.list);
        ReminderAdapter adapter = new ReminderAdapter(MainActivity2.this, reminders);
        listView.setAdapter(adapter);
    }

    public void addReminderTitle(final String noteId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Reminder Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = input.getText().toString().trim();
                if (!title.isEmpty()) {
                    ContentValues values = new ContentValues();
                    values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, title);

                    // Generate a unique ID for the reminder
                    String reminderId = UUID.randomUUID().toString();

                    // Call addReminder with noteId, reminderId, and values
                    firestoreHelper.addReminder(noteId, reminderId, values);

                    // Reload the data after successful insertion
                    restartLoader();
                } else {
                    Toast.makeText(MainActivity2.this, "Reminder title cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void restartLoader() {
        getSupportLoaderManager().restartLoader(VEHICLE_LOADER, null, this);
    }
}

/*
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;

import easy.tuto.notespro.data.AlarmReminderContract;
import easy.tuto.notespro.data.FirestoreHelper;

public class MainActivity2 extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton mAddReminderButton;
    AlarmCursorAdapter mCursorAdapter;
    FirestoreHelper firestoreHelper;
    ListView reminderListView;
    ProgressDialog prgDialog;
    TextView reminderText;
    Toolbar mToolbar;
    private String alarmTitle = "";

    private static final int VEHICLE_LOADER = 0;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        Intent intent=getIntent();
        String x=intent.getStringExtra("NOTE_ID");

        firestoreHelper = new FirestoreHelper(x);
        reminderListView = findViewById(R.id.list);
        mCursorAdapter = new AlarmCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);
        View emptyView = findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);
        reminderText = findViewById(R.id.reminderText);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Reminder clickedReminder = (Reminder) adapterView.getItemAtPosition(position);
                if (clickedReminder != null) {
                    String reminderTitle = clickedReminder.getTitle();
                    Intent intent = new Intent(MainActivity2.this, AddReminderActivity.class);
                    intent.putExtra("NOTE_ID",x);
                    intent.putExtra("REMINDER_TITLE", reminderTitle);
                    startActivity(intent);
                }

            }
        });

        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab);
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                String xyz=intent.getStringExtra("NOTE_ID");

               addReminderTitle(x);
                //Intent i=new Intent(MainActivity2.this,AddReminderActivity.class);
                //i.putExtra("NOTE_ID",xyz);
                //startActivity(i);

            }
        });

        Intent intent1=getIntent();
        String x1=intent1.getStringExtra("NOTE_ID");
        fetchRemindersFromFirestore(x1);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            mCursorAdapter.swapCursor(cursor);
            if (cursor.getCount() > 0) {
                reminderText.setVisibility(View.VISIBLE);
            } else {
                reminderText.setVisibility(View.INVISIBLE);
            }
        } else {
            // Handle case where cursor is null
            // For example, display an error message or take appropriate action
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

    private void fetchRemindersFromFirestore(String noteId) {
        ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "Fetching reminders...");
        firestoreHelper.getReminders(noteId, new FirestoreHelper.ReminderFetchListener() {
            @Override
            public void onReminderFetchSuccess(List<Reminder> reminders) {
                progressDialog.dismiss();
                populateListView(reminders);
            }

            @Override
            public void onReminderFetchFailure(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity2.this, "Failed to fetch reminders: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateListView(List<Reminder> reminders) {
        ListView listView = findViewById(R.id.list);
        ReminderAdapter adapter = new ReminderAdapter(MainActivity2.this, reminders);
        listView.setAdapter(adapter);
    }

    public void addReminderTitle(final String noteId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Reminder Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty()) {
                    return;
                }
                alarmTitle = input.getText().toString();

                ContentValues values = new ContentValues();
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, alarmTitle);

                // Generate a unique ID for the reminder
                String reminderId = UUID.randomUUID().toString();

                // Call addReminder with noteId, reminderId, and values
                firestoreHelper.addReminder(noteId, reminderId, values);

                // Reload the data after successful insertion
                restartLoader();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public void restartLoader(){
        getSupportLoaderManager().restartLoader(VEHICLE_LOADER, null, this);
    }
}*/

