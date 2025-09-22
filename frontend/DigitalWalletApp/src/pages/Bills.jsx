import { useState } from "react";
import api from "../services/api";
import "../index.css";

function Bills() {
  const [form, setForm] = useState({ biller: "", account: "", amount: "" });

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("token");
      await api.post(
        "/bills/pay",
        {
          biller: form.biller,
          account: form.account,
          amount: parseFloat(form.amount),
        },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      alert("Bill paid successfully");
      setForm({ biller: "", account: "", amount: "" });
    } catch {
      alert("Bill payment failed");
    }
  };

  return (
    <div className="center-col">
      <div
        className="auth-card"
        style={{ padding: "28px", width: "420px", textAlign: "center" }}
      >
        <h2 className="h-title">Pay Bills</h2>
        <p className="h-sub">Settle your utility and service bills</p>

        <form onSubmit={handleSubmit} style={{ marginTop: "20px" }}>
          <div className="form-row">
            <input
              className="input"
              type="text"
              name="biller"
              placeholder="Biller Name"
              value={form.biller}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-row">
            <input
              className="input"
              type="text"
              name="account"
              placeholder="Account/Ref Number"
              value={form.account}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-row">
            <input
              className="input"
              type="number"
              name="amount"
              placeholder="Amount"
              value={form.amount}
              onChange={handleChange}
              required
            />
          </div>

          <button className="primary-action" type="submit">
            Pay Bill
          </button>
        </form>
      </div>
    </div>
  );
}

export default Bills;
