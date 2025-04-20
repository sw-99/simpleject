// store/useModalStore.ts
import { create } from 'zustand';

interface ModalStore {
    isModalOpen: boolean;
    modalMessage: string;
    modalType: 'error' | 'success' | 'warning' | '';
    openModal: (message: string, type: 'error' | 'success' | 'warning') => void;
    closeModal: () => void;
}

export const useModalStore = create<ModalStore>((set) => ({
    isModalOpen: false,
    modalMessage: '',
    modalType: '',

    openModal: (message, type) => set({ isModalOpen: true, modalMessage: message, modalType: type }),
    closeModal: () => set({ isModalOpen: false, modalMessage: '', modalType: '' })
}));
