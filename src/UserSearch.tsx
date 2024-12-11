import React, { useState } from "react";

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
    <div className="input-group mb-3">
      <input
        type="text"
        className="form-control"
        placeholder="Enter username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <button className="btn btn-primary" onClick={handleSearch}>
        Search
      </button>
    </div>
  );
};

export default UserSearch;
