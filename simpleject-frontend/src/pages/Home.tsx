import { useAuthStore } from '@/store/useAuthStore';
import { Image } from '@/components/Image';
import { LoginForm } from '@/components/auth/LoginForm';
import { SignupForm } from '@/components/auth/SignupForm';
import { GoogleLoginButton } from '@/components/GoogleLoginButton';
import {Button} from "@/components/Button.tsx";
import {Modal} from "@/components/Modal.tsx";

export const Home = () => {
    const { isLoggedIn, authView, setAuthView } = useAuthStore();

    return (
        <div className="flex justify-center items-center min-h-screen bg-gray-100 px-8 py-16">
            {/* 왼쪽: 이미지 */}
            <div className="flex-1 flex justify-center items-center">
                <Image
                    src="/ex.png"
                    alt="이미지 설명"
                    className="w-full max-w-lg h-auto"
                />
            </div>

            {/* 오른쪽: 인증 섹션 */}
            <div className="flex-1 flex flex-col justify-center items-start space-y-6">
                <h1 className="text-3xl font-bold mb-8">Welcome to Simpleject</h1>

                {/* authView에 따라 다른 컴포넌트/버튼을 보여줍니다 */}
                {!isLoggedIn && authView === 'default' && (
                    <div className="space-y-4">
                        <Button
                            onClick={() => setAuthView('login')}
                            className="w-full bg-blue-500 text-white py-2 px-4 rounded"
                         label={"로그인"} type={"primary"}>
                        </Button>
                        <Button
                            onClick={() => setAuthView('signup')}
                            className="w-full bg-green-500 text-white py-2 px-4 rounded"
                            label={"이메일로 회원가입"}
                            type={"secondary"}>
                        </Button>
                        <Button
                            onClick={() => setAuthView('signup')}
                            className="w-full bg-green-500 text-white py-2 px-4 rounded"
                            label={"구글로 회원가입"}
                            type={"secondary"}>
                        </Button>
                    </div>
                )}

                {authView === 'login' && <LoginForm />}
                {authView === 'login' && <GoogleLoginButton />}
                {authView === 'signup' && <SignupForm />}
                <Modal />

            </div>
        </div>
    );
};
