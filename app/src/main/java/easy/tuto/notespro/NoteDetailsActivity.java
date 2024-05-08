package easy.tuto.notespro;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    Spinner fontSizeSpinner, fontColorSpinner, backgroundColorSpinner,textStyleSpinner;
    TextView pageTitleTextView, deleteNoteTextViewBtn;
    ImageButton saveNoteBtn, menuBtn,imageButton;
    String title, content, docId;
    String noteIdp;
   // int bgcolor=-1,ftcolor=-1;
    String bgcolor,ftcolor,textStyle;
    String ftsize;
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
        setupTextstyle();

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");
        ftcolor=getIntent().getStringExtra("fontcolor");
        ftsize=getIntent().getStringExtra("fontsize");
        bgcolor=getIntent().getStringExtra("background");
        textStyle=getIntent().getStringExtra("textstyle");
        if(ftsize!=null)
        {
            Toast.makeText(NoteDetailsActivity.this,"",Toast.LENGTH_SHORT).show();
        }
        else
        {
            ftsize="12";
        }
        if(ftcolor==null)
        {
            Toast.makeText(NoteDetailsActivity.this,"",Toast.LENGTH_SHORT).show();
        }
        else
        {
           // ftcolor="Black";
            contentEditText.setTextColor(Color.parseColor(ftcolor));
           //titleEditText.setTextColor(Color.parseColor(ftcolor));

        }
        if(bgcolor==null)
        {
            Toast.makeText(NoteDetailsActivity.this,"",Toast.LENGTH_SHORT).show();
        }
        else
        {
            contentEditText.setBackgroundColor(Color.parseColor(bgcolor));
             //bgcolor="White";

        }
        if(textStyle==null)
        {
            Toast.makeText(NoteDetailsActivity.this,"",Toast.LENGTH_SHORT).show();
        }
        else
        {
                Typeface typeface = Typeface.DEFAULT;
                switch (textStyle) {
                    case "Bold":
                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
                        contentEditText.setTypeface(typeface);
                        break;
                    case "Italic":
                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
                        contentEditText.setTypeface(typeface);
                        break;
                    case "Underline":
                        contentEditText.setPaintFlags(contentEditText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        contentEditText.setPaintFlags(contentEditText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        contentEditText.setPaintFlags(contentEditText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        break;
                    // Add more cases as needed
                    default:
                        contentEditText.setTypeface(typeface);
                        // Handle the default case
                        break;
                }



        }

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

        //menuBtn.setOnClickListener(v -> toggleSpinnersVisibility());
        // Java
        ImageButton menuBtn = findViewById(R.id.menu_btn);
        RelativeLayout optionsLayout = findViewById(R.id.options_layout);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionsLayout.getVisibility() == View.VISIBLE) {
                    optionsLayout.setVisibility(View.INVISIBLE);

                } else {
                    optionsLayout.setVisibility(View.VISIBLE);
                    int visibility = fontSizeSpinner.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE;
                    fontSizeSpinner.setVisibility(visibility);
                    fontColorSpinner.setVisibility(visibility);
                    backgroundColorSpinner.setVisibility(visibility);
                    textStyleSpinner.setVisibility(visibility);
                }
            }
        });

        saveNoteBtn.setOnClickListener(v -> saveNote());
        deleteNoteTextViewBtn.setOnClickListener(v -> deleteNoteFromFirebase());
    }


    private void applyTextStyle(String textStyle1) {
        switch (textStyle1) {
            case "Bold":
                // Apply bold style to your TextView or EditText
                titleEditText.setTypeface(null, Typeface.BOLD); // For EditText
                // Or
                contentEditText.setTypeface(null, Typeface.BOLD); // For TextView
                textStyle="Bold";
                break;
            case "Italic":
                // Apply italic style to your TextView or EditText
                titleEditText.setTypeface(null, Typeface.ITALIC); // For EditText
                // Or
                contentEditText.setTypeface(null, Typeface.ITALIC); // For TextView
                textStyle="Italic";
                break;
            case "Underline":
                // Apply underline style to your TextView or EditText
                // You can use a SpannableStringBuilder to achieve underline effect
                SpannableString content = new SpannableString(titleEditText.getText().toString());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                titleEditText.setText(content); // For EditText
                // Or
                contentEditText.setText(content);
                textStyle="Underline";
                break;
            default:
                // Handle default case
                break;
        }
    }


    /*private void toggleSpinnersVisibility() {
        int visibility = fontSizeSpinner.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE;
        fontSizeSpinner.setVisibility(visibility);
        fontColorSpinner.setVisibility(visibility);
        backgroundColorSpinner.setVisibility(visibility);
    }*/

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
        note.setTimestamp(Timestamp.now());
       // if(bgc)
        note.setBackgroundColor(bgcolor);
        note.setFontColor(ftcolor);
        note.setFontSize(ftsize);
        note.setTextstyle(textStyle);
        // Setting current date and time

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
                Intent intent=new Intent(NoteDetailsActivity.this,MainActivity.class);
                intent.putExtra("fontcolor", ftcolor);
                intent.putExtra("fontsize", ftsize);
                intent.putExtra("background", bgcolor);
                intent.putExtra("textstyle",textStyle);
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
                    CharSequence selectedFontSize = (CharSequence) parent.getItemAtPosition(position);
                    if (selectedFontSize instanceof Spanned) {
                        Spanned spannedText = (Spanned) selectedFontSize;
                        String fontSizeString = spannedText.toString(); // Convert to String
                        // Extract the font size from the string
                        String[] parts = fontSizeString.split("\\(");
                        if (parts.length > 1) {
                            String fontSizeValue = parts[1].replaceAll("\\D+","");
                            int fontSize = Integer.parseInt(fontSizeValue);
                            contentEditText.setTextSize(fontSize);
                            ftsize = fontSizeValue; // Assign the font size as a string
                        }
                    }
                }
            }



            /*public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedFontSize = (String) parent.getItemAtPosition(position);
                   // float fontSize = Float.parseFloat(selectedFontSize.split("\\(")[1].split("\\)")[0]);
                    contentEditText.setTextSize(Integer.parseInt(selectedFontSize));
                    ftsize=selectedFontSize;
                }
            }*/

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
    private void setupTextstyle() {
        textStyleSpinner = findViewById(R.id.text_style_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.text_style_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textStyleSpinner.setAdapter(adapter);
        textStyleSpinner.setSelection(0, false);

        textStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected text style
                String selectedTextStyle = (String) parent.getItemAtPosition(position);
                // Apply the selected text style to your TextView or EditText
                applyTextStyle(selectedTextStyle);
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
                    //int fontColor = Color.parseColor(selectedFontColor);
                    //contentEditText.setTextColor(fontColor);
                    contentEditText.setTextColor(Color.parseColor(selectedFontColor));
                   // titleEditText.setTextColor(Color.parseColor(selectedFontColor));
                    //ftcolor=fontColor;
                    ftcolor=selectedFontColor;
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
                   // int backgroundColor = Color.parseColor(selectedBackgroundColor);
                    contentEditText.setBackgroundColor(Color.parseColor(selectedBackgroundColor));
                   // bgcolor=backgroundColor;
                    bgcolor=selectedBackgroundColor;
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