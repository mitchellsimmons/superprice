import React from 'react';
import { getCategoryByName } from '@/api/categories';
import styles from './page.module.css';
import Link from 'next/link';
import ProductCard from './[childcategory]/[grandchildcategory]/ProductCard';
import {getInventoryByCategoryName, Inventory} from "@/api/products";

/* --- Parent Category page: displays all categories in a parent category.
       Example url: http://localhost:8080/Fruit%20%26%20Vegetables --- */

interface ParentCategoryPageProps {
    params: { parentcategory: string };
}

export default async function ParentCategoryPage({
    params: { parentcategory },
}: ParentCategoryPageProps) {
    // Decode category from uri encodings
    parentcategory = decodeURIComponent(parentcategory);

    // Gets a category by name; also contains all subcategories
    const category = await getCategoryByName(
        process.env.API_URL || '',
        parentcategory);

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
        process.env.API_URL || '', parentcategory
    );

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
                    href={`/`}
                >
                    <h3 className={styles.back_button}>Back </h3>
                </Link>
                <div style={{ flex: '1', textAlign: 'center' }}>
                    <h2>{category.name}</h2>
                </div>
            </div>
            <div style={{ margin: '20px' }}>
                <div style={{ textAlign: 'center', margin: '50px' }}>
                    <div className={styles.subcategory_container}>
                        {category.children.map((subcategory) => (
                            <Link
                                key={subcategory.name}
                                href={`/${category.name}/${subcategory.name}`}
                            >
                                <div
                                    className={styles.subcategory}
                                    key={subcategory.id}
                                >
                                    {subcategory.name}
                                </div>
                            </Link>
                        ))}
                    </div>
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
