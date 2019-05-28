package smartshoppinglist.at.smartshoppinglist.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import smartshoppinglist.at.smartshoppinglist.InputValidator;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Config;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;
import smartshoppinglist.at.smartshoppinglist.uiadapters.ListsExpandableAdapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    private ListsExpandableAdapter listAdapter;
    private ExpandableListView shoppingListList;
    private GroupList groupList;
    private AlertDialog dialog;
    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.shopping_lists);
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
                getFragmentManager().popBackStack();
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

        int groupPos = 0, childPos = 0;
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
        {
            groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
            childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
        }

        if(groupList.getGroups()[groupPos].getShoppinglists()[childPos].isDefault()) return;


        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            menu.setHeaderTitle("Optionen");
            ((AppCompatActivity) getActivity()).getMenuInflater().inflate(R.menu.list_long_click_menu, menu);
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
            case R.id.list_longClick_alter:
                alterList(groupList.getGroups()[groupPos].getShoppinglists()[childPos], groupList.getGroups()[groupPos]);
                return true;
            case R.id.list_longClick_remove:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage(R.string.really_want_to_delete_shoppinglist);
                dialog.setCancelable(false);

                int finalGroupPos = groupPos;
                int finalChildPos = childPos;
                dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(groupList.getGroups()[finalGroupPos].getShoppinglists()[finalChildPos].equals(MainActivity.getInstance().getShoppinglist())) MainActivity.getInstance().setShoppinglist(null);
                        groupList.getGroups()[finalGroupPos].removeShoppinglist(groupList.getGroups()[finalGroupPos].getShoppinglists()[finalChildPos]);
                        listAdapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.create().show();
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
    public void alterList(Shoppinglist shoppinglist, Group group){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.simple_add_popup, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popup);
        final EditText name = popup.findViewById(R.id.simple_add_popup_name);
        name.setHint(shoppinglist.getName());
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
                        shoppinglist.setName(name.getText().toString());
                        group.sort();
                        listAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }catch (Exception e) {
                }
            }
        });
        dialog = alertDialogBuilder.create();
        dialog.show();
    }

    public void addListPooup(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.add_list_popup, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popup);
        final EditText name = popup.findViewById(R.id.add_list_popup_name);
        name.setHint(R.string.list_name);
        Spinner spinner = popup.findViewById(R.id.add_list_popup_group_dropdown);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),

                android.R.layout.simple_spinner_item,groupList.getGroupNames());

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        Button button = popup.findViewById(R.id.add_list_popup_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!InputValidator.validInputString(name.getText().toString(),20)) {
                        name.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    Shoppinglist list =  ((MainActivity)getActivity()).getGroups().findGroupByName(spinner.getSelectedItem().toString()).createList(name.getText().toString());
                    if(list == null){
                        name.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    listAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog = alertDialogBuilder.create();
        dialog.show();

    }
}

