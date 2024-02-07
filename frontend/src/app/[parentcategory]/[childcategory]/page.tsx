import React from 'react';
import { getCategoryByName } from '@/api/categories';
import styles from './page.module.css';
import Link from 'next/link';
import ProductCard from './[grandchildcategory]/ProductCard';
import { getInventoryByCategoryName, Inventory } from '@/api/products';

/* --- Child Category page: displays all categories in a child category.
       Example url: http://localhost:8080/Fruit%20%26%20Vegetables/Fruit --- */

interface ChildCategoryPageProps {
  params: { parentcategory: string; childcategory: string };
}

export default async function ChildCategoryPage({
  params: { parentcategory, childcategory },
}: ChildCategoryPageProps) {
  // Decode categories from uri encodings
  parentcategory = decodeURIComponent(parentcategory);
  childcategory = decodeURIComponent(childcategory);

    // Gets a category by name; also contains all subcategories
    const category = await getCategoryByName(
        process.env.API_URL || '',
        childcategory
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
        childcategory
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
                <Link href={`/${encodeURIComponent(parentcategory)}`}>
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
                                href={`/${parentcategory}/${childcategory}/${subcategory.name}`}
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
