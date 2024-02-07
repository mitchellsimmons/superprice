'use client';

import Image from 'next/image';
import Link from 'next/link';

import { categoryImageLoader, Category } from '@/api/categories';
import styles from './page.module.css';

const CategoryCard = ({ id, name }: Category) => {
  return (
    <Link href={`/${encodeURIComponent(name)}`}>
        <div className={styles.category_card} data-testid="category-card">
            <div
                className={styles.image_container}
                data-testid="category-card-img-container"
            >
                <Image
                    src={`${id}.png`}
                    loader={categoryImageLoader}
                    alt={name}
                    width={100}
                    height={100}
                />
            </div>
            <h4>{name}</h4>
        </div>
    </Link>
  );
};

export default CategoryCard;
