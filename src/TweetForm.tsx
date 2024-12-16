import React, { useState } from "react";
import "./TweetForm.css";

interface Props {
  postTweet: (tweet: string) => void;
}

const TweetForm: React.FC<Props> = ({ postTweet }) => {
  const [tweet, setTweet] = useState<string>("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (tweet.trim()) {
      postTweet(tweet);
      setTweet("");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="tweet-form">
      <textarea
        className="form-control"
        rows={3}
        value={tweet}
        onChange={(e) => setTweet(e.target.value)}
        placeholder="What's happening?"
      ></textarea>
      <div className="character-count">
        {250 - tweet.length} characters remaining
      </div>
      <button
        type="submit"
        className={`post-button ${tweet.trim() ? "active" : ""}`}
        disabled={!tweet.trim()}
      >
        Post
      </button>
    </form>
  );
};

export default TweetForm;
