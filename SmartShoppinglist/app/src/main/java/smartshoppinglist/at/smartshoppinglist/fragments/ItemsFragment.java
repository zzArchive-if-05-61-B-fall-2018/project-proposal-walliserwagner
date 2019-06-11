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
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import smartshoppinglist.at.smartshoppinglist.InputValidator;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemCategory;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;
import smartshoppinglist.at.smartshoppinglist.uiadapters.DragableListViewAdapter;
import smartshoppinglist.at.smartshoppinglist.uiadapters.DragableListView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment {

    private List<ItemCategory> categoryList;
    private Context mContext;
    private AlertDialog createItemDialog;
    private AlertDialog dialog;
    private DragableListViewAdapter adapter;

    public ItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        View v = inflater.inflate(R.layout.fragment_items, container, false);
        categoryList = MainActivity.getInstance().getItemCategorys().getCategories();
        final DragableListView listView = (DragableListView) v.findViewById(R.id.itemsList);
        adapter = new DragableListViewAdapter(getContext(), categoryList, new DragableListViewAdapter.Listener() {
            @Override
            public void onGrab(int position, RelativeLayout row) {
                listView.onGrab(position, row);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryFragment f = new CategoryFragment();
                f.setItemCategory(categoryList.get(position));
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container, f,"category");
                fragmentTransaction.addToBackStack("category");
                fragmentTransaction.commit();
            }
        });
        listView.setAdapter(adapter);
        listView.setListener(new DragableListView.Listener() {
            @Override
            public void swapElements(int indexOne, int indexTwo) {
                int priority = categoryList.get(indexOne).getPriority();
                categoryList.get(indexOne).setPriority(categoryList.get(indexTwo).getPriority());
                categoryList.get(indexTwo).setPriority(priority);
                ItemCategory temp = categoryList.get(indexOne);
                categoryList.set(indexOne, categoryList.get(indexTwo));
                categoryList.set(indexTwo, temp);
                MainActivity.getInstance().getItemCategorys().sort();
            }
        });
        registerForContextMenu(listView);
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()) {
            case R.id.items_longClick_alter:
                if(categoryList.get(index).isDefaultCategory()){
                    Toast.makeText(getActivity().getApplicationContext(),R.string.this_category_con_not_be_altered,Toast.LENGTH_SHORT).show();
                }
                else{
                    alterCategory(categoryList.get(index));
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void alterCategory(ItemCategory itemCategory){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.simple_add_popup, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popup);
        final EditText name = popup.findViewById(R.id.simple_add_popup_name);
        name.setHint(itemCategory.getName());
        Button button = popup.findViewById(R.id.simple_add_popup_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!InputValidator.validInputEmptyString(name.getText().toString(),20)) {
                        name.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    else if(!name.getText().toString().equals("")){
                        itemCategory.setName(name.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }catch (Exception e) {
                }
            }
        });
        dialog = alertDialogBuilder.create();
        dialog.show();
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
        category.setHint(Item.getDefaultCategory());
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
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                }
            }
        });
        createItemDialog = alertDialogBuilder.create();
        createItemDialog.show();
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
}
