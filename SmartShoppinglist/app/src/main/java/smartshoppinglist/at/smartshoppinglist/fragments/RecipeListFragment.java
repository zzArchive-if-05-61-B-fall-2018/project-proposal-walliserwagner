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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Recipe;
import smartshoppinglist.at.smartshoppinglist.objects.RecipeList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {

    ListView recipeListView;
    RecipeList recipeList;
    ArrayAdapter<String> adapter;
    List<String> recipes;
    public RecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.recipes);
        recipeList = MainActivity.getInstance().getRecipeList();
        View v = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        recipeListView = (ListView) v.findViewById(R.id.recipeList);
        recipes = Arrays.asList(recipeList.getNames());
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.simple_list_item,recipes);
        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeViewFragment recipeViewFragment = new RecipeViewFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("recipe", recipeList.getRecipes()[position]);
                recipeViewFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container, recipeViewFragment,"recipeView");
                fragmentTransaction.addToBackStack("recipeView");
                fragmentTransaction.commit();

            }
        });
        recipeListView.setAdapter(adapter);
        registerForContextMenu(recipeListView);
        return v;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.options);
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.recipe_long_click_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()) {
            case R.id.recipe_longClick_alter:
                RecipeFragment recipeFragment = new RecipeFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("recipe", new Gson().fromJson((new Gson().toJson(recipeList.getRecipes()[index])), Recipe.class));
                recipeFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container, recipeFragment,"recipe");
                fragmentTransaction.addToBackStack("recipe");
                fragmentTransaction.commit();
                return true;
            case R.id.recipe_longClick_remove:
                recipeList.removeRecipe(recipeList.getRecipes()[index]);
                recipes = Arrays.asList(recipeList.getNames());
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void createRecipe(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, new RecipeFragment(),"recipe");
        fragmentTransaction.addToBackStack("recipe");
        fragmentTransaction.commit();
    }
}
