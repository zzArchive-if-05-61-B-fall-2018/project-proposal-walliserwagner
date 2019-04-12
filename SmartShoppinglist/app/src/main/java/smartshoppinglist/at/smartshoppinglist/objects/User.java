package smartshoppinglist.at.smartshoppinglist.objects;

public class User {

    private String name;
    private String email;
    private int id;

    public User(String name, String email, int id){
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }
}
