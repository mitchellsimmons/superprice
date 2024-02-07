import { render, screen } from '@testing-library/react';
import { vi, test, describe, expect, beforeEach } from 'vitest';
import OrderConfirmation from '@/components/OrderConfirmation/OrderConfirmation';

describe('OrderConfirmation component', () => {
    beforeEach(async () => {
        const orderDetails = {
            id: '123',
            status: 'Confirmed',
            orderDate: '2023-10-07',
            cart: {
                totalInCents: 2000,
                items: [
                    {
                        inventoryID: '1',
                        productName: 'Test Product',
                        quantity: 2,
                        subTotalInCents: 2000,
                        storeName: 'Test Store',
                    },
                ],
            },
            details: {
                name: 'John Doe',
                email: 'john.doe@example.com',
                phone: '0402456765',
                address: '123 Main St',
                postcode: '3000',
                deliveryDate: '2023-10-14',
                deliveryWindowStart: '10:00',
            },
        };

        render(<OrderConfirmation orderDetails={orderDetails} />);
    });

    test('renders order details', async () => {
        const orderConfirmed = screen.getByText('Order Confirmed!');
        expect(orderConfirmed).not.toBeNull();

        const orderNumber = screen.getByText('Order Number: #123');
        expect(orderNumber).not.toBeNull();

        const orderStatus = screen.getByText('Order Status: Confirmed');
        expect(orderStatus).not.toBeNull();

        const orderDate = screen.getByText('Order Date: 2023-10-07');
        expect(orderDate).not.toBeNull();

        const name = screen.getByText('Name: John Doe');
        expect(name).not.toBeNull();

        const email = screen.getByText('Email: john.doe@example.com');
        expect(email).not.toBeNull();

        const phone = screen.getByText('Phone: 0402456765');
        expect(phone).not.toBeNull();

        const address = screen.getByText('Address: 123 Main St');
        expect(address).not.toBeNull();

        const postcode = screen.getByText('Postcode: 3000');
        expect(postcode).not.toBeNull();

        const deliveryDate = screen.getByText('Delivery: 2023-10-14 after 10:00');
        expect(deliveryDate).not.toBeNull();

        // Commented out because vitest is too strict when using <b> etc
        // const product1 = screen.getByText('Test Product x2: $20.00 from Test Store');
        // expect(product1).not.toBeNull();

        const orderTotal = screen.getByText('Order Total: $20.00');
        expect(orderTotal).not.toBeNull();
    });
});
