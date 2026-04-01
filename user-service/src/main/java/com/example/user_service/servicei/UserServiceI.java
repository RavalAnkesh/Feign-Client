package com.example.user_service.servicei;

import com.example.user_service.entity.User;

import java.util.List;

public interface UserServiceI {
    User createUser(User user);
    List<User> getAllUser();
    User getUserById(Long id);
    User updateUser(Long id,User user);
    void deleteUser(Long id);
}
