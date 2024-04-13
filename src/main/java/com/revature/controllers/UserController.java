package com.revature.controllers;

import com.revature.daos.UserDAO;
import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        if (userId > 0) {
            Optional<User> u = userDAO.findById(userId);

            if (u.isEmpty()) {
                return ResponseEntity.internalServerError().build();
            }
            userDAO.save(user);
            return ResponseEntity.status(201).body(user);
        }
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
        return ResponseEntity.status(201).body(u);
    }

    // get user by username and password
    @GetMapping("/find/{up}")
    public ResponseEntity<User> getUserByUsernameAndPassword(@PathVariable String up, @RequestBody String password) {
        User u = userDAO.findByUsernameAndPassword(up, password);

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
