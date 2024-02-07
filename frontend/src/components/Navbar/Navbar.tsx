'use client';

export const dynamic = 'force-dynamic'

import Image from 'next/image';
import Link from 'next/link';
import { FaShoppingCart, FaUser } from 'react-icons/fa';

import { CategoryMenu, Search, PostcodeSelector } from '@/components';
import { useGlobalContext } from '@/app/context';
import styles from './Navbar.module.css';
import { useShoppingCart } from '@/contexts/ShoppingCartContext';

// --- Constants ---

const LOGO_URL = '/assets/images/logo.svg';

// --- Component ---

const Navbar = () => {
    const { isBreak0, isBreak2, isBreak3 } = useGlobalContext();
    const { openCart, cartQuantity } = useShoppingCart();

    return (
        <nav className={styles.nav}>
            <div className={styles.main_nav}>
                <CategoryMenu />
                <div className={styles.center_container}>
                    <Link className={styles.logo_link} href="/">
                        {/* TODO: Change this to Next Image */}
                        <img className={styles.logo} src={LOGO_URL} alt="Logo" />
                    </Link>
                    {isBreak2 && <Search />}
                    <PostcodeSelector />
                    <button className={styles.cart_btn} onClick={openCart}>
                        <FaShoppingCart />
                        {/* TODO: Restyle cart quantity icon */}
                        {isBreak0 && cartQuantity > 0 && <p className={styles.cart_counter}>{`${cartQuantity} items`}</p>}
                    </button>
                </div>
                {isBreak3 && <div className={styles.filler}></div>}
            </div>
            {isBreak2 || (
                <div className={styles.search_nav}>
                    <Search />
                </div>
            )}
        </nav>
    );
};

export default Navbar;
