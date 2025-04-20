import { Link } from 'react-router-dom';
import { useAuthStore } from '@/store/useAuthStore';

export const Header = () => {
    const {
        isLoggedIn,
        logout,
        setShowLogin,
        setShowSignup
    } = useAuthStore();

    const handleLoginClick = () => {
        setShowLogin(true);
        setShowSignup(false);
    };

    const handleSignupClick = () => {
        setShowSignup(true);
        setShowLogin(false);
    };

    return (
        <header className="flex justify-between items-center w-full py-4 px-8 bg-white text-gray-800">
            <div onClick={() => window.location.href = "/"} className="text-xl font-bold cursor-pointer">
                simpleject
            </div>

            <nav className="flex space-x-6">
                <div>
                    <Link to="/" className="text-gray-800">홈</Link>
                </div>
                <div>
                    <Link to="/mypage" className="text-gray-800">마이페이지</Link>
                </div>

                {!isLoggedIn ? (
                    <>
                        <button onClick={handleLoginClick}>로그인</button>
                        <button onClick={handleSignupClick}>회원가입</button>
                    </>
                ) : (
                    <button onClick={logout}>로그아웃</button>
                )}
            </nav>
        </header>
    );
};
