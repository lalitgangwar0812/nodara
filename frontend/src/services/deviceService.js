import axios from 'axios';

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const deviceService = {
  /**
   * Get all registered devices
   */
  getDevices: async () => {
    try {
      const response = await apiClient.get('/api/v1/devices');
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message || 'Failed to fetch devices'
      );
    }
  },

  /**
   * Register a new device
   */
  registerDevice: async (deviceInfo) => {
    try {
      const response = await apiClient.post(
        '/api/v1/devices/register',
        deviceInfo
      );
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message || 'Failed to register device'
      );
    }
  },
};