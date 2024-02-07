'use client';
import React from 'react';
import { CartItem } from '../CartSidebar/CartItem';
import { useShoppingCart } from '@/contexts/ShoppingCartContext';
import styles from './OrderConfirmation.module.css';

type OrderConfirmationProps = {
    orderDetails: any; // TODO: Types
};

const OrderConfirmation: React.FC<OrderConfirmationProps> = ({ orderDetails }) => {
    console.log('orderDetails', orderDetails);

    const orderItems = orderDetails.cart.items;

    const {
        id,
        status,
        orderDate,
        cart: { totalInCents },
    } = orderDetails;

    const {
        name,
        email,
        phone,
        address,
        postcode,
        country,
        deliveryInstructions,
        deliveryDate,
        deliveryWindowStart,
    } = orderDetails.details;

    return (
        <div className={styles.orderConfirmation}>
            <h2 className={styles.orderHeader}>Order Confirmed!</h2>
            <div className={styles.orderDetails}>
                <h3>Order Number: #{id}</h3>
                <p>Order Status: {status}</p>
                <p>Order Date: {orderDate}</p>
            </div>
            <div className={styles.deliveryDetails}>
                <h3>Delivery Details</h3>
                <p>Name: {name}</p>
                <p>Email: {email}</p>
                <p>Phone: {phone}</p>
                <p>Address: {address}</p>
                <p>Postcode: {postcode}</p>
                <p>{`Delivery: ${deliveryDate} after ${deliveryWindowStart}`}</p>
            </div>
            <p>Ordered:</p>
            {orderItems.map((item: any) => (
                <div className={styles.orderItem} key={`${item.inventoryID}-div`}>
                    <p>
                        <b>{item.productName}</b> x{item.quantity}:{' '}
                        {`$${(item.subTotalInCents / 100).toFixed(2)} `}
                        from <i>{item.storeName}</i>
                    </p>
                </div>
            ))}
            <b>
                <h4 className={styles.orderTotal}>
                    Order Total: ${(totalInCents / 100).toFixed(2)}
                </h4>
            </b>
        </div>
    );
};

export default OrderConfirmation;
