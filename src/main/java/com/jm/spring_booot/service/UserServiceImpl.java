package com.jm.spring_booot.service;

import com.jm.spring_booot.DAO.UserDAO;
import com.jm.spring_booot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    @Override
    public void updateUser(User user) {
        if (!user.getPassword().equals(getUserByName(user.getUsername()).getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userDAO.save(user);
    }

    @Override
    public void removeUserById(long id) {
        userDAO.deleteById(id);
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userDAO.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public User getUserByName(String username) {
        return userDAO.getUserByUsername(username);
    }
}
