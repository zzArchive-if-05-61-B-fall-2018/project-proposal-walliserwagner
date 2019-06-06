package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String email;
    private String password;
    private int id;

    public User(String name, String email, String password, int id){
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }
}
