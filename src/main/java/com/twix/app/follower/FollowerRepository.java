package com.twix.app.follower;

import com.twix.app.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("SELECT f.following.id FROM Follower f WHERE f.follower.id = :userId")
    List<Long> findFollowedUserIds(@Param("userId") Long userId);

    @Query("SELECT f.follower FROM Follower f WHERE f.following.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);
}
