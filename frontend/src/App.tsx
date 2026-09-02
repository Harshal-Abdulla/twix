import { useState, useEffect } from "react";
import axios from "axios";
import UserSearch from "./UserSearch";
import Feed from "./Feed";
import TweetForm from "./TweetForm";

// Same origin in production: the frontend and the API are two services in one
// Vercel project, so a relative path needs no configuration. Set VITE_API_URL
// only when running the dev server against a backend on another port.
const API_BASE_URL = import.meta.env.VITE_API_URL || "/api";

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
  const [allUsers, setAllUsers] = useState<User[]>([]);
  const [followedUsers, setFollowedUsers] = useState<User[]>([]);
  const [authError, setAuthError] = useState<string | null>(null);

  const enter = (user: User) => {
    setSearchedUser(user);
    fetchFeed(user.id);
    fetchFollowedUsers(user.id);
  };

  const searchUserByUsername = (username: string) => {
    setAuthError(null);
    axios
      .get(`${API_BASE_URL}/users/${username}`)
      .then((response) => {
        const user = response.data;
        // A username that does not exist comes back as 200 with an empty body
        // rather than a 404, so the check is on the body, not the status.
        if (user && user.id) {
          enter(user);
        } else {
          setAuthError(`No account called "${username}". Create one below.`);
        }
      })
      .catch(() => setAuthError("Could not reach the server. Try again."));
  };

  const registerUser = (username: string, name: string) => {
    setAuthError(null);
    axios
      .post(`${API_BASE_URL}/users`, { username, name })
      .then((response) => enter(response.data))
      .catch((error) => {
        // The backend distinguishes a blank field from a taken username, and
        // both arrive with a message written for a person to read.
        const message = error?.response?.data?.error;
        setAuthError(message || "Could not create the account. Try again.");
      });
  };

  const fetchFeed = (userId: number) => {
    axios
      .get(`${API_BASE_URL}/posts/feed/${userId}`)
      .then((response) => setFeed(response.data))
      .catch((error) => console.error("Error fetching feed:", error));
  };

  const fetchFollowedUsers = (userId: number) => {
    axios
      .get(`${API_BASE_URL}/users/${userId}/following`) // Adjust endpoint as needed
      .then((response) => setFollowedUsers(response.data)) // Update the followedUsers state
      .catch((error) => console.error("Error fetching followed users:", error));
  };

  const followUser = (userId: number, followId: number) => {
    axios
      .post(`${API_BASE_URL}/followers/${userId}/follow/${followId}`)
      .then(() => {
        fetchFollowedUsers(userId); // Refresh followed users
        fetchFeed(userId); // Refresh feed
      })
      .catch((error) => console.error("Error following user:", error));
  };

  const unfollowUser = (userId: number, followId: number) => {
    axios
      .delete(`${API_BASE_URL}/followers/${userId}/unfollow/${followId}`)
      .then(() => {
        fetchFollowedUsers(userId); // Refresh followed users
        fetchFeed(userId); // Refresh feed
      })
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

  useEffect(() => {
    axios
      .get(`${API_BASE_URL}/users`)
      .then((response) => setAllUsers(response.data))
      .catch((error) => console.error("Error fetching users:", error));
  }, []);

  useEffect(() => {
    if (searchedUser) {
      const intervalId = setInterval(() => {
        fetchFeed(searchedUser.id);
      }, 3000);

      return () => clearInterval(intervalId);
    }
  }, [searchedUser]);

  return (
    <div className="container mt-4">
      {!searchedUser ? (
      <UserSearch
          onSearch={searchUserByUsername}
          onRegister={registerUser}
          error={authError}
        />
      ) : (
        <>
          <h2 className="mt-4">Welcome, {searchedUser.name}</h2>
          <TweetForm postTweet={postTweet} />
          <Feed
            feed={feed}
            allUsers={allUsers}
            currentUser={searchedUser}
            followUser={followUser}
            unfollowUser={unfollowUser}
            followedUsers={followedUsers} // Pass the dynamically updated followed users
          />
        </>
      )}
    </div>
  );
}

export default App;
