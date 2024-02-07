import React from 'react';
import { vi, describe, expect, test } from 'vitest';
import { render, screen } from '@testing-library/react';
import { ShoppingCartProvider, ShoppingCartContext } from '@/contexts/ShoppingCartContext';
import { CartItem } from '@/components/CartSidebar/CartItem';

describe('ShoppingCartContext', () => {
    test('should render the cart item correctly', () => {
        const inventory = {
            inventoryId: 1,
            productId: 1,
            storeId: 1,
            categoryId: 1,
            name: 'Orange',
            description: 'Yummy orange',
            time: '12:01',
            priceInCents: 200,
            stockLevel: 50,
            storeName: 'Some store',
            postcode: 3000,
        };
        const cartItems = [{ id: 1, quantity: 1, product: inventory }];
        const cartQuantity = 1;

        render(
            <ShoppingCartProvider>
                <CartItem id={1} quantity={1} product={inventory} />
            </ShoppingCartProvider>
        );

        expect(screen.getByText(`${inventory.name}`)).toBeTruthy();
    });
});
