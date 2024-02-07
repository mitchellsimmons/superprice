'use client';
import React, { useEffect, useState } from 'react';

import { useGlobalContext } from './context';
import { useEnvContext } from './EnvProvider';
import { Carousel } from '@/components';
import PopularCard from './PopularCard';
import { getPopularInventory, Inventory } from '@/api/products';
import styles from './page.module.css';

// --- Constants ---

const MAX_PRODUCTS = 15;
const MOBILE_PRODUCTS_PER_PAGE = 1;
const BREAK_0_PRODUCTS_PER_PAGE = 2;
const BREAK_1_PRODUCTS_PER_PAGE = 3;
const BREAK_2_PRODUCTS_PER_PAGE = 4;
const BREAK_3_PRODUCTS_PER_PAGE = 5;

// --- Component ---

const PopularSection = () => {
    const { postcode } = useGlobalContext();
    const { CLIENT_API_URL } = useEnvContext();
    const [inventory, setInventory] = useState<Inventory[]>([]);

    // Update inventory whenever postcode changes
    useEffect(() => {
        let isSubscribed = true;

        const fetchInventory = async () => {
            let data = await getPopularInventory(
                CLIENT_API_URL || '',
                postcode
            );

            if (isSubscribed) {
                data = data.slice(0, MAX_PRODUCTS);
                setInventory(data);
            }
        };

        fetchInventory();

        // Unsubsrcribe from this effect if another effect is about to run
        // NOTE: This is important since we are loading the postcode from local
        // storage which will result in a race condition between asynchronous effects
        return () => {
            isSubscribed = false;
        };
    }, [postcode]);

    const getCarouselContents = () => {
        let cards: React.ReactElement[] = [];

        inventory.map((item) => {
            cards.push(<PopularCard key={item.inventoryId} item={item} />);
        });

        return cards;
    };

    return (
        <section className={styles.popular_section}>
            {' '}
            <h2 className={styles.heading}>Popular</h2>
            <Carousel
                mobileCardsPerPage={MOBILE_PRODUCTS_PER_PAGE}
                break0CardsPerPage={BREAK_0_PRODUCTS_PER_PAGE}
                break1CardsPerPage={BREAK_1_PRODUCTS_PER_PAGE}
                break2CardsPerPage={BREAK_2_PRODUCTS_PER_PAGE}
                break3CardsPerPage={BREAK_3_PRODUCTS_PER_PAGE}
            >
                {getCarouselContents()}
            </Carousel>
        </section>
    );
};

export default PopularSection;
