import { Header } from '@/components/Layout/Header';
import { Footer } from '@/components/Layout/Footer';
import React from 'react';

interface LayoutProps {
    children: React.ReactNode;  // 페이지 콘텐츠를 children으로 받음
}

export const Layout = ({ children }: LayoutProps) => {

    return (
        <div>
            <Header />
            <main>
                {children} {/* 여기서 Routes로 렌더링된 페이지가 표시됩니다 */}
            </main>
            <Footer />
        </div>
    );
};
