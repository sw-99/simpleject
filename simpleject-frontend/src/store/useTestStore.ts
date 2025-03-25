import { create } from 'zustand';
import axiosInstance from '../api/axios';
import { AxiosError } from 'axios';

interface TestStore {
    testMessage: string;
    errorMessage: string;
    isLoading: boolean;
    fetchTestMessage: () => Promise<void>;
}

export const useTestStore = create<TestStore>((set) => ({
    testMessage: '',
    errorMessage: '',
    isLoading: false,

    fetchTestMessage: async () => {
        set((state) => ({ ...state, isLoading: true, errorMessage: '' })); // 기존 메시지 유지
        try {
            const response = await axiosInstance.get<string>('/test');
            set({ testMessage: response.data, isLoading: false });
        } catch (error: unknown) {
            const errorMessage =
                error instanceof AxiosError
                    ? error.response?.data?.message || error.message
                    : 'An unexpected error occurred';
            set({ errorMessage, isLoading: false });
            console.error('Error fetching test message:', errorMessage);
        }
    },
}));

