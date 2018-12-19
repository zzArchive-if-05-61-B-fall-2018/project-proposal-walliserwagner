package smartshoppinglist.at.smartshoppinglist;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {

    ListView resipeList;
    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<String> recipes = new ArrayList<>();
        recipes.add("Schnitzel");
        recipes.add("Nudelsuppe");
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);
        resipeList = (ListView) v.findViewById(R.id.recipeList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.simple_list_item,recipes);
        resipeList.setAdapter(adapter);
        registerForContextMenu(resipeList);
        return v;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Optionen");
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.recipe_long_click_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recipe_longClick_alter:
                Toast.makeText(getActivity().getApplicationContext(), "Option 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.recipe_longClick_remove:
                Toast.makeText(getActivity().getApplicationContext(), "Option 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.recipe_longClick_post:
                Toast.makeText(getActivity().getApplicationContext(), "Option 3 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
