package smartshoppinglist.at.smartshoppinglist.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.InputValidator;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;


import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SearchFragment extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private List<String> itemNames;
    private ItemList items;
    private ArrayAdapter<String> mAdapter;
    private Context mContext;
    private String text;
    private AlertDialog createItemDialog;
    private AlertDialog addItemDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        items = ((MainActivity)getActivity()).getItems();
        itemNames = items.getItemNames();
        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, itemNames);
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        String itemName = (String) listView.getAdapter().getItem(position);
        final Item item = items.findItemByName(itemName);
        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.fragment_search_ll);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popup = inflater.inflate(R.layout.add_item_popup, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popup);

        TextView name = popup.findViewById(R.id.add_item_popup_name);
        name.setText(item.getName());
        final EditText count = popup.findViewById(R.id.add_item_popup_count);
        count.setText("1");
        final EditText unit = popup.findViewById(R.id.add_item_popup_unit);
        unit.setText(item.getDefaultUnit());
        Button add = popup.findViewById(R.id.add_item_popup_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if(!InputValidator.validInputNumberString(count.getText().toString(),4)){
                        count.setError(getString(R.string.invalid_input));
                        throw new Exception();

                    } else if (!InputValidator.validInputString(unit.getText().toString(), 4)) {
                        unit.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }

                    ItemContainer itemContainer = new ItemContainer(item, Integer.parseInt(count.getText().toString()),unit.getText().toString());
                    ((MainActivity)getActivity()).getShoppinglist().addItem(itemContainer);
                    getActivity().onBackPressed();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        addItemDialog = alertDialogBuilder.create();
        addItemDialog.show();
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search, container, false);
        final ListView listView = (ListView) layout.findViewById(android.R.id.list);
        return layout;
    }
    @SuppressLint("ClickableViewAccessibility")
    public void createItem(){
        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.fragment_search_ll);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popup = inflater.inflate(R.layout.create_item_popup, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popup);

        final AutoCompleteTextView category =  popup.findViewById(R.id.autocomplete_category);
        String[] categories = (((MainActivity)getActivity()).getItemCategorys().getNames().toArray(new String[0]));
        String itemName = text;
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categories);
        category.setAdapter(adapter);

        final Item item = new Item(itemName);
        category.setHint(Item.getDefaultCategory());
        final EditText name = popup.findViewById(R.id.create_item_popup_name);
        name.setText(item.getName());
        final EditText count = popup.findViewById(R.id.create_item_popup_count);
        count.setText("1");
        final EditText unit = popup.findViewById(R.id.create_item_popup_unit);
        unit.setText(item.getDefaultUnit());
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
                    item.setName(name.getText().toString());
                    item.setCategory(category.getText().toString());
                    item.setDefaultunit(unit.getText().toString());
                    ItemContainer itemContainer = new ItemContainer(item, Integer.parseInt(count.getText().toString()), unit.getText().toString());
                    ((MainActivity)getActivity()).getItems().addItem(item);
                    ((MainActivity)getActivity()).getShoppinglist().addItem(itemContainer);
                    ((MainActivity)getActivity()).getItemCategorys().addCategoryName(item.getCategory());
                    getActivity().onBackPressed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        createItemDialog = alertDialogBuilder.create();
        createItemDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*View view = getActivity().findViewById(R.id.fab);
        view.setVisibility(View.INVISIBLE);*/
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setIconified(false);

        menu.findItem(R.id.remove_items).setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);

        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) searchView.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }

        });
    }
    public void onBackPressed()
    {
        if(createItemDialog != null){
            createItemDialog.dismiss();
            createItemDialog = null;
        }
        else if(addItemDialog != null){
            addItemDialog.dismiss();
            addItemDialog = null;
        }
        getFragmentManager().popBackStack();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        List<String> filteredValues = new ArrayList<String>(itemNames);
        for (String value : itemNames) {
            if (!value.toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }

        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, filteredValues);
        setListAdapter(mAdapter);
        text = newText;
        return false;
    }

    public void resetSearch() {
        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, itemNames);
        setListAdapter(mAdapter);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }

}
