import { vi, afterAll, afterEach, beforeAll } from 'vitest';
import { server } from './mocks/server';

beforeAll(() => {
    // Enable API mocking before tests.
    server.listen({ onUnhandledRequest: 'error' });

    // Mock the ShoppingCartContext
    vi.mock('@/contexts/ShoppingCartContext', async () => {
        const mockInventory = {
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

        const actual = (await vi.importActual('@/contexts/ShoppingCartContext')) as Record<
            string,
            any
        >;
        return {
            ...actual,
            ShoppingCartProvider: actual.ShoppingCartProvider,

            useShoppingCart: () => ({
                getItemQuantity: vi.fn(),
                increaseCartQuantity: vi.fn(),
                decreaseCartQuantity: vi.fn(),
                removeFromCart: vi.fn(),
                cartItems: [{ id: 1, quantity: 1, product: mockInventory }],
                cartQuantity: 1,
            }),
        };
    });
});

afterAll(() => {
    server.close();
});

afterEach(() => {
    server.resetHandlers();
});
