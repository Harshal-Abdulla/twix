import React, { useState } from "react";
import './UserSearch.css';
import Logo from './logo.png';

interface Props {
  onSearch: (username: string) => void;
  onRegister: (username: string, name: string) => void;
  error: string | null;
}

const UserSearch: React.FC<Props> = ({ onSearch, onRegister, error }) => {
  const [username, setUsername] = useState<string>("");
  const [name, setName] = useState<string>("");
  // Sign in is the default because most visits are a return visit. Creating an
  // account is one click away rather than a separate screen.
  const [mode, setMode] = useState<"signin" | "signup">("signin");

  const submit = (e: React.FormEvent) => {
    e.preventDefault();
    const u = username.trim();
    if (!u) return;
    if (mode === "signin") {
      onSearch(u);
    } else {
      const n = name.trim();
      if (!n) return;
      onRegister(u, n);
    }
  };

  const ready = mode === "signin" ? username.trim() : username.trim() && name.trim();

  return (
    <div className="main-container">
      <div className="left-section">
        <img src={Logo} alt="Twix" className="big-logo" />
      </div>
      <div className="right-section">
        <h1 className="title">Happening now</h1>
        <h2 className="subtitle">Join today.</h2>

        {/* A form rather than a bare button, so Enter submits. Typing a name
            and pressing Enter is what people actually do here. */}
        <form className="username-search" onSubmit={submit}>
          <input
            type="text"
            className="form-control"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            autoComplete="username"
          />

          {mode === "signup" && (
            <input
              type="text"
              className="form-control"
              placeholder="Display name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              autoComplete="name"
            />
          )}

          <button
            type="submit"
            className={`btn-primary-button ${ready ? "active" : ""}`}
            disabled={!ready}
          >
            {mode === "signin" ? "Sign in" : "Create account"}
          </button>
        </form>

        {/* The message is announced, because someone using a screen reader
            needs to know the sign-in failed without going looking for it. */}
        {error && (
          <p className="auth-error" role="alert">
            {error}
          </p>
        )}

        <p className="auth-switch">
          {mode === "signin" ? (
            <>
              Don't have an account?{" "}
              <button type="button" onClick={() => setMode("signup")}>
                Create one
              </button>
            </>
          ) : (
            <>
              Already have one?{" "}
              <button type="button" onClick={() => setMode("signin")}>
                Sign in
              </button>
            </>
          )}
        </p>
      </div>
    </div>
  );
};

export default UserSearch;
