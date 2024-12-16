package com.twix.app.follower;

import com.twix.app.observer.NotificationService;
import com.twix.app.user.User;
import com.twix.app.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/followers")
public class FollowerController {

    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final NotificationService notificationService;

    public FollowerController(
            UserRepository userRepository,
            FollowerRepository followerRepository,
            NotificationService notificationService) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
        this.notificationService = notificationService;
    }

    @PostMapping("/{userId}/follow/{followId}")
    public String followUser(@PathVariable Long userId, @PathVariable Long followId) {
        User follower = userRepository.findById(userId).orElseThrow();
        User following = userRepository.findById(followId).orElseThrow();

        Follower followRelation = new Follower();
        followRelation.setFollower(follower);
        followRelation.setFollowing(following);
        followerRepository.save(followRelation);

        notificationService.followUser(follower, following);

        return follower.getUsername() + " is now following " + following.getUsername();
    }

    @Transactional
    @DeleteMapping("/{userId}/unfollow/{followId}")
    public String unfollowUser(@PathVariable Long userId, @PathVariable Long followId) {
        User follower = userRepository.findById(userId).orElseThrow();
        User following = userRepository.findById(followId).orElseThrow();

        followerRepository.deleteByFollowerIdAndFollowingId(userId, followId);

        notificationService.unfollowUser(follower, following);

        return "Unfollowed successfully.";
    }
}