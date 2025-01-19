/// <reference types="vite/client" />

interface ImportMetaEnv {
    readonly VITE_API_BASE_URL: string; // API URL 타입 정의
    readonly VITE_APP_TITLE: string; // 애플리케이션 제목
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}

