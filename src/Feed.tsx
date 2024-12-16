import React, { useState } from "react";


interface User {
  id: number;
  name: string;
  username: string;
}

interface Tweet {
  id: number;
  content: string;
  createdAt: string;
  user: User;
}

interface Props {
  feed: Tweet[];
  allUsers: User[];
  currentUser: User;
  followUser: (userId: number, followId: number) => void;
  unfollowUser: (userId: number, followId: number) => void;
  followedUsers: User[];
}




const Feed: React.FC<Props> = ({
                                   feed,
                                   currentUser,
                                   followUser,
                                   unfollowUser,
                                   allUsers,
                                   followedUsers, // Directly use the prop
                               }) => {
    // Function to check if the current user is following another user
    const isFollowing = (userId: number) =>
        followedUsers && followedUsers.some((user) => user.id === userId);

    const handleFollow = (userId: number, followId: number) => {
        console.log("User ID:", userId, "Follow ID:", followId);
        followUser(userId, followId);
    };

    const handleUnfollow = (userId: number, followId: number) => {
        unfollowUser(userId, followId);
    };

    return (
        <div>
            <h3>Feed</h3>
            <ul className="list-group">
                {feed.map((tweet) => (
                    <li key={tweet.id} className="list-group-item">
                        <strong>{tweet.user.name}:</strong> {tweet.content}
                    </li>
                ))}
            </ul>

            <h3 className="mt-4">Following</h3>
            <ul className="list-group">
                {allUsers
                    .filter((user) => isFollowing(user.id)) // Dynamically reflects the new user's followed list
                    .map((user) => (
                        <li
                            key={user.id}
                            className="list-group-item d-flex justify-content-between align-items-center"
                        >
                            <span>{user.name}</span>
                            <button
                                className="btn btn-danger btn-sm"
                                onClick={() => handleUnfollow(currentUser.id, user.id)}
                            >
                                Unfollow
                            </button>
                        </li>
                    ))}
            </ul>

            <h3 className="mt-4">People You May Know</h3>
            <ul className="list-group">
                {allUsers
                    .filter((user) => user.id !== currentUser.id) // Exclude the current user
                    .filter((user) => !isFollowing(user.id)) // Exclude already followed users
                    .map((user) => (
                        <li
                            key={user.id}
                            className="list-group-item d-flex justify-content-between align-items-center"
                        >
                            <span>{user.name}</span>
                            <button
                                className="btn btn-primary btn-sm"
                                onClick={() => handleFollow(currentUser.id, user.id)}
                                disabled={isFollowing(user.id)}
                            >
                                Follow
                            </button>
                            <button
                                className="btn btn-danger btn-sm"
                                onClick={() => handleUnfollow(currentUser.id, user.id)}
                                disabled={!isFollowing(user.id)}
                            >
                                Unfollow
                            </button>
                        </li>
                    ))}
            </ul>
        </div>
    );
};

export default Feed;