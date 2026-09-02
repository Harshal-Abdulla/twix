package com.twix.app.user;

import com.twix.app.observer.NotificationService;
import com.twix.app.follower.FollowerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public User getUserByUsername(@PathVariable String username) {
        return userRepository.findOneByUsername(username);
    }

    /**
     * Registers a user.
     *
     * The two failures a sign-up form has to be able to tell apart are a blank
     * field and a name somebody already has, so they get distinct statuses and
     * a message the form can show as-is. Previously neither was handled: blank
     * strings saved happily, because the columns reject null rather than empty,
     * and a duplicate username surfaced the unique-constraint violation as a
     * 500, which tells the person nothing about what to do next.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        String username = user.getUsername() == null ? "" : user.getUsername().trim();
        String name = user.getName() == null ? "" : user.getName().trim();

        if (username.isEmpty() || name.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Both a username and a display name are required."));
        }

        if (userRepository.findOneByUsername(username) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "That username is taken."));
        }

        // Stored trimmed, so " harshal" and "harshal" cannot become two accounts
        // that look identical on screen.
        user.setUsername(username);
        user.setName(name);

        User savedUser = userRepository.save(user);
        notificationService.createSubjectForUser(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/{id}/not-following")
    public List<User> getAllNotFollowingUsers(@PathVariable Long id) {
        List<Long> userIds = followerRepository.findFollowedUserIds(id);
        return userRepository.findNotFollowedUsers(userIds);
    }

    @GetMapping("/{id}/following")
    public List<User> getAllFollowingUsers(@PathVariable Long id) {
        List<Long> userIds = followerRepository.findFollowedUserIds(id);
        return userRepository.findAllById(userIds);
    }
}