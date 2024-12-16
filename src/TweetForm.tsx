import React, { useState } from 'react';
import './TweetForm.css';  // Import the CSS file

interface Props {
  postTweet: (tweet: string) => void;
}

const TweetForm: React.FC<Props> = ({ postTweet }) => {
  const [tweet, setTweet] = useState<string>('');

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    if (e.target.value.length <= 250) {
      setTweet(e.target.value);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (tweet.trim()) {
      postTweet(tweet);
      setTweet('');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="tweet-form">
      <textarea
        className="form-control"
        rows={3}
        value={tweet}
        onChange={handleChange}
        placeholder="What's happening?"
      ></textarea>
      <div className="character-count">{250 - tweet.length} characters remaining</div>
      <button type="submit" className="btn post-button" disabled={!tweet.trim()}>
        Post
      </button>
    </form>
  );
};

export default TweetForm;
