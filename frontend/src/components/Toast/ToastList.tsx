import React, { useEffect, useRef } from 'react';
import styles from './ToastList.module.css';
import Toast from './Toast';
import { useToastContext } from '../../contexts/ToastContext';

export interface ToastData {
    key: number;
    message: string;
    type: string;
}

export interface ToastListProps {
    toasts: ToastData[];
    position: string;
    removeToast: (id: number) => void;
}

const ToastList: React.FC<ToastListProps> = ({ position, removeToast }) => {
    const { toasts } = useToastContext();

    useEffect(() => {
        console.log('toasts in toast list changed:', toasts);
    }, [toasts]);

    const positionStyle = styles[position];

    const listRef = useRef(null);

    // const sortedData = position.includes('bottom') ? [...data].reverse() : [...data];

    useEffect(() => {
        const handleScrolling = (el: HTMLDivElement | null) => {
            const isTopPosition = ['top-left', 'top-right'].includes(position);
            if (isTopPosition) {
                el?.scrollTo(0, el.scrollHeight);
            } else {
                el?.scrollTo(0, 0);
            }
        };

        handleScrolling(listRef.current);
    }, [position, toasts]);
    return (
        toasts.length > 0 && (
            <div className={`${styles.container} ${positionStyle}`}>
                {toasts.map((toast) => (
                    <Toast
                        key={toast.key * Math.random()}
                        message={toast.message}
                        type={toast.type}
                        onClose={() => removeToast(toast.key)}
                    />
                ))}
            </div>
        )
    );
};

export default ToastList;
