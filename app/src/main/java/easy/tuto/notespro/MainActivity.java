package easy.tuto.notespro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private LinearLayout searchLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton addNoteBtn;
    private ImageButton menuBtn;
    private ImageButton searchIcon; // Added ImageButton for search icon
    private NoteAdapter noteAdapter;

    private boolean isSearchMode = false; // Flag to track search mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchLayout = findViewById(R.id.searchLayout);
        recyclerView = findViewById(R.id.recyclerView);
        addNoteBtn = findViewById(R.id.add_note_btn);
        menuBtn = findViewById(R.id.menu_btn);
        searchIcon = findViewById(R.id.search_icon); // Initialize search icon

        // Set onClickListener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the search query from the searchEditText
                String query = searchEditText.getText().toString().trim();
                // Check if the query is empty
                if (!query.isEmpty()) {
                    // Call a method to search notes with the given query
                    searchNotes(query);
                } else {
                    // Show a message indicating that a title is required
                    searchEditText.setError("Title is required for searching");
                }
            }
        });

        // Set onClickListener for the menu button
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the menu options
                showMenu(v);
            }
        });

        // Set onClickListener for the add note button
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the activity to add a new note
                startActivity(new Intent(MainActivity.this,
                        NoteDetailsActivity.class));
            }
        });

        // Set onClickListener for the search icon
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSearchMode) {
                    enterSearchMode();
                } else {
                    exitSearchMode();
                }
            }
        });

        // Setup RecyclerView
        setupRecyclerView();
    }

    // Method to toggle the visibility of the search layout
    private void toggleSearchLayoutVisibility() {
        if (searchLayout.getVisibility() == View.VISIBLE) {
            searchLayout.setVisibility(View.GONE);
        } else {
            searchLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
        popupMenu.getMenu().add("Logout");
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Logout")) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);
    }

    // Method to search notes with the given query
    // Method to search notes with the given query
    // Method to search notes with the given query
    // Method to search notes with the given query
   private void searchNotes(String query) {
        // Perform search operation based on the query
        // For example, update the Firestore query to fetch notes with titles containing the query
        Query searchQuery = Utility.getCollectionReferenceForNotes()
                .orderBy("title")
                .startAt(query.toLowerCase())
                .endAt(query.toLowerCase() + "\uf8ff");

        searchQuery.get().addOnSuccessListener(documentSnapshots -> {
            List<Note> searchResults = new ArrayList<>();
            for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                // Check if the searched note exists
                if (document.exists()) {
                    // Convert the document snapshot to a Note object
                    Note note = document.toObject(Note.class);
                    searchResults.add(note);
                }
            }
            // Update the RecyclerView adapter with the search results
            noteAdapter.updateData(searchResults);

            if (searchResults.isEmpty()) {
                // If no matching note is found, show a message indicating that the note does not exist
                // You can display a Toast message or set an error on the searchEditText
                searchEditText.setError("Note not found");
            }
        }).addOnFailureListener(e -> {
            // Handle any potential errors
            e.printStackTrace();
        });
    }
    // Method to search notes with the given query
   /* private void searchNotes(String query) {
        // Perform search operation based on the query
        // For example, update the Firestore query to fetch notes with titles containing the query
        Query searchQuery = Utility.getCollectionReferenceForNotes()
                .orderBy("title")
                .startAt(query.toLowerCase())
                .endAt(query.toLowerCase() + "\uf8ff");
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(searchQuery, Note.class)
                .build();
        noteAdapter.updateOptions(options);
    }
    private void searchNotes(String query) {
        // Perform search operation based on the query
        // For example, update the Firestore query to fetch notes with titles containing the query
        Query searchQuery = Utility.getCollectionReferenceForNotes()
                .orderBy("title")
                .startAt(query.toLowerCase())
                .endAt(query.toLowerCase() + "\uf8ff");

        searchQuery.get().addOnSuccessListener(documentSnapshots -> {
            List<Note> searchResults = new ArrayList<>();
            for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                // Check if the searched note exists
                if (document.exists()) {
                    // Convert the document snapshot to a Note object
                    Note note = document.toObject(Note.class);
                    searchResults.add(note);
                }
            }
            // Update the RecyclerView adapter with the search results
            noteAdapter.updateData(searchResults);

            if (searchResults.isEmpty()) {
                // If no matching note is found, show a message indicating that the note does not exist
                // You can display a Toast message or set an error on the searchEditText
                searchEditText.setError("Note not found");
            }
        }).addOnFailureListener(e -> {
            // Handle any potential errors
            e.printStackTrace();
        });
    }

*/






    // Method to enter search mode
    private void enterSearchMode() {
        searchLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        isSearchMode = true;
    }

    // Method to exit search mode
    private void exitSearchMode() {
        searchLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        searchEditText.setText(""); // Clear search query
        isSearchMode = false;
    }

    // Override onBackPressed to handle back button press
    @Override
    public void onBackPressed() {
        if (isSearchMode) {
            exitSearchMode();
        } else {
            super.onBackPressed();
        }
    }

    // onStart and onStop methods for FirestoreRecyclerAdapter lifecycle
    @Override
    protected void onStart() {
        super.onStart();
        if (noteAdapter != null) {
            noteAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }
}
