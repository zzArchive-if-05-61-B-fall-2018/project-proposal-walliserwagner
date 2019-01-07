package smartshoppinglist.at.smartshoppinglist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class CreateItemPopUp extends Activity{
    Item item;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_item_popup);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int hight = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (hight * 0.22));

        final AutoCompleteTextView category = (AutoCompleteTextView) findViewById(R.id.autocomplete_category);


        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String itemName;
            itemName = new Gson().fromJson(extra.getString("name"), String.class);
            String[] categories = new Gson().fromJson(extra.getString("categories"), String[].class);

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
            category.setAdapter(adapter);

            item = new Item(itemName);
            EditText name = findViewById(R.id.create_item_popup_name);
            name.setText(item.getName());
            final EditText count = findViewById(R.id.create_item_popup_count);
            count.setText("1");
            final EditText unit = findViewById(R.id.create_item_popup_unit);
            unit.setText(item.getDefaultUnit());
            category.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event){
                    category.showDropDown();
                    return false;
                }
            });
            Button add = findViewById(R.id.create_item_popup_add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        item.setCategory(category.getText().toString());
                        ItemContainer itemContainer = new ItemContainer(item, Integer.parseInt(count.getText().toString()), unit.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra("itemContainer", new Gson().toJson(itemContainer));
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (Exception e) {
                    }
                }
            });
        }
    }
}
