package smartshoppinglist.at.smartshoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.ItemCategory;
import smartshoppinglist.at.smartshoppinglist.uiadapters.DragableListViewAdapter;
import smartshoppinglist.at.smartshoppinglist.uiadapters.DragableListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment {

    private List<ItemCategory> categoryList;

    public ItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items, container, false);
        categoryList = MainActivity.getInstance().getItemCategorys().getCategories();
        final DragableListView listView = (DragableListView) v.findViewById(R.id.itemsList);
        DragableListViewAdapter adapter = new DragableListViewAdapter(getContext(), categoryList, new DragableListViewAdapter.Listener() {
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
                Toast.makeText(getActivity().getApplicationContext(), R.string.not_implemented_yet, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
