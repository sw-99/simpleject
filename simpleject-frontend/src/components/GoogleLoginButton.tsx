// components/GoogleLoginButton.tsx
import { Button } from './Button';

export const GoogleLoginButton = () => {
    const handleGoogleLogin = () => {
        window.location.href = 'https://accounts.google.com/o/oauth2/v2/auth';
    };

    return (
        <Button label="구글로 로그인" onClick={handleGoogleLogin} type="google" />
    );
};
