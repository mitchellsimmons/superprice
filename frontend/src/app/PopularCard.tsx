'use client';

import Image from 'next/image';
import Link from 'next/link';

import { productImageLoader, Inventory } from '@/api/products';
import { QuantitySelector } from '@/components';
import styles from './page.module.css';

const PopularCard = ({ item }: { item: Inventory }) => {
    const { productId, name, storeName, priceInCents, postcode } = item;

    const handleClick = (event: React.MouseEvent<HTMLAnchorElement>) => {
        if (event.target !== null) {
            const target = event.target as HTMLElement;
            const parent = target?.parentNode as HTMLElement;

            if (parent?.className?.startsWith('QuantitySelector')) {
                event.preventDefault();
                event.stopPropagation();
            }
        }
    };

    return (
        <Link href={`/product/${productId}`} onClick={handleClick}>
            <div className={styles.product_card} data-testid='popular-card'>
                <p className={styles.price}>{`\$${priceInCents / 100.0}`}</p>
                <div className={styles.image_container}>
                    <Image
                        src={`${productId}.png`}
                        loader={productImageLoader}
                        alt={name}
                        width={100}
                        height={100}
                    />
                </div>
                <h4>{name}</h4>
                <div className={styles.info_container}>
                    <p>{storeName}</p>
                    <p>{postcode}</p>
                </div>
                <QuantitySelector item={item} />
            </div>
        </Link>
    );
};

export default PopularCard;
