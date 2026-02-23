import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import AddExpense from "./pages/AddExpense";
import UploadCSV from "./pages/UploadCSV";
import Dashboard from "./pages/Dashboard";
import Anomalies from "./pages/Anomalies";

export default function App() {
  return (
    <BrowserRouter>
      <div className="page">
        <Navbar />

        <main className="page-content">
          <Routes>
            <Route path="/" element={<AddExpense />} />
            <Route path="/upload" element={<UploadCSV />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/anomalies" element={<Anomalies />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}
