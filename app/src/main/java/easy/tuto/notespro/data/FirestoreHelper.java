/*package easy.tuto.notespro.data;

import android.content.ContentValues;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import easy.tuto.notespro.Reminder;


public class FirestoreHelper {

    private FirebaseFirestore db;
    private CollectionReference remindersCollection;

    public interface OnCompleteListener {
        void onComplete(boolean success);
    }
    public void loadReminderData(String reminderId, final LoadReminderDataListener listener) {
        // Get the reference to the reminder document
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reminders")
                .document(reminderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Reminder document exists, retrieve data
                        Map<String, Object> reminderData = documentSnapshot.getData();

                        // Assuming you have a Reminder class with appropriate fields
                        Reminder reminder = new Reminder();
                        reminder.setId(reminderId);
                        reminder.setTitle((String) reminderData.get("title"));
                        reminder.setDate((String) reminderData.get("date"));
                        reminder.setTime((String) reminderData.get("time"));
                        reminder.setRepeatNo((String) reminderData.get("repeatNo"));
                        reminder.setRepeatType((String) reminderData.get("repeatType"));
                        reminder.setActive((String) reminderData.get("active"));

                        // Pass the loaded reminder to the listener
                        listener.onLoadReminderDataSuccess(reminder);
                    } else {
                        // Reminder document does not exist
                        listener.onLoadReminderDataFailure("Reminder not found");
                    }
                })
                .addOnFailureListener(e -> {
                    // Failed to retrieve reminder data
                    listener.onLoadReminderDataFailure(e.getMessage());
                });
    }

    public interface LoadReminderDataListener {
        void onLoadReminderDataSuccess(Reminder reminder);
        void onLoadReminderDataFailure(String errorMessage);
    }
    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        remindersCollection = db.collection("reminders");
    }
    public void deleteReminder(String reminderId, OnCompleteListener listener) {
        // Get the document reference for the reminder
        DocumentReference reminderRef = remindersCollection.document(reminderId);

        // Delete the reminder document
        reminderRef.delete()
                .addOnCompleteListener((com.google.android.gms.tasks.OnCompleteListener<Void>) listener);
    }

    public void saveReminder(Reminder reminder, OnCompleteListener listener) {
        // Add the reminder to the Firestore collection
        remindersCollection.add(reminder)
                .addOnCompleteListener((com.google.android.gms.tasks.OnCompleteListener<DocumentReference>) listener);
    }



    public void addReminder(final String noteId, ContentValues values) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            db.collection("reminders")
                    .document(currentUser.getEmail())
                    .collection("user_notes")
                    .document(noteId)
                    .collection("reminders")
                    .add(values)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Handle success
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                        }
                    });
        } else {
            // Handle the case where the current user is null
        }
    }

    public void getReminders(final String noteId, final ReminderFetchListener listener) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            db.collection("reminders")
                    .document(currentUser.getEmail())
                    .collection("user_notes")
                    .document(noteId)
                    .collection("reminders")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            List<Reminder> reminders = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                Reminder reminder = documentSnapshot.toObject(Reminder.class);
                                reminders.add(reminder);
                            }
                            listener.onReminderFetchSuccess(reminders);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onReminderFetchFailure(e.getMessage());
                        }
                    });
        } else {
            // Handle the case where the current user is null
            listener.onReminderFetchFailure("User is not logged in.");
        }
    }

    public interface ReminderFetchListener {
        void onReminderFetchSuccess(List<Reminder> reminders);
        void onReminderFetchFailure(String errorMessage);
    }
}*/
package easy.tuto.notespro.data;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import easy.tuto.notespro.Reminder;

public class FirestoreHelper {

    private FirebaseFirestore db;
    private CollectionReference path;
    private CollectionReference remindersCollection;

    public interface OnCompleteListener {
        void onComplete(boolean success);
    }

    public interface LoadReminderDataListener {
        void onLoadReminderDataSuccess(Reminder reminder);

        void onLoadReminderDataFailure(String errorMessage);
    }

    public interface ReminderFetchListener {
        void onReminderFetchSuccess(List<Reminder> reminders);

        void onReminderFetchFailure(String errorMessage);
    }

