package smartshoppinglist.at.smartshoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;

public class AddItemPopUp extends Activity {
    Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_popup);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int hight = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.8),(int)(hight*0.15));

        Bundle extra = getIntent().getExtras();
        if (extra != null){
            item = new Gson().fromJson(extra.getString("item"), Item.class);
            TextView name = findViewById(R.id.add_item_popup_name);
            name.setText(item.getName());
            final EditText count = findViewById(R.id.add_item_popup_count);
            count.setText("1");
            final EditText unit = findViewById(R.id.add_item_popup_unit);
            unit.setText(item.getDefaultUnit());
            Button add = findViewById(R.id.add_item_popup_add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                        ItemContainer itemContainer = new ItemContainer(item, Integer.parseInt(count.getText().toString()),unit.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra ("itemContainer", new Gson().toJson(itemContainer));
                        setResult(RESULT_OK, intent);
                        finish();
                    }catch (Exception e){}
                }
            });
        }

    }
}
