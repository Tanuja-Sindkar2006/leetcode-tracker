import axios from "axios";

// one place for the backend URL
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080/api",
});

export default api;