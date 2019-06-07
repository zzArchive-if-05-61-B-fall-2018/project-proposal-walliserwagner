package smartshoppinglist.at.smartshoppinglist.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import smartshoppinglist.at.smartshoppinglist.InputValidator;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.AlterGroupActivity;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Config;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.uiadapters.GroupListAdapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {
    public static final int REQUEST_ID = 1;
    private ListView groupListView;
    private AlertDialog dialog;
    private ArrayAdapter<Group> adapter;

    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.groups);
        GroupList groupList = MainActivity.getInstance().getGroups();
        View v = inflater.inflate(R.layout.fragment_group, container, false);
        groupListView = (ListView) v.findViewById(R.id.groupList);
        adapter = new GroupListAdapter(getActivity(),groupList);
        groupListView.setAdapter(adapter);
        registerForContextMenu(groupListView);
        return v;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.options);
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.group_long_click_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        switch (item.getItemId()) {
            case R.id.group_longClick_alter:
                Intent intent = new Intent(getActivity(), AlterGroupActivity.class);
                Group group = MainActivity.getInstance().getGroups().getGroups()[index];
                intent.putExtra ("group", group);
                startActivityForResult(intent,REQUEST_ID);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void createGroup(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.simple_add_popup, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popup);
        final EditText name = popup.findViewById(R.id.simple_add_popup_name);
        name.setHint(R.string.group_name);
        Button button = popup.findViewById(R.id.simple_add_popup_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!InputValidator.validInputString(name.getText().toString(),20)) {
                        name.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    Group group = new Group(name.getText().toString(), Config.getInstance().getUser(), false);
                    if(!((MainActivity)getActivity()).getGroups().addGroup(group)) {
                        name.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                }catch (Exception e) {
                }
            }
        });
        dialog = alertDialogBuilder.create();
        dialog.show();
    }
    public void notifyDatasetChanged(){
        adapter.notifyDataSetChanged();
    }
}
