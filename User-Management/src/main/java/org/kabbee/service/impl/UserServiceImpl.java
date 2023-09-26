package org.kabbee.service.impl;

import org.kabbee.Repository.UserRepository;
import org.kabbee.entity.User;
import org.kabbee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUser(String username, String password) {
        return userRepository.findUserByUsernameAndPassword(username, password).orElse(null);
    }

    @Override
    public User editUser(String password) {
        return userRepository.findUserByPassword(password).orElse(null);
    }
}
