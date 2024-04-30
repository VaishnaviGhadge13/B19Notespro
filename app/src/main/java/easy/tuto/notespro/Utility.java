/*package easy.tuto.notespro;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {

    static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionReferenceForNotes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my_notes");
    }

    static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }

}*/
/*package easy.tuto.notespro;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {

    static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionReferenceForNotes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return FirebaseFirestore.getInstance().collection("notes")
                    .document(currentUser.getUid()).collection("my_notes");
        } else {
            // Handle the case where the current user is null
            return null;
        }
    }

    static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }
}*/

        package easy.tuto.notespro;

        import android.content.Context;
        import android.widget.Toast;

        import com.google.firebase.Timestamp;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.FirebaseFirestore;

        import java.text.SimpleDateFormat;
        import java.util.Locale;
        import java.util.TimeZone;



public class Utility {

    static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionReferenceForNotes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Get the current user's email ID
            String userEmail = currentUser.getEmail();

            // Replace '.' with '_' in the email ID to avoid Firestore restrictions
            String sanitizedEmail = userEmail.replace(".", "_");

            // Create a reference to the subcollection with the user's email ID
            return FirebaseFirestore.getInstance()
                    .collection("notes")
                    .document(sanitizedEmail)
                    .collection("my_notes");
        } else {
            // Handle the case where the current user is null
            return null;
        }
    }


    /*static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());*/


    /* static String timestampToString(Timestamp timestamp) {
         SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());
         sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Set the timezone to India
         return sdf.format(timestamp.toDate());
     }*/
    static String timestampToString(Timestamp timestamp) {
        if (timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Set India timezone
            return sdf.format(timestamp.toDate());
        } else {
            return ""; // or handle the null case as per your requirement
        }
    }

}


//These utility methods can be helpful for displaying messages to users, working with Firestore collections, and formatting timestamps for display.
//These utility methods can be helpful for displaying messages to users, working with Firestore collections, and formatting timestamps for display.
