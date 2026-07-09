import React, { useEffect, useState } from "react";

const OrderDashboard = () => {
    const [orders, setOrders] = useState([]);

    const fetchOrders = async () => {
        try {
            const res = await fetch("http://localhost:8081/api/orders");
            if (res.ok) {
                const data = await res.json();
                setOrders(data);
            }
        } catch (_) { }
    };

    useEffect(() => {
        fetchOrders();                     // initial load
        const interval = setInterval(fetchOrders, 2000);
        return () => clearInterval(interval);
    }, []);

    const statusBadge = (status) => {
        const colors = {
            PLACED: "badge-grey",
            PAYMENT: "badge-blue",
            KITCHEN: "badge-orange",
            DELIVERY: "badge-purple",
            DELIVERED: "badge-green",
            CANCELLED: "badge-red",
        };
        const defaultColor = "badge-grey";
        const badgeClass = colors[status] || defaultColor;
        return <span className={`badge ${badgeClass}`}>{status}</span>;
    };

    return (
        <section className="dashboard-section">
            <div className="card">
                <h2>Orders Dashboard</h2>
                <table className="orders-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Customer</th>
                            <th>Item</th>
                            <th>Amount</th>
                            <th>Status</th>
                            <th>Created At</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orders.map((o) => (
                            <tr key={o.id}>
                                <td>{o.id}</td>
                                <td>{o.customerName}</td>
                                <td>{o.item}</td>
                                <td>{o.amount}</td>
                                <td>{statusBadge(o.status)}</td>
                                <td>{new Date(o.createdAt).toLocaleString()}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </section>
    );
};

export default OrderDashboard;
