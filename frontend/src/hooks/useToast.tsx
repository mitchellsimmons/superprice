import { useState } from 'react';

export interface ToastData {
    key: number;
    message: string;
    type: string;
}

export const useToast = () => {
    const [toasts, setToasts] = useState<ToastData[]>([]);
    // TODO: Can make this configurable if desired?
    const autoCloseDuration = 5;

    const removeToast = (id: number) => {
        setToasts((prevToasts) => prevToasts.filter((toast) => toast.key !== id));
    };

    const showToast = (message: string, type: string) => {
        const toast = {
            key: Date.now(),
            message,
            type,
        };

        setToasts((prevToasts) => [...prevToasts, toast]);

        setTimeout(() => {
            removeToast(toast.key);
        }, autoCloseDuration * 1000);
    };

    return { toasts, showToast, removeToast };
};
