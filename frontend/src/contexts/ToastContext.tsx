'use client';
import React, { createContext, useEffect, useContext } from 'react';

import { useEnvContext } from '@/app/EnvProvider';
import { useGlobalContext } from '@/app/context';
import { useToast, ToastData } from '../hooks/useToast';

interface ToastProviderProps {
    children: React.ReactNode;
}

type ToastContextType = {
    showToast: (message: string, type: string) => void;
    toasts: ToastData[];
};

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export const ToastProvider: React.FC<ToastProviderProps> = ({ children }) => {
    const { showToast, toasts } = useToast();
    const { CLIENT_API_URL } = useEnvContext();
    const { postcode } = useGlobalContext();

    useEffect(() => {
        const showToasts = (evt: Event) => {
            const messageEvent = evt as MessageEvent;
            const data = messageEvent.data;

            if (postcode) {
                for (const item of JSON.parse(data)) {
                    if (item.postcode === postcode) {
                        showToast(
                            `${item.description} is now $${
                                item.priceInCents / 100
                            } at ${item.storeName} (${item.postcode})`,
                            'info'
                        );
                    }
                }
            } else {
                for (const item of JSON.parse(data).slice(0, 3)) {
                    showToast(
                        `${item.description} is now $${
                            item.priceInCents / 100
                        } at ${item.storeName} (${item.postcode}`,
                        'info'
                    );
                }
            }
        };

        let evtSource: EventSource | null = null;

        if (typeof EventSource !== 'undefined') {
            evtSource = new EventSource(
                CLIENT_API_URL + '/v1/products/notifications'
            );

            evtSource.addEventListener('new inventory update', showToasts);
        }

        return () => {
            if (evtSource) {
                evtSource.removeEventListener(
                    'new inventory update',
                    showToasts
                );
            }
        };
    }, [postcode]);

    return (
        <ToastContext.Provider value={{ showToast, toasts }}>
            {children}
        </ToastContext.Provider>
    );
};

export const useToastContext = () => {
    const context = useContext(ToastContext);
    if (context === undefined) {
        throw new Error('useToastContext must be used within a ToastProvider');
    }
    return context;
};
