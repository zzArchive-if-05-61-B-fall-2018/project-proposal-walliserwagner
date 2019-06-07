package smartshoppinglist.at.smartshoppinglist.objects;

import com.google.gson.annotations.Expose;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.localsave.Read;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;
import smartshoppinglist.at.smartshoppinglist.server.Server;

public class Group implements Serializable {
    @Expose private String name;
    @Expose private List<User> users;
    private List<Shoppinglist> shoppinglists;
    @Expose private boolean isDefault = false;
    private int changeset = 1;
    private int id = -1; // for testing offline

    public Group(String name, List<User> users, List<Shoppinglist> shoppinglists) {
        this(name,users,shoppinglists, false);
        createGroup();
    }

    public Group(String name, List<User> users, List<Shoppinglist> shoppinglists, boolean isDefault) {
        this.name = name;
        this.users = users;
        this.shoppinglists = shoppinglists;
        this.isDefault = isDefault;
    }

    public Group(String name, List<User> users) {
        this(name, users, new ArrayList<Shoppinglist>());
    }
    public Group(String name, List<User> users, boolean isDefault) {
        this(name,users);
        this.isDefault = isDefault;
    }

    public Group(String name, User user, Invite invite) {
        this(name, user, false, invite);
    }

    public Group(String name, User user, boolean isDefault, Invite invite) {
        this.name = name;
        shoppinglists = new ArrayList<>();
        users = new ArrayList<>();
        users.add(user);
        this.isDefault = isDefault;
        if(invite == null) {
            createGroup();
        }
        else{
            id = invite.getGroupid();
        }
    }



    private void createGroup(){
        if(isDefault){
            return;
        }
        String tmp = Server.getInstance().postRequest("/group", String.format("{\"userid\":\"%d\",\"name\":\"%s\"}", MainActivity.getInstance().getCurrentUser().getId(), name));
        try {
            id = new JSONObject(tmp).getInt("groupid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId(){
        return id;
    }

    public String[] getUsernames(){
        List<String> result = new ArrayList<>();
        for (User user:getUsers()) {
            result.add(user.getName());
        }
        return result.toArray(new String[0]);
    }


    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        redoShoppinglists();
    }

    public User[] getUsers() {
        return Server.getInstance().getUsersOfGroup(id, MainActivity.getInstance()).toArray(new User[0]);
    }

    public void addUsers(User user) {
        this.users.add(user);
    }

    public Shoppinglist[] getShoppinglists()
    {
        return shoppinglists.toArray(new Shoppinglist[0]);
    }

    public Shoppinglist createList(String shoppinglistname, boolean isdefault){
        Shoppinglist list = new Shoppinglist(shoppinglistname,this, isdefault);
        this.shoppinglists.add(list);
        list.setChanges();
        if(!isDefault) {
            Server.getInstance().postRequest("/createshoppinglist", String.format("{\"groupid\":\"%d\", \"name\":\"%s\"}", id, shoppinglistname));
        }
        sort();
        return list;
    }

    public Shoppinglist createList(String shoppinglistname)
    {
        if(findListByName(shoppinglistname) != null) return null;
        Shoppinglist list = new Shoppinglist(shoppinglistname,this);
        this.shoppinglists.add(list);
        list.setChanges();
        sort();
        if(!isDefault) {
            Server.getInstance().postRequest("/createshoppinglist", String.format("{\"groupid\":\"%d\", \"name\":\"%s\"}", id, shoppinglistname));
        }
        return list;
    }

    public void removeShoppinglist(Shoppinglist shoppinglist) {
        this.shoppinglists.remove(shoppinglist);
        if(!isDefault){
            Server.getInstance().deleteRequest(String.format("/deleteshoppinglist?groupid=%d&listname=%s",id,shoppinglist.getName()));
        }
        Save.remove(shoppinglist.getName(), id);
    }
    public boolean isDefault() {
        return isDefault;
    }

    private void redoShoppinglists(){
        for (Shoppinglist s:shoppinglists) {
            Save.remove(s.getName(), id);
            Save.save(s);
        }
    }

    public void populateShoppinglist(){
        shoppinglists = new ArrayList<>();
        shoppinglists = Read.readShoppinglist(id);
    }

    public Shoppinglist findListByName(String name){
        for (Shoppinglist shoppinglist:shoppinglists) {
            if(shoppinglist.getName().equals(name)){
                return shoppinglist;
            }
        }
        return null;
    }
    public void sort(){
        Collections.sort(shoppinglists);
    }

    public String[] getShoppingListNames(){
        String[] names = new String[shoppinglists.size()];
        for (int i = 0; i < shoppinglists.size();i++) {
            names[i] = shoppinglists.get(i).getName();
        }
        return names;
    }
}