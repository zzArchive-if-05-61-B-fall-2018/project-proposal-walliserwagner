package smartshoppinglist.at.smartshoppinglist.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import smartshoppinglist.at.smartshoppinglist.InputValidator;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemCategory;
import smartshoppinglist.at.smartshoppinglist.uiadapters.ItemListAdapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CategoryFragment extends Fragment {
    private ListView categoryList;
    private Context mContext;
    private ItemCategory itemCategory;
    private AlertDialog createItemDialog;
    private ArrayAdapter<Item> adapter;
    private List<Item> items;
    public CategoryFragment() {
        // Required empty public constructor
    }

    public void setItemCategory(ItemCategory itemCategory){
        this.itemCategory = itemCategory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category , container, false);
        mContext = getActivity();
        if(itemCategory != null){
            items = MainActivity.getInstance().getItems().getItemsByCategory(itemCategory.getName());
            categoryList = (ListView) v.findViewById(R.id.categoryList);
            adapter = new ItemListAdapter(getActivity(),items);
            categoryList.setAdapter(adapter);
            registerForContextMenu(categoryList);
        }
        return v;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.options);
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.items_long_click_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.items_longClick_alter:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index = info.position;
                alterItem(items.get(index));
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
    public void onBackPressed()
    {
        if(createItemDialog != null){
            createItemDialog.dismiss();
            createItemDialog = null;
        }
        else {
            getFragmentManager().popBackStack();
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    public void createItem(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popup = inflater.inflate(R.layout.create_item_in_itemtab_popup, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popup);

        final AutoCompleteTextView category =  popup.findViewById(R.id.autocomplete_category);
        String[] categories = (((MainActivity)getActivity()).getItemCategorys().getNames().toArray(new String[0]));
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categories);
        category.setAdapter(adapter);
        final Item item = new Item(getString(R.string.item_name));
        category.setHint(itemCategory.getName());
        final EditText name = popup.findViewById(R.id.create_item_in_itemtab_popup_name);
        name.setHint(item.getName());
        final EditText unit = popup.findViewById(R.id.create_item_in_itemtab_popup_unit);
        unit.setText(item.getDefaultUnit());
        category.setThreshold(1);
        category.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                category.showDropDown();
                return false;
            }
        });
        Button add = popup.findViewById(R.id.create_item_in_itemtab_popup_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!InputValidator.validInputString(name.getText().toString(),20)){
                        name.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    else if(!InputValidator.validInputEmptyString(category.getText().toString(),20)){
                        category.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    else if (!InputValidator.validInputString(unit.getText().toString(), 4)) {
                        unit.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    item.setName(name.getText().toString());
                    item.setCategory(category.getText().toString());
                    item.setDefaultunit(unit.getText().toString());
                    ((MainActivity)getActivity()).getItems().addItem(item);
                    ((MainActivity)getActivity()).getItemCategorys().addCategoryName(item.getCategory());
                    getActivity().onBackPressed();

                    getFragmentManager().popBackStack();
                    CategoryFragment f = new CategoryFragment();
                    f.setItemCategory(itemCategory);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, f,"category");
                    fragmentTransaction.addToBackStack("category");
                    fragmentTransaction.commit();
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                }
            }
        });
        createItemDialog = alertDialogBuilder.create();
        createItemDialog.show();
    }
    public void alterItem( Item item){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popup = inflater.inflate(R.layout.create_item_in_itemtab_popup, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popup);

        final AutoCompleteTextView category =  popup.findViewById(R.id.autocomplete_category);
        String[] categories = (((MainActivity)getActivity()).getItemCategorys().getNames().toArray(new String[0]));
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categories);
        category.setAdapter(adapter);
        category.setHint(item.getCategory());
        final EditText name = popup.findViewById(R.id.create_item_in_itemtab_popup_name);
        name.setText(item.getName());
        final EditText unit = popup.findViewById(R.id.create_item_in_itemtab_popup_unit);
        unit.setText(item.getDefaultUnit());
        category.setThreshold(1);
        category.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                category.showDropDown();
                return false;
            }
        });
        Button add = popup.findViewById(R.id.create_item_in_itemtab_popup_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!InputValidator.validInputString(name.getText().toString(),20)){
                        name.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    else if(!InputValidator.validInputEmptyString(category.getText().toString(),20)){
                        category.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    else if (!InputValidator.validInputString(unit.getText().toString(), 4)) {
                        unit.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    ((MainActivity)getActivity()).getItems().removeItem(item);
                    item.setName(name.getText().toString());
                    item.setCategory(category.getText().toString());
                    item.setDefaultunit(unit.getText().toString());
                    ((MainActivity)getActivity()).getItems().addItem(item);
                    ((MainActivity)getActivity()).getItemCategorys().addCategoryName(item.getCategory());
                    getActivity().onBackPressed();

                    getFragmentManager().popBackStack();
                    CategoryFragment f = new CategoryFragment();
                    f.setItemCategory(itemCategory);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, f,"category");
                    fragmentTransaction.addToBackStack("category");
                    fragmentTransaction.commit();
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                }
            }
        });
        createItemDialog = alertDialogBuilder.create();
        createItemDialog.show();
    }



}
