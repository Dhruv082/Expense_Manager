import { useState } from "react";
import { uploadCSV } from "../api/expenseApi";

export default function UploadCSV() {
  const [file, setFile] = useState<File | null>(null);
  const [message, setMessage] = useState("");

  const handleUpload = async () => {
    if (!file) {
      alert("Please select a file");
      return;
    }

    setMessage("Uploading...");

    try {
      await uploadCSV(file);
      setMessage("File processed successfully ✔");
    } catch (err) {
      setMessage("Upload failed ❌");
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h2>Upload CSV</h2>

        <input
          type="file"
          accept=".csv"
          onChange={(e) => setFile(e.target.files?.[0] || null)}
        />

        <br />
        <br />

        <button onClick={handleUpload}>Upload</button>

        {message && (
          <p style={{ marginTop: 20, fontWeight: "bold" }}>{message}</p>
        )}
      </div>
    </div>
  );
}
