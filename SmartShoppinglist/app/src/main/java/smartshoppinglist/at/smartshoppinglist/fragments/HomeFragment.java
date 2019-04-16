package smartshoppinglist.at.smartshoppinglist.fragments;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import smartshoppinglist.at.smartshoppinglist.InputValidator;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.uiadapters.ShoppingListExpandableAdapter;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.objects.Category;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private ShoppingListExpandableAdapter listAdapter;
    private ExpandableListView expListView;
    private Shoppinglist shoppinglist;
    private AlertDialog alterItemDialog;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.remove_items_menu, menu);
        listAdapter.notifyDataSetChanged();
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.remove_items){
            removeTickedItemsFromListDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        shoppinglist = ((MainActivity)getActivity()).getShoppinglist();

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        expListView = (ExpandableListView) v.findViewById(R.id.listView);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        listAdapter = new ShoppingListExpandableAdapter(getActivity().getApplicationContext(), shoppinglist,expListView);
        expListView.setAdapter(listAdapter);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(shoppinglist.getName());
        registerForContextMenu(expListView);
        expandListView();
        return v;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
        {
            menu.setHeaderTitle(R.string.options);
            ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.shoppinglist_long_click_menu, menu);
        }

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();

        int groupPos = 0, childPos = 0;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
        {
            groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
            childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
        }
        switch (item.getItemId()) {
            case R.id.shoppinglist_longClick_alter:
                ItemContainer itemContainer = shoppinglist.getItemByPos(groupPos,childPos);
                alterItem(itemContainer);
                listAdapter.notifyDataSetChanged();
                return true;
            case R.id.shoppinglist_longClick_remove:
                shoppinglist.removeItem(shoppinglist.getItemByPos(groupPos,childPos));
                listAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void searchItem(Application application) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new SearchFragment(),"search");
        fragmentTransaction.addToBackStack("search");
        fragmentTransaction.commit();
        listAdapter.notifyDataSetChanged();
    }
    public void removeTickedItemsFromListDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(R.string.really_want_to_delete_all_marked_items);
        dialog.setCancelable(false);

        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shoppinglist.removeTickedItems();
                listAdapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.create().show();
    }
    private void expandListView(){
        Category[] category = shoppinglist.getItems();
        for (int i = 0; i < category.length;i++) {
            if (category[i].isExpanded()){
                expListView.expandGroup(i);
            }
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private void alterItem(ItemContainer itemContainer ){
        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.fragment_search_ll);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View popup = inflater.inflate(R.layout.create_item_popup, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popup);

        final AutoCompleteTextView category =  popup.findViewById(R.id.autocomplete_category);
        String[] categories = (((MainActivity)getActivity()).getItemCategorys().getNames().toArray(new String[0]));
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categories);
        category.setAdapter(adapter);
        category.setHint(itemContainer.getItem().getCategory());
        final EditText name = popup.findViewById(R.id.create_item_popup_name);
        name.setText(itemContainer.getItem().getName());
        final EditText count = popup.findViewById(R.id.create_item_popup_count);
        count.setText( Integer.toString(itemContainer.getCount()));
        final EditText unit = popup.findViewById(R.id.create_item_popup_unit);
        unit.setText(itemContainer.getItem().getDefaultUnit());
        category.setThreshold(1);
        category.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                category.showDropDown();
                return false;
            }
        });
        Button add = popup.findViewById(R.id.create_item_popup_add);
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
                    else if(!InputValidator.validInputNumberString(count.getText().toString(),4)){
                        count.setError(getString(R.string.invalid_input));
                        throw new Exception();

                    } else if (!InputValidator.validInputString(unit.getText().toString(), 4)) {
                        unit.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    String categoryName = category.getText().toString();
                    if(categoryName.equals("")) categoryName = itemContainer.getItem().getCategory();
                    Item item = new Item(name.getText().toString(),categoryName,unit.getText().toString());
                    shoppinglist.removeItem(itemContainer);
                    itemContainer.setItem(item);
                    itemContainer.setCount(Integer.parseInt(count.getText().toString()));
                    itemContainer.setUnit(unit.getText().toString());
                    item = ((MainActivity)getActivity()).getItems().findItemByName(itemContainer.getItem().getName());
                    if(item != null){
                        ((MainActivity)getActivity()).getItems().removeItem(item);
                        item.setCategory(itemContainer.getItem().getCategory());
                        shoppinglist.updateCategoriesedItems();
                        ((MainActivity)getActivity()).getItems().addItem(item);
                    }
                    shoppinglist.addItem(itemContainer);
                    ((MainActivity)getActivity()).getItemCategorys().addCategoryName(itemContainer.getItem().getCategory());
                    alterItemDialog.dismiss();
                    alterItemDialog = null;
                } catch (Exception e) {
                }
            }
        });
        alterItemDialog = alertDialogBuilder.create();
        alterItemDialog.show();
    }
    public void onBackPressed()
    {
        if(alterItemDialog != null){
            alterItemDialog.dismiss();
            alterItemDialog = null;
        }
    }
}
