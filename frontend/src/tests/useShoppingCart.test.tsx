import React from 'react';
import { vi, describe, expect, test } from 'vitest';
import { render, screen } from '@testing-library/react';
import { ShoppingCartProvider, useShoppingCart } from '@/contexts/ShoppingCartContext';

describe('useShoppingCart', () => {
    test('should return the correct values from the context', () => {
        // Cart context is mocked in setup.ts
        function TestComponent() {
            const { cartItems, cartQuantity } = useShoppingCart();

            return (
                <div>
                    <div data-testid="cartItems">{cartItems.length}</div>
                    <div data-testid="cartQuantity">{cartQuantity}</div>
                </div>
            );
        }

        render(
            <ShoppingCartProvider>
                <TestComponent />
            </ShoppingCartProvider>
        );

        expect(screen.getByTestId('cartItems').textContent).toBe('1');
        expect(screen.getByTestId('cartQuantity').textContent).toBe('1');
    });
});
