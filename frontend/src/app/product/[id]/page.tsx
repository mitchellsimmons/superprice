import { getInventoryByProductId, Inventory, productImageLoader } from '@/api/products';

import Image from 'next/image';
import { QuantitySelector } from '@/components';

import styles from './page.module.css';

/* --- Product page: displays a product information.
       Example url: http://localhost:8080/product/1 --- */

interface ProductPageProps {
    params: { id: string };
}
export default async function ProductPage({ params: { id } }: ProductPageProps) {
    // Gets list of inventory for the product ID
    const inventory = await getInventoryByProductId(process.env.API_URL || '', Number.parseInt(id));

    //Check for invalid product
    if (inventory.length === 0) {
        return (
            <div style={{ textAlign: 'center', marginTop: '50px' }}>
                <h1>No results found.</h1>
                <p>Sorry, there is no inventory available for this product.</p>
            </div>
        );
    }

    // SortS the inventory by priceInCents in ascending order
    const sortedInventory = inventory.slice().sort((a, b) => a.priceInCents - b.priceInCents);

    //Gets first product object for information
    const item = inventory[0];

    //Gets store product with the lowest price
    const lowestPrice = Math.min(...inventory.map((item: Inventory) => item.priceInCents));

    //Loads the image
    const imageSrc = productImageLoader({ src: `${id}.png` });

    return (
        <div className="flex">
            <div style={{ marginRight: '40px', textAlign: 'center' }}>
                <div style={{ margin: '100px 0 0 100px' }}>
                    <Image src={imageSrc} alt={item.name} width={400} height={400} />
                </div>
            </div>
            <div>
                <h2>{item.name}</h2>
                <p>{item.description}</p>
                <div className={styles.productContainer}>
                    <div className="product-grid">
                        {sortedInventory.map((storeItem: Inventory) => (
                            <div
                                className={`${styles.product_item} ${
                                    storeItem.priceInCents === lowestPrice
                                        ? styles.lowest_price
                                        : ''
                                }`}
                                key={storeItem.storeId}
                            >
                                <div className={styles.info_container}>
                                    <div className={styles.store_name}>
                                        {storeItem.storeName} {storeItem.postcode}
                                    </div>
                                    <div className={styles.price}>{`$${(
                                        storeItem.priceInCents / 100.0
                                    ).toFixed(2)}`}</div>
                                </div>
                                <div className={styles.quantity_selector}>
                                    <QuantitySelector item={storeItem} />
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
}
