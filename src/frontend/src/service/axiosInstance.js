// axiosInstance.js
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080', // Replace with your backend's base URL
   withCredentials: true // Important if using session-based authentication
});

// Add a response interceptor for handling 401 errors
axiosInstance.interceptors.response.use(
    response => response, // Pass through successful responses
    error => {
        if (error.response && error.response.status === 401) {
            // Redirect to the login page if a 401 Unauthorized response is encountered
            window.location.href = '/login';
        }
        return Promise.reject(error); // Reject other errors so they can be handled elsewhere
    }
);
axiosInstance.interceptors.request.use(request => {
    console.log('Starting Request:', JSON.stringify(request, null, 2)); // Log complete request for inspection
    return request;
});

export default axiosInstance;
