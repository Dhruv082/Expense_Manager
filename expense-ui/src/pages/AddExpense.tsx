import { useState } from "react";
import { createExpense } from "../api/expenseApi";

export default function AddExpense() {
  const [form, setForm] = useState({
    amount: "",
    date: "",
    vendorName: "",
    description: "",
  });

  const [response, setResponse] = useState<any>(null);
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setResponse(null);

    try {
      const res = await createExpense({
        amount: Number(form.amount),
        date: form.date,
        vendorName: form.vendorName,
        description: form.description,
      });

      setResponse(res.data);
    } catch (err) {
      alert("Error saving expense");
    }

    setLoading(false);
  };

  return (
    <div className="container">
      <h2>Add Expense</h2>
      <div className="card">
        <div className="form-wrapper">
          <form
            onSubmit={handleSubmit}
            style={{
              display: "flex",
              flexDirection: "column",
              gap: 10,
            }}
          >
            <input
              name="amount"
              placeholder="Amount"
              value={form.amount}
              onChange={handleChange}
              required
            />

            <input
              placeholder="Date"
              type="date"
              name="date"
              value={form.date}
              onChange={handleChange}
              required
            />

            <input
              name="vendorName"
              placeholder="Vendor Name (e.g. Swiggy)"
              value={form.vendorName}
              onChange={handleChange}
              required
            />

            <input
              name="description"
              placeholder="Description"
              value={form.description}
              onChange={handleChange}
            />

            <button type="submit" disabled={loading}>
              {loading ? "Saving..." : "Save Expense"}
            </button>
          </form>
        </div>
      </div>

      {response && (
        <div className="card">
          <h3>Processed Result</h3>

          <pre
            style={{
              background: "#111",
              color: "#0f0",
              padding: 15,
              borderRadius: 6,
              overflowX: "auto",
            }}
          >
            {JSON.stringify(response, null, 2)}
          </pre>
        </div>
      )}
    </div>
  );
}
