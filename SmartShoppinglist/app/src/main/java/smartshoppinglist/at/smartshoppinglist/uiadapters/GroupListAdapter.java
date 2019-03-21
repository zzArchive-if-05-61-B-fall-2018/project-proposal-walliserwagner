package smartshoppinglist.at.smartshoppinglist.uiadapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;

public class GroupListAdapter extends ArrayAdapter<Group> {
    private Activity context;
    private GroupList groupList;


    public GroupListAdapter(Activity context, GroupList groupList) {
        super(context, R.layout.listview_row);
        this.context = context;
        this.groupList = groupList;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView= inflater.inflate(R.layout.listview_row, null, true);
        TextView tv = rowView.findViewById(R.id.listview_row_text);
        tv.setText(groupList.getGroups()[position].getName());
        return rowView;
    }
    @Override
    public int getCount()
    {
        return groupList.getGroups().length;
    }

    @Override
    public Group getItem(int position)
    {
        return groupList.getGroups()[position];
    }
}
