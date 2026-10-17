import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api";
import { BRANCHES, YEARS } from "../constants";

const TYPES = [
  { value: "total", label: "All time" },
  { value: "weekly", label: "This week" },
  { value: "today", label: "Today" },
];

export default function Leaderboard() {
  const [type, setType] = useState("total");
  const [branch, setBranch] = useState("");
  const [year, setYear] = useState("");
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // runs again whenever type, branch or year changes
  useEffect(() => {
    setLoading(true);
    setError("");
    api
      .get("/leaderboard", {
        params: { type, branch: branch || undefined, year: year || undefined },
      })
      .then((res) => setRows(res.data))
      .catch(() => setError("Could not load the leaderboard. Is the backend running?"))
      .finally(() => setLoading(false));
  }, [type, branch, year]);

  const showGain = type !== "total";

  return (
    <div>
      <h1>Leaderboard</h1>

      <div className="filters">
        <div className="tabs">
          {TYPES.map((t) => (
            <button
              key={t.value}
              className={type === t.value ? "tab active" : "tab"}
              onClick={() => setType(t.value)}
            >
              {t.label}
            </button>
          ))}
        </div>

        <select value={branch} onChange={(e) => setBranch(e.target.value)}>
          <option value="">All branches</option>
          {BRANCHES.map((b) => (
            <option key={b} value={b}>{b}</option>
          ))}
        </select>

        <select value={year} onChange={(e) => setYear(e.target.value)}>
          <option value="">All years</option>
          {YEARS.map((y) => (
            <option key={y} value={y}>Year {y}</option>
          ))}
        </select>
      </div>

      {loading && <p>Loading...</p>}
      {error && <p className="error">{error}</p>}
      {!loading && !error && rows.length === 0 && <p>No students found.</p>}

      {!loading && rows.length > 0 && (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Rank</th>
                <th>Name</th>
                <th>Branch</th>
                <th>Year</th>
                <th>Total</th>
                <th>Easy</th>
                <th>Medium</th>
                <th>Hard</th>
                {showGain && <th>{type === "today" ? "Today" : "This week"}</th>}
              </tr>
            </thead>
            <tbody>
              {rows.map((r) => (
                <tr key={r.studentId}>
                  <td>{r.rank}</td>
                  <td><Link to={`/student/${r.studentId}`}>{r.name}</Link></td>
                  <td>{r.branch}</td>
                  <td>{r.year}</td>
                  <td><strong>{r.totalSolved}</strong></td>
                  <td className="easy">{r.easy}</td>
                  <td className="medium">{r.medium}</td>
                  <td className="hard">{r.hard}</td>
                  {showGain && <td className="gain">+{r.gain}</td>}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}