package org.kabbee.service;

import org.kabbee.entity.User;

public interface UserService {

    public User addUser(User user);
    public User getUser(String username, String password);
    public User editUser(String password);
}
