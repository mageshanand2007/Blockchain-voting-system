package services;

import model.User;
import model.Admin;
import model.Voter;
import exceptions.VotingException;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private Map<String, User> users;
    private User loggedInUser;

    public AuthenticationService() {
        users = new HashMap<>();
        // Add Demo Data
        users.put("admin", new Admin("admin", "admin", "admin123"));
        users.put("VOTER001", new Voter("VOTER001", "Alice", "password"));
        users.put("VOTER002", new Voter("VOTER002", "Bob", "password"));
        users.put("VOTER003", new Voter("VOTER003", "Charlie", "password"));
    }

    public User login(String id, String password) throws VotingException {
        User user = users.get(id);
        if (user == null || !user.authenticate(password)) {
            throw new VotingException("Invalid ID or password.");
        }
        loggedInUser = user;
        return user;
    }

    public void registerVoter(String id, String username, String password) throws VotingException {
        if (users.containsKey(id)) {
            throw new VotingException("Voter ID already exists.");
        }
        users.put(id, new Voter(id, username, password));
    }

    public void logout() {
        loggedInUser = null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Map<String, User> getUsers() {
        return users;
    }
}
