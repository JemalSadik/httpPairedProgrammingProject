package com.revature.controllers;

import com.revature.daos.UserDAO;
import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    UserDAO userDAO;

    @Autowired
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // add a user
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User u = userDAO.save(user);

        if (u == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.status(201).body(u);
    }

    // update a user
    // patching partial change is not working
    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable int userId) {
        Optional<User> existingUser = userDAO.findById(userId);

        if (existingUser.isPresent()) {
            User u = existingUser.get();
            // update username if not empty or null
            if (!Objects.equals(user.getUsername(), "") && user.getUsername() != null) {
                u.setUsername(user.getUsername());
            }
            // update password if not empty or null
            if (!Objects.equals(user.getPassword(), "") && user.getPassword() != null) {
                u.setPassword(user.getPassword());
            }
            // Save updated user and return response
            userDAO.save(u);
            return ResponseEntity.ok(u);
        }
        // user at userId was not found
        return ResponseEntity.notFound().build();
    }

    // get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userDAO.findAll();
        return ResponseEntity.ok(users);
    }

    // get user by name
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User u = userDAO.findByUsername(username);

        if (u == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(u);
    }

    // get user by username and password
    @GetMapping("/find/{username}")
    public ResponseEntity<User> getUserByUsernameAndPassword(@PathVariable String username, @RequestBody String password) {
        User u = userDAO.findByUsernameAndPassword(username, password);

        if (u == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.status(201).body(u);
    }

    // delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        if (userId > 0) {
            Optional<User> u = userDAO.findById(userId);
            if (u.isPresent()) {
                userDAO.deleteById(userId);
                return ResponseEntity.ok("Deleted user");
            }
        }
        return ResponseEntity.notFound().build();
    }

}
