package com.twix.app.observer;

import com.twix.app.user.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {
    private final Map<Long, UserSubject> userSubjects = new HashMap<>();

    public void createSubjectForUser(User user) {
        userSubjects.putIfAbsent(user.getId(), new UserSubject());
    }

    public void followUser(User follower, User following) {
        UserSubject subject = userSubjects.get(following.getId());
        if (subject == null) {
            subject = new UserSubject();
            userSubjects.put(following.getId(), subject);
        }
        subject.addObserver(follower);
    }

    public void unfollowUser(User follower, User following) {
        UserSubject subject = userSubjects.get(following.getId());
        if (subject == null) {
            return;
        }
        subject.removeObserver(follower);

    }

    public void notifyFollowers(User user, String message) {
        UserSubject subject = userSubjects.get(user.getId());
        if (subject == null) {
            return;
        }
        subject.notifyObservers(message);

    }
}