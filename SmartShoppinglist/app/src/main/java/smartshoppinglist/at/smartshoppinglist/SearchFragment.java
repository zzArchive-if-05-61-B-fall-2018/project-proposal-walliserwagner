package smartshoppinglist.at.smartshoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.support.v4.app.ListFragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private List<String> itemNames;
    private ItemList items;
    private ArrayAdapter<String> mAdapter;
    private Context mContext;
    private String text;
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
        String item = (String) listView.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(),AddItemPopUp.class);
        intent.putExtra ("item", new Gson().toJson(items.FindItemByName(item)));
        startActivityForResult(intent,1);
        View view = getActivity().findViewById(R.id.fab);
        view.setVisibility(View.VISIBLE);
        getFragmentManager().popBackStack();
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search, container, false);
        final ListView listView = (ListView) layout.findViewById(android.R.id.list);
        Button emptyTextView = (Button) layout.findViewById(R.id.searchfragment_create_item);
        listView.setEmptyView(emptyTextView);

        emptyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] categories = (((MainActivity)getActivity()).getItemCategorys().getNames().toArray(new String[0]));
                Intent intent = new Intent(getActivity(),CreateItemPopUp.class);
                intent.putExtra ("name", new Gson().toJson(text));
                intent.putExtra("categories",new Gson().toJson(categories));
                startActivityForResult(intent,2);
                View view = getActivity().findViewById(R.id.fab);
                view.setVisibility(View.VISIBLE);
                getFragmentManager().popBackStack();
            }
        });
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        View view = getActivity().findViewById(R.id.fab);
        view.setVisibility(View.INVISIBLE);
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Suchen");
        searchView.setIconified(false);
        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) searchView.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if(view != null){
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
                getActivity().onBackPressed();
            }

        });
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
