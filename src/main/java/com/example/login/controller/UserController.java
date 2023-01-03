package com.example.login.controller;


import com.example.login.model.User;
import com.example.login.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin( "*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/profile/{id}")
    public ResponseEntity<?> showProFile(@PathVariable Long id){
        if(userService.findById(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<User> editProFile(@PathVariable Long id, @RequestBody User user){
        user.setId(id);
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }



}
