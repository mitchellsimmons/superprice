'use client';

import styles from './QuantityAdjuster.module.css';
import { useShoppingCart } from '@/contexts/ShoppingCartContext';
import { Inventory } from '@/api/products';
import { CartItemProps } from '@/components/CartSidebar/CartItem';

interface QuantityAdjusterProps {
    item: Inventory;
    onAdjust: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
}

const QuantityAdjuster = ({ item }: QuantityAdjusterProps) => {
    const { getItemQuantity, increaseCartQuantity, decreaseCartQuantity } = useShoppingCart();

    // Get quantity from cart
    const quantity = getItemQuantity(item.inventoryId);

    return (

        <div className={styles.container}>
            <div className={styles.quantity_container}>
                <button
                    className={quantity === 1 ? styles.disabled : ''}
                    onClick={() => decreaseCartQuantity(item.inventoryId)}
                >
                    -
                </button>
                <div>{quantity}</div>
                <button onClick={() => increaseCartQuantity(item.inventoryId, item)}>+</button>
            </div>
        </div>
    );
};

export default QuantityAdjuster;
