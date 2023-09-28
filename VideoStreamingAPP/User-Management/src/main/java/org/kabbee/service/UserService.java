package org.kabbee.service;

import org.kabbee.entity.User;

import java.util.List;

public interface UserService {

    public User addUser(User user);
    public User getUser(String username, String password);
    public User editUser(String password);
    public List<String> getUsersEmail();
    public User getUserByEmail(String email);

    public void deleteUser(String email);
}
