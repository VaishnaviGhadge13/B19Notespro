package easy.tuto.notespro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    private Context context;
    private List<Note> notes = new ArrayList<>(); // List to hold the notes data

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }


   @Override
   protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
       holder.titleTextView.setText(note.getTitle());
       holder.contentTextView.setText(note.getContent());
       holder.timestampTextView.setText(Utility.timestampToString(note.getTimestamp()));

       // Set background color if it's not null
      String backgroundColor = note.getBackgroundColor();
       if (backgroundColor != null) {
           int backgroundColorInt = Color.parseColor(backgroundColor);
           holder.itemView.setBackgroundColor(backgroundColorInt);
       }
       else
       {
           holder.itemView.setBackgroundColor(0);
       }

       String textStyle = note.getTextstyle();
       if (textStyle != null) {
           Typeface typeface = Typeface.DEFAULT;
           switch (textStyle) {
               case "Bold":
                   typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
                   holder.titleTextView.setTypeface(typeface);
                   holder.contentTextView.setTypeface(typeface);
                   holder.timestampTextView.setTypeface(typeface);
                   break;
               case "Italic":
                   typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
                   holder.titleTextView.setTypeface(typeface);
                   holder.contentTextView.setTypeface(typeface);
                   holder.timestampTextView.setTypeface(typeface);
                   break;
               case "Underline":
                   holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                   holder.contentTextView.setPaintFlags(holder.titleTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                   holder.timestampTextView.setPaintFlags(holder.titleTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                   break;
               // Add more cases as needed
               default:
                   // Handle the default case
                   break;
           }

       }

       // Set font color
       String fontcolor=note.getFontColor();
       if(fontcolor!=null)
       {
           int fontColorInt = Color.parseColor(fontcolor);
           holder.titleTextView.setTextColor(fontColorInt);
           holder.contentTextView.setTextColor(fontColorInt);
           holder.timestampTextView.setTextColor(fontColorInt);
       }


       // Set font size
       String fontSizeString = note.getFontSize();
       if (fontSizeString != null) {
           try {
               float fontSize = Float.parseFloat(fontSizeString);
               holder.titleTextView.setTextSize(fontSize);
               holder.contentTextView.setTextSize(fontSize);
               holder.timestampTextView.setTextSize(fontSize);
           } catch (NumberFormatException e) {
               // Handle the case where the string cannot be parsed as a float
               e.printStackTrace(); // Or log the error
           }
       } else {
           // Handle the case where the font size string is null
      }
       holder.itemView.setOnClickListener(v -> {
           float x=12;
           Intent intent = new Intent(context, NoteDetailsActivity.class);
           intent.putExtra("title", note.getTitle());
           intent.putExtra("content", note.getContent());
           intent.putExtra("fontcolor", fontcolor);
           intent.putExtra("fontsize", fontSizeString);
           intent.putExtra("background", backgroundColor);
           intent.putExtra("textstyle",textStyle);
           String docId = this.getSnapshots().getSnapshot(position).getId();
           intent.putExtra("docId", docId);
           context.startActivity(intent);
       });
   }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView, timestampTextView,item_view;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title_text_view);
            contentTextView = itemView.findViewById(R.id.note_content_text_view);
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view);
          /*  backgroundTextView=itemView.findViewById(R.id.note_background_color_view);
            fontcolorTextView=itemView.findViewById(R.id.note_font_color_view);
            fontsizeTextView=itemView.findViewById(R.id.note_font_size_view);*/

        }
    }

    // Method to update the dataset with new data
    public void updateData(List<Note> newData) {
        // Clear the existing data
        notes.clear();
        // Add the new data
        notes.addAll(newData);
        // Notify the adapter that the dataset has changed
        notifyDataSetChanged();
    }
}
