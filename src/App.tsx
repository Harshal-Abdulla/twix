import React, { useState, useEffect } from "react";
import axios from "axios";
import UserSearch from "./UserSearch";
import Feed from "./Feed";
import TweetForm from "./TweetForm";

const API_BASE_URL = "http://localhost:8080/api";

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

function App() {
  const [searchedUser, setSearchedUser] = useState<User | null>(null);
  const [feed, setFeed] = useState<Tweet[]>([]);

  const searchUserByUsername = (username: string) => {
    axios
      .get(`${API_BASE_URL}/users/${username}`)
      .then((response) => {
        const user = response.data;
        if (user) {
          setSearchedUser(user);
          fetchFeed(user.id);
        } else {
          alert("User not found!");
        }
      })
      .catch((error) => console.error("Error searching user:", error));
  };

  const fetchFeed = (userId: number) => {
    axios
      .get(`${API_BASE_URL}/posts/feed/${userId}`)
      .then((response) => setFeed(response.data))
      .catch((error) => console.error("Error fetching feed:", error));
  };

  const followUser = (userId: number, followId: number) => {
    axios
      .post(`${API_BASE_URL}/followers/${userId}/follow/${followId}`)
      .then(() => fetchFeed(userId))
      .catch((error) => console.error("Error following user:", error));
  };

  const unfollowUser = (userId: number, followId: number) => {
    axios
      .delete(`${API_BASE_URL}/followers/${userId}/unfollow/${followId}`)
      .then(() => fetchFeed(userId))
      .catch((error) => console.error("Error unfollowing user:", error));
  };

  const postTweet = (tweet: string) => {
    if (!searchedUser) return;
    axios
      .post(`${API_BASE_URL}/posts`, {
        user: { id: searchedUser.id },
        content: tweet,
      })
      .then(() => fetchFeed(searchedUser.id))
      .catch((error) => console.error("Error posting tweet:", error));
  };

  // Automatically refresh the feed every 3 seconds when a user is selected
  useEffect(() => {
    if (searchedUser) {
      const intervalId = setInterval(() => {
        fetchFeed(searchedUser.id);
      }, 3000); // Fetch every 3 seconds

      // Cleanup interval on component unmount or user change
      return () => clearInterval(intervalId);
    }
  }, [searchedUser]);

  return (
    <div className="container mt-4">
      <h1 className="text-center">Twitter Clone</h1>
      <UserSearch onSearch={searchUserByUsername} />
      {searchedUser && (
        <>
          <h2 className="mt-4">Welcome, {searchedUser.name}</h2>
          <TweetForm postTweet={postTweet} />
          <Feed
            feed={feed}
            currentUser={searchedUser}
            followUser={followUser}
            unfollowUser={unfollowUser}
          />
        </>
      )}
    </div>
  );
}

export default App;
