'use client';

export const dynamic = 'force-dynamic';

import Image from 'next/image';
import Link from 'next/link';
import { useEffect, useState } from 'react';
import { FaChevronRight } from 'react-icons/fa';
import { FiChevronLeft } from 'react-icons/fi';
import { usePathname } from 'next/navigation';
import { getParentCategories, categoryImageLoader, Category } from '@/api/categories';
import { useGlobalContext } from '@/app/context';
import { useEnvContext } from '@/app/EnvProvider';
import styles from './CategoryMenu.module.css';

// --- Constants ---

const ARROW_URL = '/assets/images/category_arrow.svg';

// --- Component ---

const CategoryMenu = () => {
    const [isSidebarVisible, setIsSidebarVisible] = useState(false);
    const [parentCategory, setParentCategory] = useState<Category | null>(null);
    const [childCategory, setChildCategory] = useState<Category | null>(null);
    const [categories, setCategories] = useState<Category[]>([]);
    const { CLIENT_API_URL } = useEnvContext();
    const { isBreak2, isBreak3 } = useGlobalContext();
    const pathName = usePathname();

    // Client components cannot be async, therefore async behaviour must be isolated
    const updateCategories = async () => {
        setCategories(await getParentCategories(CLIENT_API_URL || ''));
    };

    // Fetch categories upon opening the sidebar
    useEffect(() => {
        if (isSidebarVisible) {
            updateCategories();
        }
    }, [isSidebarVisible]);

    // Prevent the body from scrolling when the sidebar is open
    const toggleScroll = (enabled: boolean) => {
        if (enabled) {
            document.body.classList.add('prevent-scroll');
        } else {
            document.body.classList.remove('prevent-scroll');
        }
    };

    // Toggle sidebar visibility when browse button is clicked
    const handleToggleMenu = () => {
        setChildCategory(null);
        setParentCategory(null);

        setIsSidebarVisible((previous) => {
            toggleScroll(!previous);
            return !previous;
        });
    };

    const handleParentCategoryClick = (category: Category) => {
        setChildCategory(null);
        setParentCategory(category);
    };

    const handleChildCategoryClick = (category: Category) => {
        setChildCategory(category);
    };

    const boxShadow = {
        'box-shadow': '0 0 40px rgba(0, 0, 0, 0.2)',
    } as React.CSSProperties;
    let parentStyle = {};
    let childStyle = {};
    let grandchildStyle = {};

    if (isBreak3) {
        // Display shadow only if parent selected
        if (parentCategory) {
            parentStyle = boxShadow;
        }

        // Display shadow only if child selected
        if (childCategory) {
            childStyle = boxShadow;
        }
    } else {
        if (parentCategory) {
            childStyle = boxShadow;
        }

        if (childCategory) {
            grandchildStyle = boxShadow;
        }
    }

    return (
        <button className={`${styles.hamburger_btn} ${isSidebarVisible && styles.selected}`}>
            <div className={styles.hamburger_lines_container} onClick={handleToggleMenu}>
                <div className={styles.hamburger_line} />
                <div className={styles.hamburger_line} />
                <div className={styles.hamburger_line} />
            </div>
            <aside
                className={`${styles.sidebar} ${isSidebarVisible && styles.visible}`}
                style={parentStyle}
            >
                <h2>Browse Categories</h2>
                <div className={styles.category_container}>
                    {categories.map((category) => {
                        return (
                            <div
                                key={category.id}
                                className={styles.category}
                                onClick={() => handleParentCategoryClick(category)}
                            >
                                <Image
                                    src={`${category.id}.png`}
                                    loader={categoryImageLoader}
                                    alt={category.name}
                                    width={100}
                                    height={100}
                                />
                                {category.name}
                                {isBreak3 && (
                                    <FaChevronRight
                                        className={`${
                                            parentCategory?.id === category.id && styles.selected
                                        }`}
                                    />
                                )}
                            </div>
                        );
                    })}
                </div>
            </aside>
            <aside
                className={`${styles.sidebar} ${styles.child} ${parentCategory && styles.visible}`}
                style={childStyle}
            >
                {parentCategory && (
                    <div
                        className={styles.heading_container}
                        onClick={() => setParentCategory(null)}
                    >
                        <h2>{parentCategory.name}</h2>
                        <FiChevronLeft className={styles.back_btn} />
                    </div>
                )}
                <div className={styles.category_container}>
                    {parentCategory &&
                        parentCategory.children.map((category) => {
                            return (
                                <div
                                    key={category.id}
                                    className={styles.category}
                                    onClick={() => handleChildCategoryClick(category)}
                                >
                                    <Image
                                        src={`${category.id}.png`}
                                        loader={categoryImageLoader}
                                        alt={category.name}
                                        width={100}
                                        height={100}
                                    />
                                    {category.name}
                                    {isBreak3 && (
                                        <FaChevronRight
                                            className={`${
                                                childCategory?.id === category.id && styles.selected
                                            }`}
                                        />
                                    )}
                                </div>
                            );
                        })}
                </div>
            </aside>
            <aside
                className={`${styles.sidebar} ${styles.grandchild} ${
                    childCategory && styles.visible
                }`}
                style={grandchildStyle}
            >
                {parentCategory && childCategory && (
                    <div
                        className={styles.heading_container}
                        onClick={() => setChildCategory(null)}
                    >
                        <h2>{childCategory.name}</h2>
                        <FiChevronLeft className={styles.back_btn} />
                    </div>
                )}
                <div className={styles.category_container}>
                    {parentCategory &&
                        childCategory &&
                        childCategory.children.map((category) => {
                            return (
                                <Link
                                    key={category.id} //TODO: I just added this to get rid of the error, but I don't know if it's correct
                                    href={`/${encodeURIComponent(
                                        parentCategory.name
                                    )}/${encodeURIComponent(
                                        childCategory.name
                                    )}/${encodeURIComponent(category.name)}`}
                                    onClick={handleToggleMenu}
                                >
                                    <div key={category.id} className={styles.category}>
                                        <Image
                                            src={`${category.id}.png`}
                                            loader={categoryImageLoader}
                                            alt={category.name}
                                            width={100}
                                            height={100}
                                        />
                                        {category.name}
                                    </div>
                                </Link>
                            );
                        })}
                </div>
            </aside>
            <div
                className={`${styles.overlay} ${isSidebarVisible && styles.visible}`}
                onClick={handleToggleMenu}
            ></div>
            {isBreak2 && pathName === '/' && (
                <div className={styles.arrow_tooltip}>
                    <img className={styles.arrow} src={ARROW_URL} alt="Arrow" />
                    <p>Browse Categories</p>
                </div>
            )}
        </button>
    );
};

export default CategoryMenu;
