// components/Button.tsx
interface ButtonProps {
    label: string;
    onClick: () => void;
    className?: string; // 추가적인 클래스
    type: 'primary' | 'secondary' | 'google'; // 버튼 스타일 구분
}

export const Button = ({ label, onClick, className, type }: ButtonProps) => {
    let buttonStyles = '';

    if (type === 'primary') {
        buttonStyles = 'bg-blue-500 text-white';
    } else if (type === 'secondary') {
        buttonStyles = 'bg-green-500 text-white';
    } else if (type === 'google') {
        buttonStyles = 'bg-red-500 text-white';
    }

    return (
        <button
            onClick={onClick}
            className={`${buttonStyles} py-2 px-4 rounded ${className}`}
        >
            {label}
        </button>
    );
};
