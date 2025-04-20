import React, { useState } from 'react';
import { useAuthStore } from '@/store/useAuthStore'; // zustand store 가져오기

export const SignupForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const { setShowSignup } = useAuthStore(); // zustand에서 setShowSignup 가져오기

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        // 회원가입 로직 추가
        setShowSignup(false); // 회원가입 폼 숨기기
    };

    return (
        <div className="absolute top-1/2 right-1/4 transform -translate-y-1/2 p-8 bg-white shadow-md rounded-lg">
            <h2 className="text-2xl mb-4">회원가입</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="이메일"
                    className="w-full p-2 mb-4 border rounded"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="비밀번호"
                    className="w-full p-2 mb-4 border rounded"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button type="submit" className="w-full bg-green-500 text-white py-2 rounded">
                    이메일로 회원가입
                </button>
            </form>
        </div>
    );
};
