'use client';
import React from 'react';
import styles from './Toast.module.css';

export interface ToastProps {
    key: number;
    message: string;
    type: string;
    onClose: () => void;
}

const Toast: React.FC<ToastProps> = ({ message, type, onClose }) => {
    return (
        <div className={`${styles.container} ${styles[type]}`} role="alert" data-testid="toast">
            <div className={styles.message}>
                <p>{message}</p>
            </div>
            <button className={styles.closeBtn} onClick={onClose}>
                X
            </button>
        </div>
    );
};

export default Toast;
