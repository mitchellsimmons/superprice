
// cart context adapted from tutorial https://www.youtube.com/watch?v=lATafp15HWA
import { Inventory } from '@/api/products';
import { CartSidebar } from '@/components/CartSidebar/CartSidebar';
import { createContext, useContext, useState, useEffect } from 'react';
import { useLocalStorage } from '@/hooks/useLocalStorage';

export const MIN_QUANTITY = 0;
export const MAX_QUANTITY = 999;

interface ShoppingCartProviderProps {
    children: React.ReactNode;
}

interface CartItem {
    id: number;
    quantity: number;
    product: Inventory;
}

interface ShoppingCartContext {
    openCart: () => void;
    closeCart: () => void;
    getItemQuantity: (id: number) => number;
    setItemQuantity: (id: number, product: Inventory, quantity: number) => void;
    increaseCartQuantity: (id: number, product: Inventory) => void;
    decreaseCartQuantity: (id: number) => void;
    removeFromCart: (id: number) => void;
    cartItems: CartItem[];
    cartQuantity: number;
    resetCart: () => void;
}

export const ShoppingCartContext = createContext({} as ShoppingCartContext);

export function useShoppingCart() {
    return useContext(ShoppingCartContext);
}

export function ShoppingCartProvider({ children }: ShoppingCartProviderProps) {
    const [isOpen, setIsOpen] = useState(false);
    const [cartItems, setCartItems] = useState<CartItem[]>([]); // Initialize to an empty array (hydrate it later)
    const [cartQuantity, setCartQuantity] = useState(0);
    const [hasMounted, setHasMounted] = useState(false);

    // All these use effects are to stop hydration errors
    useEffect(() => {
        setHasMounted(true);
    }, []);

    useEffect(() => {
        // Load cart items from local storage only after the component has mounted
        if (hasMounted) {
            const storedItems = window.localStorage.getItem('shopping-cart');
            setCartItems(storedItems !== null ? JSON.parse(storedItems) : []);
        }
    }, [hasMounted, setCartItems]);

    useEffect(() => {
        // Only update cart quantity after the component has mounted
        if (hasMounted) {
            const newQuantity = cartItems.reduce((quantity, item) => item.quantity + quantity, 0);
            setCartQuantity(newQuantity);
            // Store the new cart items in local storage
            window.localStorage.setItem('shopping-cart', JSON.stringify(cartItems));
        }
    }, [cartItems, hasMounted]);

    const openCart = () => setIsOpen(true);
    const closeCart = () => setIsOpen(false);

    function getItemQuantity(id: number) {
        return cartItems.find((item) => item.id === id)?.quantity || 0;
    }

    function setItemQuantity(productId: number, product: Inventory, quantity: number) {
        quantity = Math.max(MIN_QUANTITY, Math.min(quantity, MAX_QUANTITY));

        setCartItems((currItems) => {
            if (currItems.find((item) => item.id === productId) == null) {
                return [...currItems, { id: productId, quantity: quantity, product: product }];
            } else {
                return currItems.map((item) => {
                    if (item.id === productId) {
                        return { ...item, quantity };
                    } else {
                        return item;
                    }
                });
            }
        });
    }

    function increaseCartQuantity(productId: number, product: Inventory) {
        setCartItems((currItems) => {
            if (currItems.find((item) => item.id === productId) == null) {
                return [...currItems, { id: productId, quantity: 1, product: product }];
            } else {
                return currItems.map((item) => {
                    if (item.id === productId) {
                        let quantity = Math.min(MAX_QUANTITY, item.quantity + 1);
                        return { ...item, quantity };
                    } else {
                        return item;
                    }
                });
            }
        });
    }

    function decreaseCartQuantity(productId: number) {
        setCartItems((currItems) => {
            if (currItems.find((item) => item.id === productId)?.quantity === 1) {
                return currItems.filter((item) => item.id !== productId);
            } else {
                return currItems.map((item) => {
                    if (item.id === productId) {
                        let quantity = Math.max(MIN_QUANTITY, item.quantity - 1);
                        return { ...item, quantity: quantity };
                    } else {
                        return item;
                    }
                });
            }
        });
    }

    function removeFromCart(productId: number) {
        setCartItems((currItems) => {
            return currItems.filter((item) => item.id !== productId);
        });
    }

    function resetCart() {
        setCartItems([]);
    }

    return (
        <ShoppingCartContext.Provider
            value={{
                openCart,
                closeCart,
                getItemQuantity,
                setItemQuantity,
                increaseCartQuantity,
                decreaseCartQuantity,
                removeFromCart,
                cartItems,
                cartQuantity,
                resetCart,
            }}
        >
            <CartSidebar isOpen={isOpen} onClose={closeCart} />
            {children}
        </ShoppingCartContext.Provider>
    );
}
