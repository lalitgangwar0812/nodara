import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { DeviceList } from './components/DeviceList';
import './styles.css';

function App() {
  return (
    <main className="shell">
      <section className="hero" aria-labelledby="product-name">
        <p className="eyebrow">Platform foundation</p>
        <h1 id="product-name">Nodara</h1>
        <p className="tagline">IT Infrastructure Visibility &amp; Automation Platform</p>
      </section>
      <DeviceList />
    </main>
  );
}

createRoot(document.getElementById('root')).render(<StrictMode><App /></StrictMode>);
