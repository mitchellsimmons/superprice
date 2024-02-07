'use client';
import { useState } from 'react';
import DeliveryForm from '@/components/DeliveryForm/DeliveryForm';
import OrderConfirmation from '@/components/OrderConfirmation/OrderConfirmation';

export default function CheckoutPage() {
    const [orderDetails, setOrderDetails] = useState(null);

    //TODO: Change any to correct type
    const handleOrderConfirmation = (details: any) => {
        setOrderDetails(details);
    };

    return (
        <div>
            {orderDetails ? (
                <OrderConfirmation orderDetails={orderDetails} />
            ) : (
                <DeliveryForm onOrderConfirmation={handleOrderConfirmation} />
            )}
        </div>
    );
}
