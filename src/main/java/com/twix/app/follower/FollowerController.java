package com.twix.app.follower;

import com.twix.app.user.User;
import com.twix.app.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/followers")
public class FollowerController {

    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    public FollowerController(UserRepository userRepository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    @PostMapping("/{userId}/follow/{followId}")
    public String followUser(@PathVariable Long userId, @PathVariable Long followId) {

        User follower = userRepository.findById(userId).orElseThrow();
        User following = userRepository.findById(followId).orElseThrow();

        Follower followRelation = new Follower();
        followRelation.setFollower(follower);
        followRelation.setFollowing(following);
        followerRepository.save(followRelation);

        return follower.getName() + " is now following " + following.getName();
    }
    @Transactional
    @DeleteMapping("/{userId}/unfollow/{followId}")
    public String unfollowUser(@PathVariable Long userId, @PathVariable Long followId) {

        followerRepository.deleteByFollowerIdAndFollowingId(userId, followId);
        return "Unfollowed successfully.";
    }
}
