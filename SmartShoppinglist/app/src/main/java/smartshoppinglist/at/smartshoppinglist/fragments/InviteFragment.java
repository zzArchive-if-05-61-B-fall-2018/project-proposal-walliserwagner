package smartshoppinglist.at.smartshoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Invite;
import smartshoppinglist.at.smartshoppinglist.objects.InviteList;
import smartshoppinglist.at.smartshoppinglist.uiadapters.InviteArrayAdapter;

import android.widget.ListView;

import java.util.List;

public class InviteFragment extends Fragment {

    private static final String TAG = "CardListActivity";
    private InviteArrayAdapter inviteArrayAdapter;
    private ListView listView;

    public InviteFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.invites);
        View v = inflater.inflate(R.layout.fragment_invite, container, false);
        listView = (ListView) v.findViewById(R.id.card_listView);

        InviteList inviteList = MainActivity.getInstance().getInviteList();
        inviteArrayAdapter = new InviteArrayAdapter(getContext(), R.layout.list_item_card, MainActivity.getInstance().getInviteList());
        listView.setAdapter(inviteArrayAdapter);
        return v;
    }
    public void changeFragment(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        getFragmentManager().popBackStack();
        fragmentTransaction.replace(R.id.main_container, new GroupFragment());
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }
    public void reload(){
        inviteArrayAdapter.notifyDataSetChanged();
    }
}