import { productImageLoader, Inventory } from '@/api/products';
import { useShoppingCart } from '@/contexts/ShoppingCartContext';
import { Literata } from 'next/font/google';
import React from 'react';
import styles from './CartItem.module.css';
import Image from 'next/image';
import QuantityAdjuster from './QuantityAdjuster';
import Link from 'next/link';

export interface CartItemProps {
    id: number;
    quantity: number;
    product: Inventory;
    onClose: () => void;
    // name: string;
    // priceInCents: number;
}

export const CartItem = ({ id, quantity, product, onClose }: CartItemProps) => {
    const { removeFromCart } = useShoppingCart();
    const price = product.priceInCents / 100.0;
    const sumPrice = price * quantity;

    const handleCloseSidebar = (event: React.MouseEvent<HTMLAnchorElement>) => {
        if (event.target !== null) {
            const target = event.target as HTMLElement;
            const parent = target?.parentNode as HTMLElement;
            console.log(target);
            console.log(parent);

            if (parent?.className?.startsWith('QuantityAdjuster')) {
                event.preventDefault();
                event.stopPropagation();
            } else {
                onClose();
            }
        } else {
            onClose();
        }
    };

    const handleAdjustQuantity = (
        event: React.MouseEvent<HTMLButtonElement, MouseEvent>
    ) => {
        event.preventDefault();
        event.stopPropagation();
    };

    return (
        <div className={styles.cartItemContainer}>
            <Link
                href={`/product/${product.productId}`}
                onClick={handleCloseSidebar}
            >
                <div className={styles.container}>
                    <div className={styles.productImage}>
                        <Image
                            src={`${product.productId}.png`}
                            loader={productImageLoader}
                            alt={product.name}
                            width={70}
                            height={70}
                        />
                    </div>
                    <div className={styles.productDetails}>
                        <div className={styles.productName}>{product.name}</div>
                        <div className={styles.price}>
                            <span>${sumPrice.toFixed(2)}</span>
                        </div>
                        <div className={styles.storeDetails}>
                            <span className={styles.storePrice}>
                                ${`${price.toFixed(2)}`}
                            </span>
                            <span className={styles.storePrice}> @ </span>
                            <span className={styles.storePrice}>
                                {product.storeName}
                            </span>
                            <QuantityAdjuster
                                item={product}
                                onAdjust={handleAdjustQuantity}
                            />
                        </div>
                    </div>
                </div>
            </Link>
            <button
                className={styles.removeBtn}
                onClick={() => removeFromCart(id)}
            >
                x
            </button>
        </div>
    );
};
