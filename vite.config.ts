import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: "localhost", // Make the server accessible externally (useful for LAN access)
    port: 8000, // Change the port if necessary
    open: true, // Automatically open the browser
  },
});
