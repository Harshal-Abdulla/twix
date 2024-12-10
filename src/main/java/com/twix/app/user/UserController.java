package com.twix.app.user;

import com.twix.app.follower.FollowerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    public UserController(UserRepository userRepository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List a = userRepository.findAll();
        return a;
    }

    @GetMapping("/{username}")
    public Optional<User> getUserByUsername(@PathVariable String username) {
        Optional<User> a = userRepository.findOneByUsername(username);
        return a;
    }

}
