'use client';
import { useToastContext } from '@/contexts/ToastContext';

export default function ToastTest() {
    const { showToast } = useToastContext();

    const handleClick = () => {
        showToast('Something happened!', 'info');
    };

    return <button onClick={handleClick}>Click for toast</button>;
}
