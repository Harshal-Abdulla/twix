import React, { useState } from "react";

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
    <form onSubmit={handleSubmit} className="mb-4">
      <div className="form-group">
        <textarea
          className="form-control"
          rows={3}
          value={tweet}
          onChange={(e) => setTweet(e.target.value)}
          placeholder="What's happening?"
        ></textarea>
      </div>
      <button type="submit" className="btn btn-primary mt-2">
        Tweet
      </button>
    </form>
  );
};

export default TweetForm;
