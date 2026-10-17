import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import { BRANCHES, YEARS } from "../constants";

export default function AddStudent() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: "", branch: "CSE", year: 1, leetcodeUsername: "" });
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  // one handler for all inputs: uses the input's "name" attribute
  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();               // stop the browser reloading the page
    setError("");
    setSubmitting(true);
    try {
      await api.post("/students", { ...form, year: Number(form.year) });
      navigate("/");                  // go to the leaderboard
    } catch (err) {
      const data = err.response?.data;
      const fieldMessages = data?.errors ? Object.values(data.errors).join(", ") : "";
      setError(fieldMessages || data?.message || "Something went wrong. Is the backend running?");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div>
      <h1>Register</h1>
      <p className="muted">Enter your exact LeetCode username. We check that it exists.</p>

      <form onSubmit={handleSubmit} className="form">
        <label>
          Full name
          <input name="name" value={form.name} onChange={handleChange} required />
        </label>

        <label>
          Branch
          <select name="branch" value={form.branch} onChange={handleChange}>
            {BRANCHES.map((b) => (
              <option key={b} value={b}>{b}</option>
            ))}
          </select>
        </label>

        <label>
          Year
          <select name="year" value={form.year} onChange={handleChange}>
            {YEARS.map((y) => (
              <option key={y} value={y}>Year {y}</option>
            ))}
          </select>
        </label>

        <label>
          LeetCode username
          <input name="leetcodeUsername" value={form.leetcodeUsername} onChange={handleChange} required />
        </label>

        {error && <p className="error">{error}</p>}

        <button type="submit" className="primary" disabled={submitting}>
          {submitting ? "Checking LeetCode..." : "Register"}
        </button>
      </form>
    </div>
  );
}