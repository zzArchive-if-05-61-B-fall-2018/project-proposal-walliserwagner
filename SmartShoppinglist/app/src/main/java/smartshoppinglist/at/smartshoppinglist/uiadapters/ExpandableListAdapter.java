package smartshoppinglist.at.smartshoppinglist.uiadapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Shoppinglist shoppinglist;


    public ExpandableListAdapter(Context context, Shoppinglist shoppinglist) {
        this.context = context;
        this.shoppinglist = shoppinglist;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.shoppinglist.getItems()[groupPosition].getElements()[childPosititon];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemContainer itemContainer = (ItemContainer)shoppinglist.getItems()[groupPosition].getElements()[childPosition];
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItemName);
        txtListChild.setText(itemContainer.getItem().getName());
        ImageView imgListChild = (ImageView) convertView.findViewById(R.id.lblListItemIcon);
        imgListChild.setImageResource(itemContainer.getItem().getIcon());
        txtListChild = (TextView) convertView.findViewById(R.id.lblListItemCount);
        txtListChild.setText(Integer.toString(itemContainer.getCount()));
        txtListChild = (TextView) convertView.findViewById(R.id.lblListItemUnit);
        txtListChild.setText(itemContainer.getUnit());

        final CheckBox checkBox = convertView.findViewById(R.id.lblListCheckbox);
        if(shoppinglist.getItemByPos(groupPosition,childPosition).isTicked()){
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
                    shoppinglist.tickItem(shoppinglist.getItemByPos(groupPosition,childPosition));
                    notifyDataSetChanged();
                }
                else {
                    shoppinglist.unTickItem(shoppinglist.getItemByPos(groupPosition,childPosition));
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.shoppinglist.getItems()[groupPosition].getElements().length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.shoppinglist.getItems()[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return this.shoppinglist.getItems().length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(shoppinglist.getItems()[groupPosition].getName());
        lblListHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ImageView indicator = (ImageView) convertView.findViewById(R.id.indicator);
        indicator.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageView image =  v.findViewById(R.id.indicator);
                if(isExpanded){
                    ((ExpandableListView) parent).collapseGroup(groupPosition);
                    image.setImageResource(R.drawable.ic_arrow_down);
                }
                else {
                    ((ExpandableListView) parent).expandGroup(groupPosition, true);
                    image.setImageResource(R.drawable.ic_arrow_up);
                }

            }
        });


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
}