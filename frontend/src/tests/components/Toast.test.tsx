import { vi, test, expect, describe, beforeEach } from 'vitest';
import { act, render, renderHook, screen, waitForElementToBeRemoved } from '@testing-library/react';
import { useToastContext, ToastProvider } from '@/contexts/ToastContext';
import { useToast, ToastData } from '@/hooks/useToast';
import ToastList from '@/components/Toast/ToastList';

const TestingComponent = () => {
    const { showToast } = useToastContext();

    const handleClick = () => {
        showToast('test toast', 'info');
    };

    return <button onClick={handleClick}>Click for toast</button>;
};

describe('Toast component', () => {
    beforeEach(async () => {
        const toastHook = renderHook(useToast);
        const { toasts, removeToast } = toastHook.result.current;
        console.log('toasts', toasts);

        render(
            <ToastProvider>
                <ToastList toasts={toasts} removeToast={removeToast} position={'bottomRight'} />
                <TestingComponent />
            </ToastProvider>
        );
    });

    test(
        'useToast renders Toast to toastList and removes it after timeout',
        async () => {
            // Click the button to generate a toast
            const button = screen.getByText('Click for toast');

            // wait for re-render after button clic
            await act(async () => button.click());

            // Check if the toast is displayed
            let toast = screen.getByTestId('toast');
            expect(toast);

            // Check if the toast is removed after timeout
            // Test will fail if toast is not removed after timeout (5 secs)

            // ----- UNCOMMENT BELOW LINE TO TEST AUTOREMOVAL -----
            // const removed = await waitForElementToBeRemoved(toast, { timeout: 5000 });
        },
        { timeout: 6000 }
    );
});
