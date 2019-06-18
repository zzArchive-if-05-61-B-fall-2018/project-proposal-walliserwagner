package smartshoppinglist.at.smartshoppinglist.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

import smartshoppinglist.at.smartshoppinglist.InputValidator;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.Recipe;
import smartshoppinglist.at.smartshoppinglist.uiadapters.RecipeItemListAdapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class RecipeFragment extends Fragment {
    private Recipe recipe = new Recipe("","", new ArrayList<>());
    private View v;
    private AlertDialog createItemDialog;
    private Context mContext;
    private boolean isAlter = false;
    private Recipe oldRecipe;
    private RecipeItemListAdapter recipeItemListAdapter;

    public RecipeFragment(){

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if(getArguments() != null){
            Recipe r = (Recipe) getArguments().getSerializable("recipe");
            if(r != null) {
                recipe = new Recipe(r);
                isAlter = true;
                oldRecipe = r;
            }
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(recipe.getName());
        }
        v = inflater.inflate(R.layout.fragment_recipe, container, false);
        EditText name = v.findViewById(R.id.recipe_fragment_recipe_name);
        name.setText(recipe.getName());
        EditText description = v.findViewById(R.id.recipe_fragment_recipe_text);
        description.setText(recipe.getDescription());
        ListView listView = v.findViewById(R.id.recipe_fragment_add_ingredients_list);
        recipeItemListAdapter = new RecipeItemListAdapter(getActivity(),recipe);
        listView.setAdapter(recipeItemListAdapter);
        registerForContextMenu(listView);
        ImageButton add = v.findViewById(R.id.recipe_fragment_add_ingredients);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        ScrollView parent = v.findViewById(R.id.recipe_fragment_scrollview);
        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.findViewById(R.id.recipe_fragment_description_scrollview).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        ScrollView child = v.findViewById(R.id.recipe_fragment_description_scrollview);
        child.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        return v;
    }

    public void createRecipe(){
        if(v == null) return;
        EditText name = v.findViewById(R.id.recipe_fragment_recipe_name);
        EditText description = v.findViewById(R.id.recipe_fragment_recipe_text);
        if(!InputValidator.validInputString(name.getText().toString(),20)) {
            name.setError(getString(R.string.invalid_input));
            return;
        }
        else if(!isAlter && MainActivity.getInstance().getRecipeList().findRecipeByName(name.getText().toString()) != null){
            name.setError(getString(R.string.invalid_input));
            return;
        }
        recipe.setName(name.getText().toString());
        recipe.setDescription(description.getText().toString());
        if(isAlter) MainActivity.getInstance().getRecipeList().removeRecipe(MainActivity.getInstance().getRecipeList().findRecipeByName(oldRecipe.getName()));
        MainActivity.getInstance().getRecipeList().addRecipe(recipe);
        getActivity().onBackPressed();

    }
    public void addItem(){
        addItem(new ItemContainer(new Item("")));
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addItem(ItemContainer item){
        if(item == null) return;
        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.fragment_search_ll);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popup = inflater.inflate(R.layout.recipe_add_item_popup, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(popup);

        final AutoCompleteTextView name =  popup.findViewById(R.id.autocomplete_name);
        String[] names = MainActivity.getInstance().getItems().getItemNames().toArray(new String[0]);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, names);
        name.setAdapter(adapter);

        if(item.getItem().getName().equals("")) {
            name.setHint(R.string.item_name);
        }
        else {
            name.setText(item.getItem().getName());
        }
        final EditText count = popup.findViewById(R.id.add_item_popup_count);
        count.setText(Integer.toString(item.getCount()));
        final EditText unit = popup.findViewById(R.id.add_item_popup_unit);
        unit.setText(item.getUnit());
        name.setThreshold(1);
        name.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                name.showDropDown();
                return false;
            }
        });
        Button add = popup.findViewById(R.id.add_item_popup_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!InputValidator.validInputEmptyString(name.getText().toString(),20)){
                        name.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    else if(!InputValidator.validInputNumberString(count.getText().toString(),4)){
                        count.setError(getString(R.string.invalid_input));
                        throw new Exception();

                    } else if (!InputValidator.validInputString(unit.getText().toString(), 4)) {
                        unit.setError(getString(R.string.invalid_input));
                        throw new Exception();
                    }
                    MainActivity.getInstance().getItems().addItem(new Item(name.getText().toString()));
                    Item newItem = MainActivity.getInstance().getItems().findItemByName(name.getText().toString());
                    ItemContainer itemContainer = new ItemContainer(newItem, Integer.parseInt(count.getText().toString()), unit.getText().toString());
                    recipe.removeItem(item);
                    recipe.addItem(itemContainer);
                    recipeItemListAdapter.notifyDataSetChanged();
                    getActivity().onBackPressed();
                } catch (Exception e) {
                }
            }
        });
        createItemDialog = alertDialogBuilder.create();
        createItemDialog.show();
    }
    public void onBackPressed()
    {
        if(createItemDialog != null){
            createItemDialog.dismiss();
            createItemDialog = null;
        }
        else {
            getFragmentManager().popBackStack();
        }

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.options);
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.list_long_click_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()) {
            case R.id.list_longClick_remove:
                recipe.removeItem(recipe.getItems().get(index));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
