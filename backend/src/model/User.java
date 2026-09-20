package model;
import interfaces.Authenticatable;
public abstract class User implements Authenticatable {
    protected String id;
    protected String username;
    protected String passwordHash;
    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.passwordHash = password;
    }
    @Override public boolean authenticate(String password) { return this.passwordHash.equals(password); }
    @Override public String getId() { return id; }
    public String getUsername() { return username; }
    public abstract String getRole();
}
