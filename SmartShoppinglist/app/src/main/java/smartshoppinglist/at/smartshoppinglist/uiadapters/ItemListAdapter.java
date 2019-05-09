package smartshoppinglist.at.smartshoppinglist.uiadapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.Item;

public class ItemListAdapter extends ArrayAdapter<Item> {
    private Activity context;
    private List<Item> items;


    public ItemListAdapter(Activity context, List<Item> items) {
        super(context, R.layout.item_row);
        this.context = context;
        this.items = items;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView= inflater.inflate(R.layout.item_row, null, true);
        TextView name = rowView.findViewById(R.id.category_item_name);
        name.setText(items.get(position).getName());
        TextView unit = rowView.findViewById(R.id.category_item_unit);
        unit.setText(items.get(position).getDefaultUnit());
        ImageView icon = rowView.findViewById(R.id.category_item_icon);
        icon.setImageResource(items.get(position).getIcon());
        return rowView;
    }


    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Item getItem(int position)
    {
        return items.get(position);
    }
}
