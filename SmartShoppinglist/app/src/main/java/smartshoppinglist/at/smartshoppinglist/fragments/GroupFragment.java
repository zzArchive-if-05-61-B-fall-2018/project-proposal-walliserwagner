package smartshoppinglist.at.smartshoppinglist.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.uiadapters.GroupListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {

    ListView groupListView;

    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GroupList groupList = MainActivity.getInstance().getGroups();
        View v = inflater.inflate(R.layout.fragment_group, container, false);
        groupListView = (ListView) v.findViewById(R.id.groupList);
        ArrayAdapter<Group> adapter = new GroupListAdapter(getActivity(),groupList);
        groupListView.setAdapter(adapter);
        registerForContextMenu(groupListView);
        return v;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Optionen");
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.group_long_click_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group_longClick_alter:
                Toast.makeText(getActivity().getApplicationContext(), "Option 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.group_longClick_quit:
                Toast.makeText(getActivity().getApplicationContext(), "Option 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.group_longClick_remove:
                Toast.makeText(getActivity().getApplicationContext(), "Option 3 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.group_longClick_invite:
                Toast.makeText(getActivity().getApplicationContext(), "Option 4 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
