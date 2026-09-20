package interfaces;
public interface Authenticatable {
    boolean authenticate(String password);
    String getId();
    String getRole();
}
