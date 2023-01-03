package com.example.login.service.role;

import com.example.login.model.Role;
import com.example.login.service.IService;

import java.util.Optional;

public interface IRoleService extends IService<Role> {
    Optional<Role> findByName (String roleName);
}
