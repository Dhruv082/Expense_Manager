import { useEffect, useState } from "react";
import { getAnomalies, getAnomalyCount } from "../api/expenseApi";

export default function Anomalies() {

  const [data, setData] = useState<any[]>([]);
  const [count, setCount] = useState<number>(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const list = await getAnomalies();
      const cnt = await getAnomalyCount();

      setData(list.data);
      setCount(cnt.data);
    } catch {
      alert("Failed to load anomalies");
    }
  };

  return (
    <div className="container">

      <div className="card">
        <h2>Anomalies Detected: {count}</h2>

        <table>
          <thead>
            <tr>
              <th>Date</th>
              <th>Vendor</th>
              <th>Category</th>
              <th>Amount</th>
              <th>Description</th>
            </tr>
          </thead>
          <tbody>
            {data.map((e, i) => (
              <tr key={i} className="anomaly">
                <td>{e.date}</td>
                <td>{e.vendor?.name}</td>
                <td>{e.vendor?.category?.name}</td>
                <td>{e.amount}</td>
                <td>{e.description}</td>
              </tr>
            ))}
          </tbody>
        </table>

      </div>

    </div>
  );
}