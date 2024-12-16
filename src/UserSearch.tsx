import React, { useState } from "react";
import './UserSearch.css';
import Logo from './logo.png';


interface Props {
  onSearch: (username: string) => void;
}

const UserSearch: React.FC<Props> = ({ onSearch }) => {
  const [username, setUsername] = useState<string>("");

  const handleSearch = () => {
    if (username.trim()) {
      onSearch(username.trim());
    }
  };

  return (
    <div className="main-container">
      <div className="left-section">
        <img src={Logo} alt="Custom Logo" className="big-logo" />
      </div>
      <div className="right-section">
        <h1 className="title">Happening now</h1>
        <h2 className="subtitle">Join today.</h2>
        <div className="username-search">
          <input
            type="text"
            className="form-control"
            placeholder="Enter username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <button
            className={`btn-primary-button ${username.trim() ? "active" : ""}`}
            onClick={handleSearch}
          >
           Search
          </button>
        </div>
      </div>
    </div>
  );
};

export default UserSearch;
