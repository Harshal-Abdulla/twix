import React from "react";

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
  currentUser: User;
  followUser: (userId: number, followId: number) => void;
  unfollowUser: (userId: number, followId: number) => void;
}

const Feed: React.FC<Props> = ({
  feed,
  currentUser,
  followUser,
  unfollowUser,
}) => {
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
      <h3 className="mt-4">People You May Know</h3>
      <ul className="list-group">
        {feed
          .map((tweet) => tweet.user)
          .filter(
            (user, index, self) =>
              self.findIndex((u) => u.id === user.id) === index
          )
          .map((user) => (
            <li
              key={user.id}
              className="list-group-item d-flex justify-content-between align-items-center"
            >
              <span>{user.name}</span>
              <button
                className="btn btn-primary btn-sm"
                onClick={() => followUser(currentUser.id, user.id)}
              >
                Follow
              </button>
              <button
                className="btn btn-danger btn-sm"
                onClick={() => unfollowUser(currentUser.id, user.id)}
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
