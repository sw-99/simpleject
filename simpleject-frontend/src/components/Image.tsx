// components/Image.tsx
interface ImageProps {
    src: string;
    alt: string;
    className?: string; // 이미지에 추가할 스타일링
}

export const Image = ({ src, alt, className }: ImageProps) => {
    return <img src={src} alt={alt} className={className} />;
};
