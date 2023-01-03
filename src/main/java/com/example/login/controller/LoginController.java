package com.example.login.controller;

import com.example.login.model.Role;
import com.example.login.model.User;
import com.example.login.model.request.LoginForm;
import com.example.login.model.request.SignUpForm;
import com.example.login.model.request.UserPrinciple;
import com.example.login.security.JwtResponse;
import com.example.login.security.JwtTokenProvider;
import com.example.login.service.role.RoleService;
import com.example.login.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateJwtToken(authentication);
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        User currentUser = userService.getCurrentUser();


        return ResponseEntity.ok(new JwtResponse(currentUser.getId(), jwt,
                currentUser.getUsername(),userDetails.getPassword(),userDetails.getFullName(),
                userDetails.getAddress() ,userDetails.getAuthorities()
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getPassword(), signUpRequest.getAddress());
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleService.findByName("ROLE_ADMIN")
                            .orElseThrow(() -> new RuntimeException("Fail! -> User Role not find."));
                    roles.add(adminRole);
                    break;
                default:
                    Role userRole = roleService.findByName("ROLE_USER")
                            .orElseThrow(() -> new RuntimeException("Fail! -> User Role not find."));
                    roles.add(userRole);
            }
        });
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}