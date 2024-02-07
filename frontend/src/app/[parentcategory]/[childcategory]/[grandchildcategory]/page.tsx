import { getCategoryByName, Category } from '@/api/categories';
import { getInventoryByCategoryName, Inventory } from '@/api/products';
import React from 'react';
import styles from './page.module.css';
import ProductCard from './ProductCard';
import Link from 'next/link';

/* --- Category page: displays all products in a category.
       Example url: http://localhost:8080/Fruit%20%26%20Vegetables/Fruit/Bananas --- */

interface GrandchildCategoryPageProps {
    params: {
        parentcategory: string;
        childcategory: string;
        grandchildcategory: string;
    };
}
export default async function GrandchildCategoryPage({
    params: { parentcategory, childcategory, grandchildcategory },
}: GrandchildCategoryPageProps) {
    // Decode categories from uri encodings
    parentcategory = decodeURIComponent(parentcategory);
    childcategory = decodeURIComponent(childcategory);
    grandchildcategory = decodeURIComponent(grandchildcategory);

    const category: Category | undefined = await getCategoryByName(
        process.env.API_URL || '', grandchildcategory
    );

    if (!category) {
        return (
            <div style={{ textAlign: 'center', marginTop: '50px' }}>
                <h1>Category not found</h1>
                <p>
                    Sorry, the category you&rsquo;re looking for does not exist.
                </p>
            </div>
        );
    }

    // Gets latest inventory items for this category
    const inventory: Inventory[] = await getInventoryByCategoryName(
        process.env.API_URL || '',
        grandchildcategory
    );

    //Checks for invalid categories
    if (!inventory || inventory.length === 0) {
        return (
            <div style={{ textAlign: 'center', marginTop: '50px' }}>
                <h1>No results found</h1>
                <p>
                    Sorry, there are no products available for in this category.
                </p>
            </div>
        );
    }

    return (
        <div>
            <div
                style={{
                    margin: '20px',
                    display: 'flex',
                    alignItems: 'center',
                }}
            >
                <Link
                    href={`/${encodeURIComponent(
                        parentcategory
                    )}/${encodeURIComponent(childcategory)}`}
                >
                    <h3 className={styles.back_button}>Back </h3>
                </Link>
                <div style={{ flex: '1', textAlign: 'center' }}>
                    <h2>{category.name}</h2>
                </div>
            </div>
            <div className={styles.grid}>
                {inventory.map((inventoryItem: Inventory) => (
                    <Link
                        key={inventoryItem.inventoryId}
                        href={`/product/${inventoryItem.productId}`}
                    >
                        <ProductCard
                            key={inventoryItem.inventoryId}
                            {...inventoryItem}
                        />
                    </Link>
                ))}
            </div>
        </div>
    );
}
