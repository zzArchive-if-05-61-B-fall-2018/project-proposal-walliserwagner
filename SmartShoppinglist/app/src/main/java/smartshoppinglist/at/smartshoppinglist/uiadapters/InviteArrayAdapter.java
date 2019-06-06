package smartshoppinglist.at.smartshoppinglist.uiadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Invite;
import smartshoppinglist.at.smartshoppinglist.objects.InviteList;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;
import smartshoppinglist.at.smartshoppinglist.server.Server;

public class InviteArrayAdapter extends ArrayAdapter<Invite> {
    private static final String TAG = "InviteArrayAdapter";
    private InviteList inviteList;

    static class CardViewHolder {
        TextView line1;
        TextView line2;
    }

    public InviteArrayAdapter(Context context, int textViewResourceId, InviteList inviteList) {
        super(context, textViewResourceId);
        this.inviteList = inviteList;
    }

    @Override
    public int getCount() {
        return this.inviteList.getInvites().length;
    }

    @Override
    public Invite getItem(int index) {
        return this.inviteList.getInvites()[index];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.group);
            viewHolder.line2 = (TextView) row.findViewById(R.id.email);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        Invite card = getItem(position);
        viewHolder.line1.setText(card.getGroup());
        viewHolder.line2.setText(card.getSenderEmail());
        ImageButton accept = row.findViewById(R.id.btn_accept);
        ImageButton deny = row.findViewById(R.id.btn_deny);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Server.getInstance().deleteRequest(String.format("/invite?userid=%d&groupid=%d&accepted=true", MainActivity.getInstance().getCurrentUser().getId(), inviteList.getInvites()[position].getGroupid()));
                MainActivity.getInstance().getInviteList().removeInvite(inviteList.getInvites()[position]);
                notifyDataSetChanged();
            }
        });
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Server.getInstance().deleteRequest(String.format("/invite?userid=%d&groupid=%d&accepted=false", MainActivity.getInstance().getCurrentUser().getId(), inviteList.getInvites()[position].getGroupid()));
                MainActivity.getInstance().getInviteList().removeInvite(inviteList.getInvites()[position]);
                notifyDataSetChanged();
            }
        });
        return row;
    }

}

