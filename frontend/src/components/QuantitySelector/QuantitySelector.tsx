'use client';

import { useEffect, useState, FocusEvent } from 'react';

import styles from './QuantitySelector.module.css';
import {
    useShoppingCart,
    MIN_QUANTITY,
    MAX_QUANTITY,
} from '@/contexts/ShoppingCartContext';
import { Inventory } from '@/api/products';

interface QuantitySelectorProps {
    item: Inventory;
    onBlur?: (event: FocusEvent<HTMLInputElement>) => void;
}

const QuantitySelector = ({ item, onBlur }: QuantitySelectorProps) => {
    const {
        cartItems,
        getItemQuantity,
        setItemQuantity,
        increaseCartQuantity,
        decreaseCartQuantity,
    } = useShoppingCart();

    // Get quantity from cart
    let quantity = getItemQuantity(item.inventoryId);
    const [input, setInput] = useState(`${quantity}`);

    useEffect(() => {
        const value = getItemQuantity(item.inventoryId)?.toString();
        if (value !== input) {
            setInput(value);
        }
    }, [cartItems]);

    const handleIncreaseCartQuantity = (
        event: React.MouseEvent<HTMLElement>
    ) => {
        increaseCartQuantity(item.inventoryId, item);
        setInput(`${Math.min(MAX_QUANTITY, quantity + 1)}`);
    };

    const handleDecreaseCartQuantity = (
        event: React.MouseEvent<HTMLElement>
    ) => {
        decreaseCartQuantity(item.inventoryId);
        setInput(`${Math.max(MIN_QUANTITY, quantity - 1)}`);
    };

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        // Limit value in range 0-999
        let value = `${event.target.value}`.slice(0, 3);
        // Strip any non-numeric characters from input
        let num = Number.parseInt(value);
        value = Number.isNaN(num) ? '' : `${num}`;
        num = Number.isNaN(num) ? 0 : num;
        setInput(value);
        setItemQuantity(item.inventoryId, item, num);
    };

    const handleInputBlur = (event: FocusEvent<HTMLInputElement>) => {
        if (input === '') {
            setInput('0');
        }

        if (onBlur) {
            onBlur(event);
        }
    };

    return (
        <div className={styles.quantity_outer_container}>
            <div className={styles.quantity_container}>
                <button
                    className={quantity === 0 ? styles.disabled : ''}
                    onClick={handleDecreaseCartQuantity}
                >
                    -
                </button>
                <input
                    value={`${input}`}
                    onChange={handleChange}
                    onBlur={handleInputBlur}
                />
                <button onClick={handleIncreaseCartQuantity}>+</button>
            </div>
            <button
                className={styles.add_to_cart}
                onClick={handleIncreaseCartQuantity}
            >
                Add to Bag
            </button>
        </div>
    );
};

export default QuantitySelector;
