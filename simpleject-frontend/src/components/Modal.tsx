// components/Modal.tsx
import { useModalStore } from '@/store/useModalStore';

export const Modal = () => {
    const { isModalOpen, modalMessage, modalType, closeModal } = useModalStore();

    let modalStyles = '';
    let icon = '';

    // 모달 타입에 따른 스타일 및 아이콘 설정
    if (modalType === 'error') {
        modalStyles = 'bg-red-500 text-white';
        icon = '❌';
    } else if (modalType === 'success') {
        modalStyles = 'bg-green-500 text-white';
        icon = '✅';
    } else if (modalType === 'warning') {
        modalStyles = 'bg-yellow-500 text-black';
        icon = '⚠️';
    }

    if (!isModalOpen) return null;

    return (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-50 flex justify-center items-center z-50">
            <div className={`bg-white p-6 rounded shadow-lg max-w-md w-full ${modalStyles}`}>
                <div className="flex items-center space-x-4">
                    <span className="text-3xl">{icon}</span>
                    <div className="text-center flex-grow">
                        <p className="text-lg font-semibold text-black">{modalMessage}</p>
                    </div>
                </div>
                <div className="mt-4 text-center">
                    <button
                        onClick={closeModal}
                        className="bg-blue-500 text-white py-2 px-4 rounded"
                    >
                        닫기
                    </button>
                </div>
            </div>
        </div>
    );
};
