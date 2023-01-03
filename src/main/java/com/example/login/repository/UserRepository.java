package com.example.login.repository;

import com.example.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
    User findByUsername( String username);
    User findUserById(Long id);
    boolean existsByUsername(String username);
}
