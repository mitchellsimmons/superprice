'use client';

import { ChangeEvent, FormEvent, useState, useEffect } from 'react';
import { useShoppingCart } from '@/contexts/ShoppingCartContext';
import styles from './DeliveryForm.module.css';
import { CartItem } from '../CartSidebar/CartItem';

interface DeliveryFormProps {
    onOrderConfirmation: (details: any) => void;
}

const DeliveryForm = ({ onOrderConfirmation }: DeliveryFormProps) => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        phone: '',
        address: '',
        postcode: '',
        deliveryDate: '',
        deliveryTime: '',
        customMessage: '',
    });

    const { cartItems, resetCart } = useShoppingCart();

    const totalPrice = cartItems.reduce((total, item) => {
        return total + (item?.product.priceInCents || 0) * item.quantity;
    }, 0);

    const [nextFiveDays, setNextFiveDays] = useState<string[]>([]);

    useEffect(() => {
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);

        const nextFiveDaysArray = Array.from({ length: 5 }, (_, index) => {
            const nextDay = new Date(tomorrow);
            nextDay.setDate(tomorrow.getDate() + index);
            return nextDay.toISOString().split('T')[0];
        });

        setNextFiveDays(nextFiveDaysArray);
    }, []);

    function DummyFunction() {}

    const handleInputChange = (
        e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
    ) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSelectionChange = (e: ChangeEvent<HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();

        //https://eoi0wsczk10g3io.m.pipedream.net - for testing post

        const response = await fetch(
            process.env.CLIENT_API_URL + '/v1/cart/checkout',
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    name: formData.name,
                    email: formData.email,
                    phone: formData.phone,
                    address: formData.address,
                    postcode: formData.postcode,
                    deliveryDate: formData.deliveryDate,
                    deliveryWindowStart: formData.deliveryTime,
                    customMessage: formData.customMessage,
                    items: cartItems.map((item) => ({
                        inventoryID: item.id,
                        quantity: item.quantity,
                    })),
                }),
            }
        );

        const messageElement = document.getElementById('message');
        if (messageElement) {
            if (response.ok) {
                const responseData = await response.json();
                console.log(responseData);

                resetCart();

                // Call the onOrderConfirmation prop with the order details
                onOrderConfirmation(responseData);
            } else if (response.status === 400) {
                const responseData = await response.json(); // Parse response as JSON
                messageElement.innerHTML = responseData.detail; // Display the detail message
            } else {
                messageElement.innerHTML = 'Error submitting order';
            }
        }
    };

    return (
        <div
            style={{ display: 'flex', justifyContent: 'space-between' }}
            data-testid='delivery-form'
        >
            <div
                style={{
                    flexBasis: '45%',
                    marginRight: '40px',
                    marginLeft: '100px',
                    textAlign: 'center',
                }}
            >
                <div className={styles.details_box}>
                    <h2>Delivery Details</h2>
                    <form onSubmit={handleSubmit}>
                        <div style={{ margin: '20px 20px 20px 20px' }}>
                            <label htmlFor='name'>Name:</label>
                            <input
                                type='text'
                                id='name'
                                name='name'
                                value={formData.name}
                                onChange={handleInputChange}
                                required
                                style={{ marginLeft: '10px' }}
                            />
                        </div>
                        <div style={{ margin: '20px 20px 20px 20px' }}>
                            <label htmlFor='address'>Address:</label>
                            <input
                                type='text'
                                id='address'
                                name='address'
                                value={formData.address}
                                onChange={handleInputChange}
                                required
                                style={{ marginLeft: '10px' }}
                            />
                        </div>
                        <div style={{ margin: '20px 20px 20px 20px' }}>
                            <label htmlFor='email'>Email:</label>
                            <input
                                type='text'
                                id='email'
                                name='email'
                                value={formData.email}
                                onChange={handleInputChange}
                                required
                                style={{ marginLeft: '10px' }}
                            />
                        </div>
                        <div style={{ margin: '20px 20px 20px 20px' }}>
                            <label htmlFor='phone'>Phone:</label>
                            <input
                                type='text'
                                id='phone'
                                name='phone'
                                value={formData.phone}
                                onChange={handleInputChange}
                                required
                                style={{ marginLeft: '10px' }}
                            />
                        </div>
                        <div style={{ margin: '20px 20px 20px 20px' }}>
                            <label htmlFor='postcode'>Postcode:</label>
                            <input
                                type='text'
                                id='postcode'
                                name='postcode'
                                value={formData.postcode}
                                onChange={handleInputChange}
                                required
                                style={{ marginLeft: '10px' }}
                            />
                        </div>
                        <div style={{ margin: '20px 20px 20px 20px' }}>
                            <label htmlFor='deliveryDate'>Delivery Date:</label>
                            <select
                                id='deliveryDate'
                                name='deliveryDate'
                                value={formData.deliveryDate}
                                onChange={handleSelectionChange}
                                required
                                style={{ marginLeft: '10px' }}
                                className={styles.drop_down}
                            >
                                <option value='' disabled>
                                    Select a date
                                </option>
                                {nextFiveDays.map((date) => (
                                    <option key={date} value={date}>
                                        {new Date(date).toLocaleDateString(
                                            'en-AU',
                                            {
                                                year: 'numeric',
                                                month: 'long',
                                                day: 'numeric',
                                            }
                                        )}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div style={{ margin: '20px 20px 20px 20px' }}>
                            <label htmlFor='deliveryTime'>Delivery Time:</label>
                            <select
                                id='deliveryTime'
                                name='deliveryTime'
                                value={formData.deliveryTime}
                                onChange={handleSelectionChange}
                                required
                                style={{ marginLeft: '10px' }}
                                className={styles.drop_down}
                            >
                                <option value='' disabled>
                                    Select a delivery window
                                </option>
                                <option value='00:00'>12 AM - 4 AM</option>
                                <option value='04:00'>4 AM - 8 AM</option>
                                <option value='8:00'>8 AM - 12 PM</option>
                                <option value='12:00'>12 PM - 4 PM</option>
                                <option value='16:00'>4 PM - 8 PM</option>
                                <option value='20:00'>8 PM - 12 PM</option>
                            </select>
                        </div>
                        <div style={{ margin: '20px 20px 20px 20px' }}>
                            <label htmlFor='customMessage'>
                                Custom Message:
                            </label>
                            <input
                                type='text'
                                id='customMessage'
                                name='customMessage'
                                value={formData.customMessage}
                                onChange={handleInputChange}
                                style={{ marginLeft: '10px' }}
                            />
                        </div>
                        <div className={styles.form_button_container}>
                            <button
                                type='submit'
                                className={styles.form_button}
                            >
                                Submit
                            </button>
                        </div>
                        <div id='message' className={styles.message}></div>
                    </form>
                </div>
            </div>
            <div style={{ flexBasis: '45%' }}>
                <div style={{ marginRight: '50px', textAlign: 'center' }}>
                    <div className={styles.details_box}>
                        <h2>Cart Details</h2>
                        <div className={styles.cartItemsContainer}>
                            {cartItems.map((item) => (
                                <CartItem
                                    key={item.id}
                                    id={item.id}
                                    quantity={item.quantity}
                                    product={item.product}
                                    onClose={DummyFunction}
                                />
                            ))}
                        </div>
                        {totalPrice > 0 && (
                            <div className={styles.totalPrice}>
                                <span className={styles.totalLabel}>
                                    Total:{' '}
                                </span>
                                ${(totalPrice / 100).toFixed(2)}
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};
export default DeliveryForm;
