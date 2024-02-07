import Image from 'next/image';

import { productImageLoader, Inventory } from '@/api/products';
import { QuantitySelector } from '@/components';
import styles from './page.module.css';

/* --- Product Card: based upon Popular Card
        changed some styles--- */

const ProductCard = (item: Inventory) => {
    const { productId, name, storeName, priceInCents } = item;
    const imageSrc = productImageLoader({ src: `${productId}.png` });
    const price = priceInCents / 100.0;

    return (
        <div className={styles.product_card}>
            <div className={styles.image_container}>
                <Image src={imageSrc} alt={name} width={100} height={100} />
            </div>
            <h4>{name}</h4>
            <div className={styles.info_container}>
                <p>{storeName}</p>
                <p>{`\$${price}`}</p>
            </div>
            <QuantitySelector item={item} />
        </div>
    );
};

export default ProductCard;
