import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true,
});

axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        const errorMessage = error?.response?.data?.message || error.message || 'Unknown error occurred';
        console.error('API Error:', errorMessage);
        return Promise.reject(error);
    }
);

export default axiosInstance;
