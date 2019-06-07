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

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Category;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;
import smartshoppinglist.at.smartshoppinglist.server.Server;

public class ListsExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private GroupList groupList;
    private ExpandableListView expandableListView;


    public ListsExpandableAdapter(Context context, GroupList groupList, ExpandableListView expandableListView) {
        this.context = context;
        this.groupList = groupList;
        this.expandableListView = expandableListView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.groupList.getGroups()[groupPosition].getShoppinglists()[childPosititon];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Shoppinglist shoppinglist = groupList.getGroups()[groupPosition].getShoppinglists()[childPosition];
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandablelistview_row, null);
        }

        TextView textView = convertView.findViewById(R.id.expandablelistview_row_text);
        textView.setText(shoppinglist.getName());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.groupList.getGroups()[groupPosition].getShoppinglists().length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.groupList.getGroups()[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return this.groupList.getGroups().length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        Group[] groups = groupList.getGroups();
        if(groups.length > groupPosition){
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }
            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            Group group = groups[groupPosition];
            /*if(!isExpanded){
                expandableListView.expandGroup(groupPosition);
            }*/
            lblListHeader.setText(group.getName());
            lblListHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            ImageView image =  convertView.findViewById(R.id.indicator);
            if(isExpanded){
                image.setImageResource(R.drawable.ic_arrow_up);
            }
            else {
                image.setImageResource(R.drawable.ic_arrow_down);
            }
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

        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        if(MainActivity.getInstance().getGroups().getGroups()[groupPosition].isDefault() || Server.getInstance().isConnected(MainActivity.getInstance())) return true;
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
}