package smartshoppinglist.at.smartshoppinglist.objects;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.localsave.Read;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;

public class Group implements Serializable {
    @Expose private String name;
    @Expose private List<User> users;
    private List<Shoppinglist> shoppinglists;
    @Expose private boolean isDefault = false;

    public Group(String name, List<User> users, List<Shoppinglist> shoppinglists) {
        this.name = name;
        this.users = users;
        this.shoppinglists = shoppinglists;
    }

    public Group(String name, List<User> users, List<Shoppinglist> shoppinglists, boolean isDefault) {
        this(name,users,shoppinglists);
        this.isDefault = isDefault;
    }

    public Group(String name, List<User> users) {
        this(name, users, new ArrayList<Shoppinglist>());
    }
    public Group(String name, List<User> users, boolean isDefault) {
        this(name,users);
    }

    public Group(String name, User user) {
        this.name = name;
        users = new ArrayList<>();
        users.add(user);
        shoppinglists = new ArrayList<>();
    }


    public Group(String name, User user, boolean isDefault) {
        this(name,user);
    }

    public String[] getUsernames(){
        List<String> result = new ArrayList<>();
        for (User user:users) {
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
        return users.toArray(new User[0]);
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
        return list;
    }

    public void removeShoppinglist(Shoppinglist shoppinglist) {
        this.shoppinglists.remove(shoppinglist);
        Save.remove(shoppinglist.getName(), name);
    }
    public boolean isDefault() {
        return isDefault;
    }

    private void redoShoppinglists(){
        for (Shoppinglist s:shoppinglists) {
            Save.remove(s.getName(), name);
            Save.save(s);
        }
    }

    public void populateShoppinglist(){
        shoppinglists = new ArrayList<>();
        shoppinglists = Read.readShoppinglist(name);
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
}

