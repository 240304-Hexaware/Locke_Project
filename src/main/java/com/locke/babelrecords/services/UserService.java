package com.locke.babelrecords.services;

import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRep;

    @Autowired
    public UserService(UserRepository userRep) {
        this.userRep = userRep;
    }

    public User findById(String id) throws ItemNotFoundException{
        return userRep.findById(id).orElseThrow(() -> new ItemNotFoundException("Id not found"));
    }

    public User findByUserName(String userName) throws ItemNotFoundException{
        return userRep.findByUserName(userName).orElseThrow(() -> new ItemNotFoundException("userName not found"));
    }
    public void insertUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRep.save(user);
    }
}
