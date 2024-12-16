package com.twix.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findOneByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.id not in :userIds")
    List<User> findNotFollowedUsers(List<Long> userIds);
}
