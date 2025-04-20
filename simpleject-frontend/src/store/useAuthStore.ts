import { create } from 'zustand';
import { axiosInstance } from '@/api/axios';  // axiosInstance를 import

// 쿠키에서 'access_token'을 읽는 함수
const getTokenFromCookie = () => {
    const token = document.cookie.split('; ').find(row => row.startsWith('access_token='));
    console.log("getTokenFromCookie token", token);

    const seta: string[] = document.cookie.split('; ');
    console.log("seta",seta);
    return token ? token.split('=')[1] : null;
};

interface AuthStore {
    token: string | null;
    isLoggedIn: boolean;
    authView: 'default' | 'login' | 'signup';
    errorMessage: string;
    login: (email: string, password: string) => Promise<boolean>;
    logout: () => void;
    setIsLoggedIn: (status: boolean) => void;
    setAuthView: (view: 'default' | 'login' | 'signup') => void;
}

export const useAuthStore = create<AuthStore>((set) => ({
    token: getTokenFromCookie(),  // 쿠키에서 토큰 읽기
    isLoggedIn: getTokenFromCookie() !== null,  // 토큰이 있으면 로그인 상태
    authView: 'default',
    errorMessage: '',

    // 로그인 요청
    login: async (email, password) => {
        set({ errorMessage: '' });
        try {
            // axiosInstance 사용
            const response = await axiosInstance.post('/auth/login', { email, password });
            if (response.status === 200) {
                console.log("response", response);
                // 로그인 성공 후 isLoggedIn 상태만 업데이트
                set({ isLoggedIn: true });
                return true; // 로그인 성공
            } else {
                set({ errorMessage: '로그인에 실패했습니다.' });
                return false; // 로그인 실패
            }
        } catch (error) {
            set({ errorMessage: '이메일 또는 비밀번호가 올바르지 않습니다.' });
            return false; // 로그인 실패
        }
    },

    logout: () => set({ token: null, isLoggedIn: false }),  // 로그아웃 처리
    // const handleLogout = async () => {
    //     await axiosInstance.post('/auth/logout');  // 로그아웃 요청
    //     setIsLoggedIn(false);  // 상태에서 로그아웃 처리
    //     setAuthView('default');  // 기본 화면으로 전환
    // };


    setIsLoggedIn: (status: boolean) => set({ isLoggedIn: status }),

    setAuthView: (view: 'default' | 'login' | 'signup') => set({ authView: view }),
}));
