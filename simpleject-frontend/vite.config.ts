import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  // Vite 플러그인 설정
  plugins: [
    react(), // React 플러그인 추가 (JSX 지원 및 HMR 등 제공)
  ],

  // 개발 서버 설정
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 프록시할 대상 서버 주소 (Spring Boot 서버)
        changeOrigin: true, // 원본 요청의 Origin을 대상 서버로 변경
      },
    },
  },

  // 프로덕션 빌드 설정
  build: {
    sourcemap: false, // 소스맵 비활성화 (소스 코드 보호)
    minify: 'terser', // 파일 압축 및 난독화 (Terser 사용)

    // Terser 설정
    terserOptions: {
      compress: {
        drop_console: true, // console.log 제거 (배포 시 디버깅 코드 숨김)
        drop_debugger: true, // debugger 제거
      },
      mangle: true, // 변수 이름 난독화 (코드 가독성 저하로 보안 강화)
    },
  },
});
