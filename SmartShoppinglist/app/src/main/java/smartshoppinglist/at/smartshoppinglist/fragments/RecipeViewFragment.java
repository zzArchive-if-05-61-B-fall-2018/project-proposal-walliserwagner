package smartshoppinglist.at.smartshoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.Recipe;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;
import smartshoppinglist.at.smartshoppinglist.uiadapters.RecipeViewItemListAdapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class RecipeViewFragment extends Fragment {
    private Recipe recipe = new Recipe("","", new ArrayList<>());
    private RecipeViewItemListAdapter recipeViewItemListAdapter;
    private View v;
    private AlertDialog dialog;
    private Group group;

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
        recipeViewItemListAdapter = new RecipeViewItemListAdapter(getActivity(),recipe);
        listView.setAdapter(recipeViewItemListAdapter);
        registerForContextMenu(listView);
        return v;
    }

    public void onBackPressed()
    {
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
        getFragmentManager().popBackStack();
    }

    public void addItemsToList(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.add_items_to_list_popup, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popup);

        group = MainActivity.getInstance().getShoppinglist().getGroup();

        Spinner listSpinner = popup.findViewById(R.id.add_to_list_popup_list_dropdown);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(),

                android.R.layout.simple_spinner_item,group.getShoppingListNames());

        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        listSpinner.setAdapter(listAdapter);
        Spinner groupSpinner = popup.findViewById(R.id.add_to_list_popup_group_dropdown);
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(getActivity(),

                android.R.layout.simple_spinner_item, MainActivity.getInstance().getGroups().getGroupNames());

        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        groupSpinner.setAdapter(groupAdapter);
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                group = MainActivity.getInstance().getGroups().getGroups()[position];
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button button = popup.findViewById(R.id.add_to_list_popup_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Shoppinglist shoppinglist = group.findListByName(listSpinner.getSelectedItem().toString());
                    for (ItemContainer ic: recipe.getItems()) {
                        if(ic.isTicked()){
                            ic.setTicked(false);
                            shoppinglist.addItem(ic);
                        }
                    }
                    getActivity().onBackPressed();
                }catch (Exception e) {
                }
            }
        });
        dialog = alertDialogBuilder.create();
        dialog.show();
    }
}
