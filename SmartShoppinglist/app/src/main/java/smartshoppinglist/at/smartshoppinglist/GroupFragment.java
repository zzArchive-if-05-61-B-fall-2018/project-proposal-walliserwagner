package smartshoppinglist.at.smartshoppinglist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {

    ListView groupList;

    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<String> groups = new ArrayList<>();
        groups.add("Familie");
        View v = inflater.inflate(R.layout.fragment_group, container, false);
        groupList = (ListView) v.findViewById(R.id.groupList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.simple_list_item,groups);
        groupList.setAdapter(adapter);
        registerForContextMenu(groupList);
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
            case R.id.group_longClick_remove:
                Toast.makeText(getActivity().getApplicationContext(), "Option 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.group_longClick_invite:
                Toast.makeText(getActivity().getApplicationContext(), "Option 3 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
