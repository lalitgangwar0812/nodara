import React, { useEffect, useState } from 'react';
import { deviceService } from '../services/deviceService';
import './DeviceList.css';

export function DeviceList() {
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadDevices();
  }, []);

  const loadDevices = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await deviceService.getDevices();
      setDevices(data);
    } catch (err) {
      setError(err.message);
      console.error('Error loading devices:', err);
    } finally {
      setLoading(false);
    }
  };

  const formatDateTime = (dateString) => {
    if (!dateString) return 'Never';
    try {
      const date = new Date(dateString);
      return date.toLocaleString();
    } catch {
      return dateString;
    }
  };

  const getStatus = (device) => {
    const value = device.status || 'STALE';
    if (value === 'ONLINE' || value === 'STALE') {
      return value;
    }

    if (!device.lastHeartbeat) {
      return 'STALE';
    }

    const lastSeen = new Date(device.lastHeartbeat).getTime();
    const ageSeconds = (Date.now() - lastSeen) / 1000;
    return ageSeconds <= 90 ? 'ONLINE' : 'STALE';
  };

  if (loading) {
    return <div className="device-list-container"><p className="loading">Loading devices...</p></div>;
  }

  if (error) {
    return (
      <div className="device-list-container">
        <div className="error">
          <p>Error loading devices: {error}</p>
          <button onClick={loadDevices} className="retry-button">Retry</button>
        </div>
      </div>
    );
  }

  return (
    <div className="device-list-container">
      <div className="device-list-header">
        <h2>Registered Endpoints</h2>
        <button onClick={loadDevices} className="refresh-button">Refresh</button>
      </div>

      {devices.length === 0 ? (
        <div className="empty-state">
          <p>No endpoints have been registered yet.</p>
          <p>Run the Nodara agent on your device to register it.</p>
        </div>
      ) : (
        <div className="device-table-wrapper">
          <table className="device-table">
            <thead>
              <tr>
                <th>Hostname</th>
                <th>Operating System</th>
                <th>Agent Version</th>
                <th>Status</th>
                <th>Registered</th>
                <th>Last Seen</th>
              </tr>
            </thead>
            <tbody>
              {devices.map((device) => (
                <tr key={device.id}>
                  <td className="hostname">{device.hostname}</td>
                  <td>{device.osName} {device.osVersion}</td>
                  <td>{device.agentVersion}</td>
                  <td>{getStatus(device)}</td>
                  <td>{formatDateTime(device.registeredAt)}</td>
                  <td>{formatDateTime(device.lastHeartbeat)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
