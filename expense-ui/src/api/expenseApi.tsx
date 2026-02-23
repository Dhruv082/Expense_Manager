import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/expenses",
  headers: {
    "Content-Type": "application/json",
  },
});

// ---------- Expense ----------
export const createExpense = (data: {
  amount: number;
  date: string;
  description: string;
  vendorName: string;
}) => api.post("", data);

// ---------- CSV Upload ----------
export const uploadCSV = (file: File) => {
  const formData = new FormData();
  formData.append("file", file);

  return api.post("/upload", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};

// ---------- Dashboard ----------
export const getMonthlyTotals = () => api.get("/dashboard/monthly-totals");

export const getTopVendors = () => api.get("/dashboard/top-vendors");

export const getAnomalies = () => api.get("/dashboard/anomalies");

export const getAnomalyCount = () => api.get("/dashboard/anomalies/count");
