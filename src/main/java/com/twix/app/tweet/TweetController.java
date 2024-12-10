package com.twix.app.tweet;

import com.twix.app.follower.FollowerRepository;
import com.twix.app.user.User;
import com.twix.app.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class TweetController {
    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public Tweet createTweet(@RequestBody Tweet tweet) {
        tweet.setCreatedAt(LocalDateTime.now());
        return tweetRepository.save(tweet);
    }

    @GetMapping("/user/{userId}")
    public List<Tweet> getUserPosts(@PathVariable Long userId) {
        return tweetRepository.findByUserId(userId);
    }

    @GetMapping("/feed/{userId}")
    public List<Tweet> getUserFeed(@PathVariable Long userId) {
        List<Long> followedUserIds = followerRepository.findFollowedUserIds(userId);
        return tweetRepository.findByUserIdInOrderByCreatedAtDesc(followedUserIds);
    }

//    @PostMapping("/{userId}/notify")
//    public String notifyFollowers(@PathVariable Long userId, @RequestBody Tweet tweet) {
//        User postingUser = userRepository.findById(userId).orElseThrow();
//        List<User> followers = followerRepository.findFollowersByUserId(userId);
//
//        String notification = postingUser.getName() + " posted: " + tweet.getContent();
//        followers.forEach(follower -> System.out.println("Notification to " + follower.getName() + ": " + notification));
//
//        return "Notifications sent.";
//    }
}

