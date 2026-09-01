import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // The app calls a relative /api, which is same-origin in production. In
    // development this forwards it to the Spring Boot server.
    proxy: {
      "/api": { target: "http://localhost:8080", changeOrigin: true },
    },
    host: "localhost", // Make the server accessible externally (useful for LAN access)
    port: 8000, // Change the port if necessary
    open: true, // Automatically open the browser
  },
});
