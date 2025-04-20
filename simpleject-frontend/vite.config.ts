/* eslint-disable import/no-default-export */
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  // Vite 플러그인 설정
  plugins: [
    react(), // React 플러그인 추가 (JSX 지원 및 HMR 등 제공)
  ],

  // 경로 alias 설정
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'), // '@/...' 경로를 'src/'로 인식
    },
  },

  // 개발 서버 설정
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 프록시할 대상 서버 주소 (Spring Boot 서버)
        changeOrigin: true, // 원본 요청의 Origin을 대상 서버로 변경
        secure: false, // HTTPS를 사용하는 경우 true로 설정
        ws: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
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
        drop_console: true, // console.log 제거
        drop_debugger: true, // debugger 제거
      },
      mangle: true, // 변수 이름 난독화
    },
  },
})
