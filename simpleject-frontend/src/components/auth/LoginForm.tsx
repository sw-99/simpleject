// components/LoginForm.tsx
import { useState } from 'react';
import { useAuthStore } from '@/store/useAuthStore';
import { useNavigate } from 'react-router-dom';
import { Input } from '@/components/Input';
import { Button } from '@/components/Button';
import { useModalStore } from '@/store/useModalStore';

export const LoginForm = () => {
    const { setIsLoggedIn, login, errorMessage, setAuthView } = useAuthStore();
    const { openModal } = useModalStore();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async () => {
        const success = await login(email, password); // 로그인 함수 호출
        if (success) {
            setIsLoggedIn(true); // 로그인 상태 변경
            setAuthView('default'); // 로그인 후 기본 화면으로 전환
            navigate('/mypage'); // 로그인 후 /mypage로 리디렉션
        } else {
            openModal(errorMessage, 'error'); // 에러 모달 표시
        }
    };

    return (
        <div>
            <Input
                type="email"
                placeholder="이메일"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <Input
                type="password"
                placeholder="비밀번호"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />

            <Button
                label="로그인"
                onClick={handleLogin}
                type="primary"
                className="w-2/3 mb-4"
            />
        </div>
    );
};
