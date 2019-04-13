package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;

public class Group implements Serializable {
    private String name;
    private List<User> users;
    private List<Shoppinglist> shoppinglists;

    public Group(String name, List<User> users, List<Shoppinglist> shoppinglists) {
        this.name = name;
        this.users = users;
        this.shoppinglists = shoppinglists;
    }

    public Group(String name, List<User> users) {
        this.name = name;
        this.users = users;
        this.shoppinglists = new ArrayList<>();
    }
    public Group(String name, User user) {
        this.name = name;
        this.users = new ArrayList<>();
        this.users.add(user);
        this.shoppinglists = new ArrayList<>();
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

    public Shoppinglist[] getShoppinglists() {
        return shoppinglists.toArray(new Shoppinglist[0]);
    }

    public void addShoppinglist(Shoppinglist shoppinglist) {
        this.shoppinglists.add(shoppinglist);
    }
    public void removeShoppinglist(Shoppinglist shoppinglist) {
        this.shoppinglists.remove(shoppinglists);
    }
}

