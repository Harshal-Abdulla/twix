package com.twix.app.tweet;

import com.twix.app.follower.FollowerRepository;
import com.twix.app.observer.NotificationService;
import com.twix.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.twix.app.user.UserRepository;
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
    @Autowired
    private NotificationService notificationService;


    @PostMapping
    public Tweet createTweet(@RequestBody Tweet tweet) {
        tweet.setCreatedAt(LocalDateTime.now());
        Tweet savedTweet = tweetRepository.save(tweet);

        User user = tweet.getUser();
        String notification = user.getUsername() + " posted: " + tweet.getContent();
        notificationService.notifyFollowers(user, notification);

        return savedTweet;
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
}

