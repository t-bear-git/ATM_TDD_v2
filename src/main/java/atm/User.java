package atm;

public class User {

    String name;
    int userId;

    public User(String name, int id) {
        this.name = name;
        this.userId = id;
    }

    public String getName() {
        return name;
    }
}
