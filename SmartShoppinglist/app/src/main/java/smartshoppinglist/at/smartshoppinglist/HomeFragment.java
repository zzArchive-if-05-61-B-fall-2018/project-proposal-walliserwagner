package smartshoppinglist.at.smartshoppinglist;


import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shoppinglist = ((MainActivity)getActivity()).getShoppinglist();

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        expListView = (ExpandableListView) v.findViewById(R.id.listView);
        listAdapter = new ExpandableListAdapter(getActivity().getApplicationContext(), shoppinglist);
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
    private void expandListView(){
        Category<ItemContainer>[] category = shoppinglist.getItems();
        for (int i = 0; i < category.length;i++) {
            if (category[i].isExpanded()){
                expListView.expandGroup(i);
            }
        }
    }
    public void searchItem(Application application) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new SearchFragment());
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }
}
