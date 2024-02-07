'use client';
import React, { useState, useEffect } from 'react';
import styles from './CartSidebar.module.css';
import { useShoppingCart } from '@/contexts/ShoppingCartContext';
import { CartItem } from './CartItem';
import Link from 'next/link';

interface CartSideBarProps {
    isOpen: boolean;
    onClose: () => void;
}

export const CartSidebar = ({ isOpen, onClose }: CartSideBarProps) => {
    const { closeCart, cartItems } = useShoppingCart();
    const totalPrice = cartItems.reduce((total, item) => {
        return total + (item?.product.priceInCents || 0) * item.quantity;
    }, 0);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            const sidebar = document.querySelector(`.${styles.sidebar}`);
            if (sidebar && !sidebar.contains(event.target as Node)) {
                onClose();
            }
        };

        if (isOpen && typeof document !== 'undefined') {
            document.body.style.overflow = 'hidden'; 
            document.addEventListener('click', handleClickOutside);
        }

        return () => {
            document.body.style.overflow = ''; 
            document.removeEventListener('click', handleClickOutside);
        };
    }, [isOpen, onClose]);

    const boxShadow = {
        'box-shadow': '0 0 40px rgba(0, 0, 0, 0.2)',
    } as React.CSSProperties;
    
    const sidebarStyle = isOpen && boxShadow;

    if (isOpen) {
        document.body.style.overflow = 'hidden'; 
    } else if (typeof window !== 'undefined') {
        document.body.style.overflow = ''; 
    }

    return (
        <div className={styles.outerContainer}>
            {isOpen && <div className={styles.overlay}></div>}
            <aside className={`${styles.sidebar} ${isOpen && styles.visible}`}>
                <div className={styles.innerContainer}>
                    <div className={styles.header}>
                        <h2>Cart</h2>
                        <button className={styles.closeBtn} onClick={closeCart}>
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                fill="#8ec4a5"  
                                height="48px"
                                viewBox="0 0 24 24"
                                width="48px"
                            >
                                <path d="M0 0h24v24H0z" fill="none" />
                                <path d="M19 6.41l-1.41-1.41L12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41l5.59 5.59L19 17.59 13.41 12z" />
                            </svg>
                        </button>
                    </div>
                    <ul className={styles.items}>
                        {cartItems.map((item) => (
                            <CartItem key={item.id} onClose={onClose} {...item} />
                        ))}
                    </ul>
                    {totalPrice > 0 && (
                        <div className={styles.totalPrice}>
                            <span className={styles.totalLabel}>Total:</span>
                            ${(totalPrice / 100).toFixed(2)} 
                        </div>
                    )}
                    {totalPrice > 0 && (
                        <Link href={`/checkout`}>
                            <button className={styles.checkoutBtn} onClick={closeCart}>
                                Checkout
                            </button>
                        </Link>
                    )}
                </div>
            </aside>
        </div>
    );
};
