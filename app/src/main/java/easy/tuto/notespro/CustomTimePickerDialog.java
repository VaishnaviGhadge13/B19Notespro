package easy.tuto.notespro;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

public class CustomTimePickerDialog extends TimePickerDialog {

    public CustomTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set your custom theme here
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // If you want to set a custom style, you can use setTheme()
        // setTheme(android.R.style.Theme_Material_Light_Dialog_Alert);
    }
}
