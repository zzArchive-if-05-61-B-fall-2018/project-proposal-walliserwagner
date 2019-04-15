package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.localsave.Read;

public class Group implements Serializable {
    private String name;
    private List<User> users;
    private transient List<Shoppinglist> shoppinglists;
    private List<String> shoppinglistnames;

    private boolean isDefault = false;

    public Group(String name, List<User> users, List<Shoppinglist> shoppinglists) {
        this.name = name;
        this.users = users;
        this.shoppinglists = shoppinglists;
        populateShoppinglists();
    }

    public Group(String name, List<User> users, List<Shoppinglist> shoppinglists, boolean isDefault) {
        this(name,users,shoppinglists);
        this.isDefault = isDefault;
    }

    public Group(String name, List<User> users) {
        new Group(name, users, new ArrayList<Shoppinglist>());
    }
    public Group(String name, List<User> users, boolean isDefault) {
        this(name,users);
    }
    public Group(String name, User user) {
        List<User> users = new ArrayList<User>();
        users.add(user);
        new Group(name, users, new ArrayList<Shoppinglist>());
    }

    private void populateShoppinglists(){
        shoppinglistnames = new ArrayList<>();
        for (String name:shoppinglistnames) {
            shoppinglists.add(Read.readShoppinglist(name));
        }
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

    public void setName(String name) {
        this.name = name;
    }

    public User[] getUsers() {
        return users.toArray(new User[0]);
    }

    public void addUsers(User user) {
        this.users.add(user);
    }

    public Shoppinglist[] getShoppinglists()
    {
        if(shoppinglists == null) shoppinglists = new ArrayList<>();
        return shoppinglists.toArray(new Shoppinglist[0]);
    }

    /*public void addShoppinglist(Shoppinglist shoppinglist) {
        this.shoppinglists.add(shoppinglist);
    }*/

    public Shoppinglist createList(String shoppinglistname)
    {
        if(shoppinglists == null) shoppinglists = new ArrayList<>();
        Shoppinglist list = new Shoppinglist(shoppinglistname,this);
        this.shoppinglists.add(list);
        this.shoppinglistnames.add(shoppinglistname);
        return list;
    }

    public void removeShoppinglist(Shoppinglist shoppinglist) {
        if(shoppinglists == null) shoppinglists = new ArrayList<>();
        this.shoppinglists.remove(shoppinglists);
    }
    public boolean isDefault() {
        return isDefault;
    }
}

