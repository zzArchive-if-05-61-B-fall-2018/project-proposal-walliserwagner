package smartshoppinglist.at.smartshoppinglist.uiadapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.Recipe;

public class RecipeViewItemListAdapter extends ArrayAdapter<ItemContainer> {
    private Activity context;
    private Recipe recipe;


    public RecipeViewItemListAdapter(Activity context, Recipe recipe) {
        super(context, R.layout.item_row);
        this.context = context;
        this.recipe = recipe;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView= inflater.inflate(R.layout.list_item, null, true);
        TextView name = rowView.findViewById(R.id.lblListItemName);
        name.setText(recipe.getItems().get(position).getItem().getName());
        TextView unit = rowView.findViewById(R.id.lblListItemUnit);
        unit.setText(recipe.getItems().get(position).getUnit());
        ImageView icon = rowView.findViewById(R.id.lblListItemIcon);
        icon.setImageResource(recipe.getItems().get(position).getItem().getIcon());
        TextView count = rowView.findViewById(R.id.lblListItemCount);
        count.setText(Integer.toString(recipe.getItems().get(position).getCount()));
        final CheckBox checkBox = rowView.findViewById(R.id.lblListCheckbox);
        ItemContainer i = recipe.getItems().get(position);
        if( i.isTicked()){
            checkBox.setChecked(true);
            notifyDataSetChanged();
        }
        else {
            checkBox.setChecked(false);
            notifyDataSetChanged();
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    i.setTicked(true);
                    notifyDataSetChanged();
                }
                else {
                    i.setTicked(false);
                    notifyDataSetChanged();
                }
            }
        });
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
