import React, { useState } from "react";

const OrderForm = () => {
  const [customerName, setCustomerName] = useState("");
  const [item, setItem] = useState("");
  const [amount, setAmount] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = {
      customerName,
      item,
      amount: parseFloat(amount)
    };
    try {
      const response = await fetch("http://localhost:8081/api/orders", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      if (!response.ok) {
        const err = await response.text();
        setMessage(`Error: ${response.status} – ${err}`);
        return;
      }
      const data = await response.json();
      setMessage(`✅ Order placed! ID: ${data.id}`);
      // clear form
      setCustomerName("");
      setItem("");
      setAmount("");
    } catch (err) {
      setMessage(`Error: ${err.message}`);
    }
  };

  return (
    <section className="order-section">
      <div className="card order-card">
        <h2 className="section-title">Place a New Order</h2>
        <form onSubmit={handleSubmit} className="order-form">
          <div className="form-group">
            <label>Customer Name</label>
            <input
              type="text"
              value={customerName}
              onChange={(e) => setCustomerName(e.target.value)}
              placeholder="John Doe"
              required
            />
          </div>
          <div className="form-group">
            <label>Item</label>
            <input
              type="text"
              value={item}
              onChange={(e) => setItem(e.target.value)}
              placeholder="Pizza"
              required
            />
          </div>
          <div className="form-group">
            <label>Amount</label>
            <input
              type="number"
              step="0.01"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder="12.99"
              required
            />
          </div>
          <button type="submit" className="btn btn-primary">Place Order</button>
        </form>
        {message && <div className={`toast ${message.startsWith('✅') ? 'success' : 'error'}`}>{message}</div>}
      </div>
    </section>
  );
};

export default OrderForm;
