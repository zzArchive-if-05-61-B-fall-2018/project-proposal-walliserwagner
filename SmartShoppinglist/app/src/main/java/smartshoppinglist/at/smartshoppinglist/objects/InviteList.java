package smartshoppinglist.at.smartshoppinglist.objects;

import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.localsave.Save;

public class InviteList {
    private List<Invite> invites;

    public InviteList() {
        invites = new ArrayList<>();
    }

    public Invite findInviteByName(String group){
        for (Invite invite:invites) {
            if (invite.getGroup().equals(group)){
                return invite;
            }
        }
        return null;
    }

    public boolean addInvite(Invite invite){
        if(findInviteByName(invite.getGroup())!= null) return false;
        invites.add(invite);
        setChanges();
        return true;
    }
    public void removeInvite(Invite invite){
        invites.remove(invite);
        setChanges();
    }
    public Invite[] getInvites(){
        return invites.toArray(new Invite[0]);
    }


    private void setChanges(){
        Save.save(this);
    }
}
