import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { CartesianGrid, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import api from "../api";

export default function StudentProfile() {
  const { id } = useParams();           // the :id from /student/:id
  const [student, setStudent] = useState(null);
  const [stats, setStats] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([api.get(`/students/${id}`), api.get(`/students/${id}/stats`)])
      .then(([s, h]) => {
        setStudent(s.data);
        setStats(h.data);
      })
      .catch(() => setError("Student not found or backend is not running."));
  }, [id]);

  if (error) return <p className="error">{error}</p>;
  if (!student) return <p>Loading...</p>;

  const latest = stats.length ? stats[stats.length - 1] : null;

  return (
    <div>
      <Link to="/">&larr; Back to leaderboard</Link>
      <h1>{student.name}</h1>
      <p className="muted">
        {student.branch} &middot; Year {student.year} &middot;{" "}
        <a href={`https://leetcode.com/u/${student.leetcodeUsername}`} target="_blank" rel="noreferrer">
          @{student.leetcodeUsername}
        </a>
      </p>

      {latest && (
        <div className="cards">
          <div className="card"><span>Total</span><strong>{latest.total}</strong></div>
          <div className="card"><span>Easy</span><strong className="easy">{latest.easy}</strong></div>
          <div className="card"><span>Medium</span><strong className="medium">{latest.medium}</strong></div>
          <div className="card"><span>Hard</span><strong className="hard">{latest.hard}</strong></div>
        </div>
      )}

      <h2>Progress</h2>
      {stats.length < 2 ? (
        <p className="muted">The graph appears after at least 2 days of data.</p>
      ) : (
        <div style={{ width: "100%", height: 320 }}>
          <ResponsiveContainer>
            <LineChart data={stats}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis allowDecimals={false} />
              <Tooltip />
              <Line type="monotone" dataKey="total" stroke="#2563eb" strokeWidth={2} name="Total solved" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      )}
    </div>
  );
}