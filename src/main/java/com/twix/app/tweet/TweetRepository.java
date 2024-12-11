package com.twix.app.tweet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findByUserId(Long userId);

    List<Tweet> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds);
}
