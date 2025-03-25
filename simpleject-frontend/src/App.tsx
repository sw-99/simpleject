import { useEffect } from 'react';
import {useTestStore} from '@/store/useTestStore';

export function App() {
    // Zustand 스토어에서 상태와 동작을 가져옵니다.
    const { testMessage, errorMessage, isLoading, fetchTestMessage } = useTestStore();

    // 컴포넌트가 처음 렌더링될 때 API 호출
    useEffect(() => {
        fetchTestMessage(); // API 호출
    }, [fetchTestMessage]);

    return (
        <div style={{ textAlign: 'center', padding: '20px' }}>
            <h1>Test API with Zustand</h1>
            <button onClick={fetchTestMessage} disabled={isLoading} style={{ padding: '10px', fontSize: '16px' }}>
                {isLoading ? 'Loading...' : 'Call /api/test'}
            </button>
            {testMessage && <p style={{ color: 'green', marginTop: '20px' }}>Response: {testMessage}</p>}
            {errorMessage && <p style={{ color: 'red', marginTop: '20px' }}>Error: {errorMessage}</p>}
        </div>
    );
}

