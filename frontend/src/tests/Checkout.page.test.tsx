import { describe, expect, test } from 'vitest';
import { render } from '@testing-library/react';
import CheckoutPage from '@/app/checkout/page';
import DeliveryForm from '@/components/DeliveryForm/DeliveryForm';
import { EnvProvider } from '@/app/EnvProvider';
import GlobalContext from '@/app/context';

describe('Testing Checkout Page', () => {
    const env = {
        CLIENT_API_URL: 'http://localhost:8080',
    };

    test('Test structure', async () => {
        const expectedChild = 'DeliveryForm';

        // Render the CheckoutPage component
        const { getByTestId } = render(
            <EnvProvider env={env}>
                <GlobalContext>
                    <CheckoutPage />
                    <></>
                </GlobalContext>
            </EnvProvider>
        );

        // Find the DeliveryForm component
        const deliveryForm = getByTestId('delivery-form');

        expect(deliveryForm).toBeTruthy();
    });
});
