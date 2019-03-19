package smartshoppinglist.at.smartshoppinglist.fragments;


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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.Toast;

import smartshoppinglist.at.smartshoppinglist.uiadapters.ExpandableListAdapter;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.objects.Category;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    private Shoppinglist shoppinglist;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.remove_items_menu, menu);
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
        listAdapter = new ExpandableListAdapter(getActivity().getApplicationContext(), shoppinglist,expListView);
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
            menu.setHeaderTitle("Optionen");
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
                Toast.makeText(getActivity().getApplicationContext(), "Option 1 selected", Toast.LENGTH_SHORT).show();
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
        dialog.setMessage(R.string.really_want_to_delete);
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
        Category<ItemContainer>[] category = shoppinglist.getItems();
        for (int i = 0; i < category.length;i++) {
            if (category[i].isExpanded()){
                expListView.expandGroup(i);
            }
        }
    }
}
