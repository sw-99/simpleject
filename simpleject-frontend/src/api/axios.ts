import axios from 'axios';

export const axiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true, // 쿠키 자동 포함 설정
});

// 요청 인터셉터 추가 (토큰을 Authorization 헤더에 추가)
axiosInstance.interceptors.request.use(
    (config) => {
        const token = document.cookie.split('; ').find(row => row.startsWith('access_token='));
        const accessToken = token ? token.split('=')[1] : null;
        console.log("interceptors token",token);
        console.log("interceptors accessToken",accessToken);
        // 토큰이 존재하면 헤더에 추가
        if (accessToken) {
            config.headers['Authorization'] = `Bearer ${accessToken}`;
        }
        console.log("config",config);
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터 (에러 처리 등)
axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        console.log("axiosInstance error", error);
        const errorMessage = error?.response?.data?.message || error.message || 'Unknown error occurred';
        console.error('API Error:', errorMessage);
        return Promise.reject(error);
    }
);

