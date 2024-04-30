package easy.tuto.notespro.data;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AlarmReminderProvider extends ContentProvider {

    private static final int REMINDER = 100;
    private static final int REMINDER_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private FirebaseFirestore db;
    private CollectionReference remindersCollection;

    static {
        sUriMatcher.addURI(AlarmReminderContract.CONTENT_AUTHORITY, AlarmReminderContract.PATH_VEHICLE, REMINDER);
        sUriMatcher.addURI(AlarmReminderContract.CONTENT_AUTHORITY, AlarmReminderContract.PATH_VEHICLE + "/#", REMINDER_ID);
    }

    @Override
    public boolean onCreate() {
        db = FirebaseFirestore.getInstance();
        remindersCollection = db.collection("reminders");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Not implementing this method for Firestore
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Not implementing this method for Firestore
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return insertReminder(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /*private Uri insertReminder(Uri uri, ContentValues values) {
        DocumentReference newReminderRef = remindersCollection.document();
        newReminderRef.set(valuesToMap(values))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Successfully added
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add
                    }
                });

        // Return the URI with the appended ID
        return ContentUris.withAppendedId(uri, Long.parseLong(Objects.requireNonNull(newReminderRef.getId())));
    }*/

    /*@Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Not implementing this method for Firestore
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        // Not implementing this method for Firestore
        return 0;
    }*/
    private Uri insertReminder(Uri uri, ContentValues values) {
        // Create a new document reference within the "reminders" collection
        DocumentReference newReminderRef = remindersCollection.document();

        // Convert ContentValues to a Map
        Map<String, Object> reminderData = valuesToMap(values);

        // Add the document with the generated ID and data
        newReminderRef.set(reminderData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Successfully added
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add
                        Log.e(LOG_TAG, "Failed to insert reminder for " + uri, e);
                    }
                });

        // Return the URI with the appended ID
        // Use the path of the document reference as the ID
        String documentId = newReminderRef.getId();
        Uri newUri = Uri.withAppendedPath(uri, documentId);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                // Not implementing this method for now, you can add Firestore deletion logic here
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                // Not implementing this method for now, you can add Firestore update logic here
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
        return 0;
    }


    // Helper method to convert ContentValues to a Map
    private Map<String, Object> valuesToMap(ContentValues values) {
        Map<String, Object> map = new HashMap<>();
        for (String key : values.keySet()) {
            map.put(key, values.get(key));
        }
        return map;
    }
}

