'use client';

import { useEffect, useState, useRef, FocusEvent } from 'react';
import Image from 'next/image';
import Link from 'next/link';

import {
    productImageLoader,
    getInventoryByName,
    Inventory,
} from '@/api/products';
import { useEnvContext } from '@/app/EnvProvider';
import { useGlobalContext } from '@/app/context';
import { QuantitySelector } from '@/components';
import styles from './Search.module.css';

// --- Constants ---

const MAX_SEARCH_RESULTS = 100;
const PRODUCT_PAGE_URL = '/product';

// --- Component ---

const Search = () => {
    const { CLIENT_API_URL } = useEnvContext();
    const { postcode } = useGlobalContext();
    const [show, setShow] = useState<boolean>(false);
    const [searchValue, setSearchValue] = useState('');
    const [inventory, setInventory] = useState<Inventory[]>([]);
    let searchRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        let isSubscribed = true;

        const fetchInventory = async () => {
            let data: Inventory[] = [];

            if (searchValue) {
                data = await getInventoryByName(
                    CLIENT_API_URL || '',
                    searchValue,
                    postcode
                );
            }

            // If effect is still subscribed after fetching then update state
            if (isSubscribed) {
                data = data.slice(0, MAX_SEARCH_RESULTS);
                setInventory(data);
            }
        };

        fetchInventory();

        // Unsubscribe from this effect if another effect is about to run
        // NOTE: This is important since the user may be typing very fast
        // which will result in a race condition between asynchronous effects
        return () => {
            isSubscribed = false;
        };
    }, [searchValue]);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        // We can't just fetch data here since fetching is asynchronous
        // meaning order would not be guaranteed
        let input = event.target.value.trim().toLowerCase();
        setSearchValue(input);
    };

    const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.code === 'Escape') {
            searchRef?.current?.blur();
        }
    };

    // Show search results on focus
    const handleFocus = () => {
        setShow(true);
    };

    // Hide search results on blur
    const handleBlur = (event: FocusEvent<HTMLInputElement>) => {
        if (!(event.relatedTarget instanceof HTMLInputElement)) {
            setShow(false);
        }
    };

    // Prevent the search input from losing focus if the results area is clicked
    const handleMouseDown = (event: React.MouseEvent<HTMLDivElement>) => {
        event.preventDefault();

        if (event.target instanceof HTMLInputElement) {
            event.target.focus();
        }
    };

    // Hide the results area and clear query when a result is clicked
    const handleClick = (event: React.MouseEvent<HTMLAnchorElement>) => {
        let isQuantityClicked = false;

        if (event.target !== null) {
            const target = event.target as HTMLElement;
            const parent = target?.parentNode as HTMLElement;

            if (parent?.className?.startsWith('QuantitySelector')) {
                isQuantityClicked = true;
                event.preventDefault();
                event.stopPropagation();
            }
        }

        if (!isQuantityClicked) {
            // Clear the current value
            if (searchRef?.current !== null) {
                searchRef.current.value = '';
            }
            setInventory([]);
            // Manually remove focus from the search field
            searchRef?.current?.blur();
        }
    };

    return (
        <div className={styles.search_container}>
            <input
                className={styles.search}
                ref={searchRef}
                type='text'
                placeholder='Search products...'
                onFocus={handleFocus}
                onBlur={handleBlur}
                onChange={handleChange}
                onKeyDown={handleKeyDown}
            />
            <div
                className={
                    styles.search_results +
                    ` ${show && inventory.length !== 0 && styles.visible}`
                }
                onMouseDown={handleMouseDown}
            >
                <div className={styles.scroll_area}>
                    {/* TODO: Test correct number of products + info displaying */}
                    {inventory.map((item) => {
                        const {
                            inventoryId,
                            productId,
                            name,
                            storeName,
                            postcode,
                            priceInCents,
                            stockLevel,
                        } = item;
                        return (
                            <article
                                className={styles.product_card}
                                key={inventoryId}
                            >
                                <Link
                                    href={`${PRODUCT_PAGE_URL}/${productId}`}
                                    onClick={handleClick}
                                >
                                    <div className={styles.image_container}>
                                        <Image
                                            src={`${productId}.png`}
                                            loader={productImageLoader}
                                            alt={name}
                                            width={100}
                                            height={100}
                                        />
                                    </div>
                                    <div className={styles.info_container}>
                                        <h4>{name}</h4>
                                        <div
                                            className={
                                                styles.store_stock_container
                                            }
                                        >
                                            <div
                                                className={
                                                    styles.store_container
                                                }
                                            >
                                                <p>{storeName}</p>
                                                <p>{postcode}</p>
                                            </div>
                                            <div
                                                className={
                                                    styles.stock_container
                                                }
                                            >
                                                <p>
                                                    {stockLevel > 0
                                                        ? 'Available'
                                                        : 'Out Of Stock'}
                                                </p>
                                                <p>
                                                    $
                                                    {(
                                                        priceInCents / 100.0
                                                    ).toFixed(2)}
                                                </p>
                                            </div>
                                            <QuantitySelector
                                                item={item}
                                                onBlur={handleBlur}
                                            />
                                        </div>
                                    </div>
                                </Link>
                            </article>
                        );
                    })}
                </div>
            </div>
        </div>
    );
};

export default Search;
