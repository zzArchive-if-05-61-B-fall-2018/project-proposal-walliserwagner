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
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Category;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.uiadapters.ListsExpandableAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    private ListsExpandableAdapter listAdapter;
    private ExpandableListView shoppingListList;
    private GroupList groupList;
    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupList = MainActivity.getInstance().getGroups();
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        shoppingListList =  v.findViewById(R.id.shoppingListList);
        shoppingListList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        shoppingListList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ((MainActivity)getActivity()).setShoppinglist(groupList.getGroups()[groupPosition].getShoppinglists()[childPosition]);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container, new HomeFragment());
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
                return false;
            }
        });
        listAdapter = new ListsExpandableAdapter(getActivity().getApplicationContext(),groupList ,shoppingListList);
        shoppingListList.setAdapter(listAdapter);
        registerForContextMenu(shoppingListList);
        expandListView();
        return v;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            menu.setHeaderTitle("Optionen");
            ((AppCompatActivity) getActivity()).getMenuInflater().inflate(R.menu.list_long_click_menu, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_longClick_alter:
                Toast.makeText(getActivity().getApplicationContext(), "Option 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.list_longClick_remove:
                Toast.makeText(getActivity().getApplicationContext(), "Option 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void expandListView(){
        Group[] groups = groupList.getGroups();
        for (int i = 0; i < groups.length;i++) {
                shoppingListList.expandGroup(i);
            }

    }
}

