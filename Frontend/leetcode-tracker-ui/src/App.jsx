import { Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar.jsx";
import Leaderboard from "./pages/Leaderboard.jsx";
import StudentProfile from "./pages/StudentProfile.jsx";
import AddStudent from "./pages/AddStudent.jsx";

export default function App() {
  return (
    <>
      <Navbar />
      <main className="container">
        <Routes>
          <Route path="/" element={<Leaderboard />} />
          <Route path="/student/:id" element={<StudentProfile />} />
          <Route path="/register" element={<AddStudent />} />
          <Route path="*" element={<p>Page not found.</p>} />
        </Routes>
      </main>
    </>
  );
}