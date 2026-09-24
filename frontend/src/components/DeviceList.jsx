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

  const formatUptime = (seconds) => {
    if (seconds === null || seconds === undefined || seconds === '') {
      return 'No telemetry';
    }

    const totalSeconds = Number(seconds);
    if (!Number.isFinite(totalSeconds) || totalSeconds < 0) {
      return 'No telemetry';
    }

    const days = Math.floor(totalSeconds / 86400);
    const hours = Math.floor((totalSeconds % 86400) / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const remainingSeconds = totalSeconds % 60;

    const parts = [];
    if (days > 0) parts.push(`${days}d`);
    if (hours > 0 || parts.length > 0) parts.push(`${hours}h`);
    if (minutes > 0 || parts.length > 0) parts.push(`${minutes}m`);
    parts.push(`${remainingSeconds}s`);

    return parts.join(' ');
  };

  const getTelemetry = (device) => device.latestTelemetry || null;

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
                <th>CPU</th>
                <th>RAM</th>
                <th>Disk</th>
                <th>Uptime</th>
                <th>Telemetry</th>
                <th>Registered</th>
                <th>Last Seen</th>
              </tr>
            </thead>
            <tbody>
              {devices.map((device) => {
                const telemetry = getTelemetry(device);
                const status = getStatus(device);
                const statusClassName = status === 'ONLINE' ? 'status-online' : 'status-stale';
                const hasTelemetry = Boolean(telemetry);

                return (
                  <tr key={device.id}>
                    <td className="hostname">{device.hostname}</td>
                    <td>{device.osName} {device.osVersion}</td>
                    <td>{device.agentVersion}</td>
                    <td className={`status-pill ${statusClassName}`}>{status}</td>
                    <td className={hasTelemetry ? 'telemetry-value' : 'no-telemetry'}>{hasTelemetry ? `${telemetry.cpuUsage ?? 'N/A'}%` : 'No telemetry'}</td>
                    <td className={hasTelemetry ? 'telemetry-value' : 'no-telemetry'}>{hasTelemetry ? `${telemetry.ramUsage ?? 'N/A'}%` : 'No telemetry'}</td>
                    <td className={hasTelemetry ? 'telemetry-value' : 'no-telemetry'}>{hasTelemetry ? `${telemetry.diskUsage ?? 'N/A'}%` : 'No telemetry'}</td>
                    <td className={hasTelemetry ? 'telemetry-value' : 'no-telemetry'}>{hasTelemetry ? formatUptime(telemetry.uptimeSeconds) : 'No telemetry'}</td>
                    <td className={hasTelemetry ? 'telemetry-value' : 'no-telemetry'}>{hasTelemetry ? formatDateTime(telemetry.recordedAt) : 'No telemetry yet'}</td>
                    <td>{formatDateTime(device.registeredAt)}</td>
                    <td>{formatDateTime(device.lastHeartbeat)}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
