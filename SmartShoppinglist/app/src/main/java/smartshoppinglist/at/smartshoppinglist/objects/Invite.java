package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;

import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.server.Server;

public class Invite implements Serializable {
    private String group;
    private int groupid;
    private String senderEmail;

    public Invite(String group, int groupid, String email) {
        this.group = group;
        this.groupid = groupid;
        this.senderEmail = email;
    }

    public String getGroup() {
        return group;
    }

    public int getGroupid(){return groupid; }

    public String getSenderEmail() {
        return senderEmail;
    }

}