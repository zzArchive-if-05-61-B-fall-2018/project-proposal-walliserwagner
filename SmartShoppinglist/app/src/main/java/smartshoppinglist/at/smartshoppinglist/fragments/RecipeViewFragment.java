package smartshoppinglist.at.smartshoppinglist.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import smartshoppinglist.at.smartshoppinglist.InputValidator;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Recipe;


public class RecipeViewFragment extends Fragment {
    private Recipe recipe = new Recipe("","", new ArrayList<>());
    View v;

    public RecipeViewFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            Recipe r = (Recipe) getArguments().getSerializable("recipe");
            if(r != null) recipe = r;
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(recipe.getName());
        }
        v = inflater.inflate(R.layout.fragment_recipe_view, container, false);
        TextView name = v.findViewById(R.id.recipe_fragment_recipe_name);
        name.setText(recipe.getName());
        TextView description = v.findViewById(R.id.recipe_fragment_recipe_text);
        description.setText(recipe.getDescription());
        ListView listView = v.findViewById(R.id.recipe_fragment_add_ingredients_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_list_item,recipe.getItemNames());
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        return v;
    }

    public void onBackPressed()
    {
        getFragmentManager().popBackStack();
    }
}
