package org.kabbee.service.impl;

import org.kabbee.Repository.UserRepository;
import org.kabbee.entity.User;
import org.kabbee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<String> getUsersEmail() {
        String email;
        List<String> emails = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for(User user: users){
            email = user.getEmail();
            emails.add(email);
        }

        return emails;

    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }

    @Override
    public void deleteUser(String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        userRepository.delete(user);
    }
}
