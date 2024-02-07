'use client';

import { ShoppingCartProvider } from '@/contexts/ShoppingCartContext';
import React, { createContext, useContext, useEffect, useState } from 'react';
import { useToast } from '../hooks/useToast';
import { ToastProvider } from '../contexts/ToastContext';
import ToastList from '../components/Toast/ToastList';

import { getPostcodeFromStorage, updatePostcodeStorage } from '@/api/postcodes';

// --- Constants ---

const POSTCODE_KEY = 'SUPERPRICE_POSTCODE';

// --- Context ---

type SetPostcodeFn = {
    (postcode: number | null): void;
};

export interface GlobalContextType {
    isBreak0: boolean;
    isBreak1: boolean;
    isBreak2: boolean;
    isBreak3: boolean;
    postcode: number | null;
    setPostcode: SetPostcodeFn;
}

const GlobalContext = createContext<GlobalContextType>({
    isBreak0: false,
    isBreak1: false,
    isBreak2: false,
    isBreak3: false,
    postcode: null,
    setPostcode: (postcode: number | null) => {},
});

// Custom hook to access global state from descendant components
export const useGlobalContext = () => useContext(GlobalContext);

// --- Provider ---

interface PropsType {
    children: React.ReactNode[];
}

// Global context component
const AppProvider = ({ children }: PropsType) => {
    const getBreak0 = () => {
        const style = getComputedStyle(document.body);
        return style.getPropertyValue('--break-0');
    };

    const getBreak1 = () => {
        const style = getComputedStyle(document.body);
        return style.getPropertyValue('--break-1');
    };

    const getBreak2 = () => {
        const style = getComputedStyle(document.body);
        return style.getPropertyValue('--break-2');
    };

    const getBreak3 = () => {
        const style = getComputedStyle(document.body);
        return style.getPropertyValue('--break-3');
    };

    const [isBreak0, setIsBreak0] = useState(true);
    const [isBreak1, setIsBreak1] = useState(true);
    const [isBreak2, setIsBreak2] = useState(true);
    const [isBreak3, setIsBreak3] = useState(true);

    useEffect(() => {
        const update = (e: MediaQueryListEvent) => setIsBreak0(e.matches);
        const break0 = getBreak0();

        // Ensure state is updated on initial render
        setIsBreak0(window.matchMedia(`(min-width: ${break0})`).matches);

        // Listen for change in media size
        window.matchMedia(`(min-width: ${break0})`).addEventListener('change', update);

        // Cleanup
        return () => {
            window.matchMedia(`(min-width: ${break0})`).removeEventListener('change', update);
        };
    }, []);

    useEffect(() => {
        const update = (e: MediaQueryListEvent) => setIsBreak1(e.matches);
        const break1 = getBreak1();

        // Ensure state is updated on initial render
        setIsBreak1(window.matchMedia(`(min-width: ${break1})`).matches);

        // Listen for change in media size
        window.matchMedia(`(min-width: ${break1})`).addEventListener('change', update);

        // Cleanup
        return () => {
            window.matchMedia(`(min-width: ${break1})`).removeEventListener('change', update);
        };
    }, []);

    useEffect(() => {
        const update = (e: MediaQueryListEvent) => setIsBreak2(e.matches);
        const break2 = getBreak2();

        // Ensure state is updated on initial render
        setIsBreak2(window.matchMedia(`(min-width: ${break2})`).matches);

        // Listen for change in media size
        window.matchMedia(`(min-width: ${break2})`).addEventListener('change', update);

        // Cleanup
        return () => {
            window.matchMedia(`(min-width: ${break2})`).removeEventListener('change', update);
        };
    }, []);

    useEffect(() => {
        const update = (e: MediaQueryListEvent) => setIsBreak3(e.matches);
        const break3 = getBreak3();

        // Ensure state is updated on initial render
        setIsBreak3(window.matchMedia(`(min-width: ${break3})`).matches);

        // Listen for change in media size
        window.matchMedia(`(min-width: ${break3})`).addEventListener('change', update);

        // Cleanup
        return () => {
            window.matchMedia(`(min-width: ${break3})`).removeEventListener('change', update);
        };
    }, []);

    // --- Toasts ---
    const { toasts, removeToast } = useToast();
    const position = 'bottomRight';

    useEffect(() => {
        console.log('toasts in context changed:', toasts);
    }, [toasts]);
    // Set postcode state and cache to localStorage
    const setPostcode = (postcode_: number | null) => {
        updatePostcodeStorage(postcode_);
        _setPostcode(postcode_);
    };

    // Local storage is not available on initial render
    const [postcode, _setPostcode] = useState<number | null>(null);

    // Retrieve postcode from localStorage after initial render
    useEffect(() => {
        _setPostcode(getPostcodeFromStorage);
    }, []);

    return (
        <GlobalContext.Provider
            value={{
                isBreak0,
                isBreak1,
                isBreak2,
                isBreak3,
                postcode,
                setPostcode,
            }}
        >
            <ToastProvider>
                <ShoppingCartProvider>
                    <ToastList toasts={toasts} removeToast={removeToast} position={position} />
                    {children}
                </ShoppingCartProvider>
            </ToastProvider>
        </GlobalContext.Provider>
    );
};

export default AppProvider;