    /*public FirestoreHelper(String noteId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        String userEmail = currentUser.getEmail();

        // Replace '.' with '_' in the email ID to avoid Firestore restrictions
        String sanitizedEmail = userEmail.replace(".", "_");
        remindersCollection = db.collection("notes").document(sanitizedEmail).collection("reminders").document(noteId).collection("Reminders of Note");
    }*/
    public FirestoreHelper(String noteId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            db = FirebaseFirestore.getInstance();
            String userEmail = currentUser.getEmail();

            // Replace '.' with '_' in the email ID to avoid Firestore restrictions
            String sanitizedEmail = userEmail.replace(".", "_");

            // Reference to the user's document using the sanitized email
            DocumentReference userDocument = db.collection("notes").document(sanitizedEmail);
            CollectionReference userNotesCollection = userDocument.collection("reminders");
            DocumentReference noteDocument = userNotesCollection.document(noteId);

            remindersCollection = noteDocument.collection("Reminders of Note");
            path=remindersCollection;

        } else {
            // Handle the case where the current user is null
        }
    }

    public void loadReminderData(String noteId, String reminderId, final LoadReminderDataListener listener) {
        // Get reference to the document containing reminder data under the specified note
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            // Replace '.' with '_' in the email ID to avoid Firestore restrictions
            String sanitizedEmail = userEmail.replace(".", "_");
            DocumentReference docRef = db.collection("notes").document(sanitizedEmail)
                    .collection("reminders").document(noteId).collection("Reminders of Note").document(reminderId);

            // Fetch the document
            docRef.get().addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Document found, parse reminder data
                            Reminder reminder = document.toObject(Reminder.class);
                            if (reminder != null) {
                                listener.onLoadReminderDataSuccess(reminder);
                            } else {
                                listener.onLoadReminderDataFailure("Failed to parse reminder data");
                            }
                        } else {
                            // Document not found
                            Log.d(TAG, "Document not found for reminderId: " + reminderId);
                            listener.onLoadReminderDataFailure("Document not found for reminderId: " + reminderId);
                        }
                    } else {
                        // Failed to fetch document
                        Log.e(TAG, "Failed to fetch document for reminderId: " + reminderId, task.getException());
                        listener.onLoadReminderDataFailure("Failed to fetch document for reminderId: " + reminderId);
                    }
                }
            });
        } else {
            // Handle the case where the current user is null
        }
    }





    // Interface to listen for load reminder data events


    public void saveReminder(Reminder reminder, OnCompleteListener listener) {
        // Add the reminder to the Firestore collection
        //remindersCollection.add(reminder)
        path.add(reminder)
                .addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
    }

    public void addReminder(final String noteId, final String reminderId, ContentValues values) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Construct the document reference for the reminder under the specified note
            DocumentReference reminderRef = db.collection("notes").document(getSanitizedEmail(currentUser.getEmail()))
                    .collection("reminders").document(noteId).collection("Reminders of Note").document(reminderId);

            // Set the reminder data under the specified document reference
            reminderRef.set(values)
                    .addOnSuccessListener(aVoid -> {
                        // Handle success
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        } else {
            // Handle the case where the current user is null
        }
    }

    private String getSanitizedEmail(String email) {
        // Replace '.' with '_' in the email ID to avoid Firestore restrictions
        return email.replace(".", "_");
    }



    public void getReminders(final String noteId, final ReminderFetchListener listener) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            db.collection("notes").document(getSanitizedEmail(currentUser.getEmail()))
                    .collection("reminders").document(noteId).collection("Reminders of Note")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        // Check if any documents exist in the query snapshot
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Iterate over each document in the snapshot
                            List<Reminder> reminders = new ArrayList<>();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Retrieve data from each document
                                Map<String, Object> reminderData = document.getData();

                                // Convert reminderData to Reminder object
                                Reminder reminder = new Reminder();
                                // Assuming you have appropriate setters in Reminder class
                                reminder.setTitle((String) reminderData.get("title"));
                                reminder.setDate((String) reminderData.get("date"));
                                // Add other fields

                                reminders.add(reminder);
                            }

                            // Pass the loaded reminders to the listener
                            listener.onReminderFetchSuccess(reminders);
                        } else {
                            // No reminders found
                            listener.onReminderFetchFailure("No reminders found");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Failed to fetch reminders
                        listener.onReminderFetchFailure(e.getMessage());
                    });
        } else {
            // Handle the case where the current user is null
            listener.onReminderFetchFailure("User is not logged in.");
        }
    }


    public void deleteReminder(String noteId,String reminderId, final OnCompleteListener listener) {
        // Get reference to the document containing the reminder to delete
        DocumentReference docRef = db.collection("reminders").document(noteId).collection("Reminders of Note").document(reminderId);

        // Delete the document
        docRef.delete().addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Reminder deleted successfully
                    listener.onComplete(true);
                } else {
                    // Failed to delete reminder
                    listener.onComplete(false);
                }
            }
        });
    }

}


