import { NavLink } from "react-router-dom";

export default function Navbar() {
  const linkStyle = ({ isActive }: any) => ({
    color: isActive ? "#60a5fa" : "white",
    textDecoration: "none",
    fontWeight: isActive ? "bold" : "normal",
  });

  return (
    <div
      style={{
        background: "#1e293b",
        padding: "12px 20px",
        display: "flex",
        gap: "20px",
        borderRadius: "0 0 8px 8px",
      }}
    >
      <NavLink style={linkStyle} to="/">
        Add Expense
      </NavLink>
      <NavLink style={linkStyle} to="/upload">
        Upload CSV
      </NavLink>
      <NavLink style={linkStyle} to="/dashboard">
        Dashboard
      </NavLink>
      <NavLink style={linkStyle} to="/anomalies">
        Anomalies
      </NavLink>
    </div>
  );
}
