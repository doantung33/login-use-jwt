package com.example.login.service.user;

import com.example.login.model.User;
import com.example.login.service.IService;

public interface IUserService extends IService<User> {

    boolean existsByUsername(String username);

    User findUserByUserName(String username);

    User getCurrentUser();
}
