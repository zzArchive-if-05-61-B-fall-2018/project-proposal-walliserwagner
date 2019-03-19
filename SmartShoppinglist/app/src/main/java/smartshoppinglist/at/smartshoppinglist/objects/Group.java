package smartshoppinglist.at.smartshoppinglist.objects;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private List<String> users;
    private List<Shoppinglist> shoppinglists;

    public Group(String name, List<String> users, List<Shoppinglist> shoppinglists) {
        this.name = name;
        this.users = users;
        this.shoppinglists = shoppinglists;
    }

    public Group(String name, List<String> users) {
        this.name = name;
        this.users = users;
        this.shoppinglists = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getUsers() {
        return users.toArray(new String[0]);
    }

    public void addUsers(String user) {
        this.users.add(user);
    }

    public Shoppinglist[] getShoppinglists() {
        return shoppinglists.toArray(new Shoppinglist[0]);
    }

    public void setShoppinglists(Shoppinglist shoppinglists) {
        this.shoppinglists.add(shoppinglists);
    }
}

