package com.twix.app.user;

import com.twix.app.observer.NotificationService;
import com.twix.app.follower.FollowerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final NotificationService notificationService;

    public UserController(
            UserRepository userRepository,
            FollowerRepository followerRepository,
            NotificationService notificationService) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{username}")
    public Optional<User> getUserByUsername(@PathVariable String username) {
        return userRepository.findOneByUsername(username);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        notificationService.createSubjectForUser(savedUser);
        return savedUser;
    }
}