import { useEffect, useState } from "react";
import { getMonthlyTotals, getTopVendors } from "../api/expenseApi";

export default function Dashboard() {
  const [monthlyTotals, setMonthlyTotals] = useState<any[]>([]);
  const [topVendors, setTopVendors] = useState<any[]>([]);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const monthlyRes = await getMonthlyTotals();
      const vendorRes = await getTopVendors();

      setMonthlyTotals(monthlyRes.data);
      setTopVendors(vendorRes.data);
    } catch (err) {
      alert("Error loading dashboard data");
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h2>Monthly Category Totals</h2>

        <table>
          <thead>
            <tr>
              <th>Year</th>
              <th>Month</th>
              <th>Category</th>
              <th>Total</th>
            </tr>
          </thead>
          <tbody>
            {monthlyTotals.map((item, index) => (
              <tr key={index}>
                <td>{item.year}</td>
                <td>{item.month}</td>
                <td>{item.category}</td>
                <td>{item.total}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="card">
        <h2>Top 5 Vendors</h2>

        <table>
          <thead>
            <tr>
              <th>Vendor</th>
              <th>Total Spend</th>
            </tr>
          </thead>
          <tbody>
            {topVendors.map((vendor, index) => (
              <tr key={index}>
                <td>{vendor.vendorName}</td>
                <td>{vendor.total}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
