import React from 'react';

import { Carousel } from '@/components';
import CategoryCard from './CategoryCard';
import { getParentCategories } from '@/api/categories';
import styles from './page.module.css';

// --- Constants ---

const MAX_CATEGORIES = 15;
const MOBILE_CATEGORIES_PER_PAGE = 1;
const BREAK_0_CATEGORIES_PER_PAGE = 2;
const BREAK_1_CATEGORIES_PER_PAGE = 3;
const BREAK_2_CATEGORIES_PER_PAGE = 4;
const BREAK_3_CATEGORIES_PER_PAGE = 5;

// --- Component ---

const CategorySection = async () => {
    //TODO: Test same as popular stuff
    const getCarouselContents = async () => {
        let categories = await getParentCategories(process.env.API_URL || '');
        // console.log(categories);
        categories = categories.slice(0, MAX_CATEGORIES);

        let cards: React.ReactElement[] = [];

        categories.forEach((category, index) => {
            cards.push(<CategoryCard key={index} {...category} />);
        });

        return cards;
    };

    return (
        <section className={styles.popular_category_section}>
            <h2 className={styles.heading}>Categories</h2>
            <Carousel
                mobileCardsPerPage={MOBILE_CATEGORIES_PER_PAGE}
                break0CardsPerPage={BREAK_0_CATEGORIES_PER_PAGE}
                break1CardsPerPage={BREAK_1_CATEGORIES_PER_PAGE}
                break2CardsPerPage={BREAK_2_CATEGORIES_PER_PAGE}
                break3CardsPerPage={BREAK_3_CATEGORIES_PER_PAGE}
            >
                {await getCarouselContents()}
            </Carousel>
        </section>
    );
};

export default CategorySection;
