package smartshoppinglist.at.smartshoppinglist.uiadapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.Recipe;

public class RecipeItemListAdapter extends ArrayAdapter<ItemContainer> {
    private Activity context;
    private Recipe recipe;


    public RecipeItemListAdapter(Activity context, Recipe recipe) {
        super(context, R.layout.item_row);
        this.context = context;
        this.recipe = recipe;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView= inflater.inflate(R.layout.recipe_list_item, null, true);
        TextView name = rowView.findViewById(R.id.lblListItemName);
        name.setText(recipe.getItems().get(position).getItem().getName());
        TextView unit = rowView.findViewById(R.id.lblListItemUnit);
        unit.setText(recipe.getItems().get(position).getUnit());
        ImageView icon = rowView.findViewById(R.id.lblListItemIcon);
        icon.setImageResource(recipe.getItems().get(position).getItem().getIcon());
        TextView count = rowView.findViewById(R.id.lblListItemCount);
        count.setText(Integer.toString(recipe.getItems().get(position).getCount()));
        return rowView;
    }


    @Override
    public int getCount()
    {
        return recipe.getItems().size();
    }

    @Override
    public ItemContainer getItem(int position)
    {
        return recipe.getItems().get(position);
    }
}
