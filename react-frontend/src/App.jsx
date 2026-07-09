import './App.css';
import OrderForm from './components/OrderForm';
import OrderDashboard from './components/OrderDashboard';

function App() {
  return (
    <div className="app-wrapper">
      <header className="app-header">
        <h1 className="app-title">Online Food Order Processing System</h1>
        <p className="app-subtitle">Real‑time order tracking powered by Camunda &amp; ActiveMQ</p>
      </header>
      <main className="app-content">
        <OrderForm />
        <OrderDashboard />
      </main>
    </div>
  );
}

export default App;
