package com.example.user_service.serviceimpl;

import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.servicei.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserServiceI {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user){
        return userRepository.save(user);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(Long id, User user) {
        User exist = getUserById(id);
        if(exist == null){
            return null;
        }
        exist.setName(user.getName());
        exist.setEmail(user.getEmail());
        return userRepository.save(exist);
    }

    public void deleteUser(Long id){
        User exist = getUserById(id);
        userRepository.delete(exist);
    }
}