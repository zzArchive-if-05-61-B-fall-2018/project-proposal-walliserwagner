package smartshoppinglist.at.smartshoppinglist.uiadapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.objects.Category;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemCategory;

public class DragableListViewAdapter extends ArrayAdapter<ItemCategory> {

    final int INVALID_ID = -1;
    public interface Listener {
        void onGrab(int position, RelativeLayout row);
    }

    final Listener listener;
    final Map<ItemCategory, Integer> mIdMap = new HashMap<>();

    public DragableListViewAdapter(Context context, List<ItemCategory> list, Listener listener) {
        super(context, 0, list);
        this.listener = listener;
        for (int i = 0; i < list.size(); ++i) {
            mIdMap.put(list.get(i), i);
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Context context = getContext();
        final ItemCategory item = getItem(position);
        if(null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.dragable_listview_row, null);
        }

        final RelativeLayout row = (RelativeLayout) view.findViewById(
                R.id.dragableRow);


        TextView textView = (TextView)view.findViewById(R.id.textViewTitle);
        textView.setText(item.getName());

        view.findViewById(R.id.imageViewGrab)
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        listener.onGrab(position, row);
                        return false;
                    }
                });

        return view;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        ItemCategory item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }
}
