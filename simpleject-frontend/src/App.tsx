import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Layout } from '@/components/Layout/Layout';
import { Home } from './pages/Home';
import { MyPage } from './pages/MyPage';
import { useAuthStore } from '@/store/useAuthStore';
import {useEffect} from "react";
import {axiosInstance} from "@/api/axios.ts"; // useAuthStore를 가져오기

export const App = () => {
    // const { isLoggedIn, setIsLoggedIn, token } = useAuthStore();

    const { isLoggedIn, setIsLoggedIn } = useAuthStore();

    useEffect(() => {
        const checkLoginStatus = async () => {
            try {
                const response = await axiosInstance.get('/auth/validate');  // 토큰 검증 API 호출
                if (response.status === 200) {
                    setIsLoggedIn(true);  // 유효한 토큰이 있으면 로그인 상태로 설정
                }
            } catch (error) {
                setIsLoggedIn(false);  // 토큰이 유효하지 않으면 로그아웃 상태로 설정
            }
        };

        checkLoginStatus();  // 컴포넌트가 마운트될 때 로그인 상태 확인
    }, []);
    
    console.log("App isLoggedIn", isLoggedIn);
    return (
        <Router>
            <Layout>
                <Routes>
                    {/* 기본 홈 페이지 */}
                    <Route path="/" element={<Home />} />

                    {/* 로그인한 사용자만 접근 가능 */}
                    <Route
                        path="/mypage"
                        element={isLoggedIn ? <MyPage /> : <Navigate to="/" />}
                    />
                </Routes>
            </Layout>
        </Router>
    );
};
