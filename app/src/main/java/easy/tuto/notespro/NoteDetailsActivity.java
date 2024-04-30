package easy.tuto.notespro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    Spinner fontSizeSpinner, fontColorSpinner, backgroundColorSpinner;
    TextView pageTitleTextView, deleteNoteTextViewBtn;
    ImageButton saveNoteBtn, menuBtn,imageButton;
    String title, content, docId;
    String noteIdp;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        menuBtn = findViewById(R.id.menu_btn);
        titleEditText = findViewById(R.id.notes_tittle_text);
        imageButton=findViewById(R.id.imageButton);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);

        setupFontSizeSpinner();
        setupFontColorSpinner();
        setupBackgroundColorSpinner();


        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");
        /*imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NoteDetailsActivity.this,MainActivity2.class);
                intent.putExtra("NOTE_ID", docId);

                startActivity(intent);
                //startActivity(intent);
            }
        });*/
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteDetailsActivity.this, MainActivity2.class);
                intent.putExtra("NOTE_ID", docId);
                startActivity(intent);
                finish(); // Finish NoteDetailsActivity to prevent returning to it
            }
        });
        if (docId != null) {
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if (isEditMode) {
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        menuBtn.setOnClickListener(v -> toggleSpinnersVisibility());
        saveNoteBtn.setOnClickListener(v -> saveNote());
        deleteNoteTextViewBtn.setOnClickListener(v -> deleteNoteFromFirebase());
    }

    private void toggleSpinnersVisibility() {
        int visibility = fontSizeSpinner.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE;
        fontSizeSpinner.setVisibility(visibility);
        fontColorSpinner.setVisibility(visibility);
        backgroundColorSpinner.setVisibility(visibility);
    }

    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if (noteTitle.isEmpty()) {
            titleEditText.setError("Title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now()); // Setting current date and time

        saveNoteToFirebase(note); // Call method to save note to Firebase
    }


    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;

        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utility.showToast(NoteDetailsActivity.this, "Note " + (isEditMode ? "updated" : "added") + " successfully");
                finish();
            } else {
                Utility.showToast(NoteDetailsActivity.this, "Failed while " + (isEditMode ? "updating" : "adding") + " note");
            }
        });
    }

    void deleteNoteFromFirebase() {
        if (isEditMode) {
            Utility.getCollectionReferenceForNotes().document(docId).delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Utility.showToast(NoteDetailsActivity.this, "Note deleted successfully");
                    finish();
                } else {
                    Utility.showToast(NoteDetailsActivity.this, "Failed while deleting note");
                }
            });
        }
    }

    private void setupFontSizeSpinner() {
        fontSizeSpinner = findViewById(R.id.font_size_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.font_size_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fontSizeSpinner.setAdapter(adapter);
        fontSizeSpinner.setSelection(0, false);

        fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedFontSize = (String) parent.getItemAtPosition(position);
                    float fontSize = Float.parseFloat(selectedFontSize.split("\\(")[1].split("\\)")[0]);
                    contentEditText.setTextSize(fontSize);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupFontColorSpinner() {
        fontColorSpinner = findViewById(R.id.font_color_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.font_color_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fontColorSpinner.setAdapter(adapter);
        fontColorSpinner.setSelection(0, false);

        fontColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedFontColor = (String) parent.getItemAtPosition(position);
                    int fontColor = Color.parseColor(selectedFontColor);
                    contentEditText.setTextColor(fontColor);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupBackgroundColorSpinner() {
        backgroundColorSpinner = findViewById(R.id.background_color_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.background_color_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        backgroundColorSpinner.setAdapter(adapter);
        backgroundColorSpinner.setSelection(0, false);

        backgroundColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedBackgroundColor = (String) parent.getItemAtPosition(position);
                    int backgroundColor = Color.parseColor(selectedBackgroundColor);
                    contentEditText.setBackgroundColor(backgroundColor);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
}
//notedetailsbedoreupdatingtime.................