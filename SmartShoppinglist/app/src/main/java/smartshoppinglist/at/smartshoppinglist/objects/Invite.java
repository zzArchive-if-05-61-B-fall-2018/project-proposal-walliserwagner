package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;

public class Invite implements Serializable {
    private String group;
    private String senderEmail;

    public Invite(String group, String email) {
        this.group = group;
        this.senderEmail = email;
    }

    public String getGroup() {
        return group;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

}