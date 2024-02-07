import { getCategoryByName } from './categories';

// --- Constants ---
const PRODUCT_IMAGE_URL = '/assets/images/products';
const PRODUCTS_REQUEST = '/v1/products';
const INVENTORY_REQUEST = '/v1/products/inventory';
const POPULAR_INVENTORY_REQUEST = '/v1/products/popular';

// --- Interfaces ---

export interface Product {
    id: number;
    name: string;
    categoryId: number;
    description: string;
}

export interface Inventory {
    inventoryId: number;
    productId: number;
    storeId: number;
    categoryId: number;
    name: string;
    description: string;
    time: string;
    priceInCents: number;
    stockLevel: number;
    storeName: string;
    postcode: number;
}

// --- Loaders ---

// Used by Image component loader to load a product image
export const productImageLoader = ({ src }: { src: string }) => {
    return `${PRODUCT_IMAGE_URL}/${src}`;
};

// --- GET ---

// Retrieve all products
export const getProducts = async (apiURL: string) => {
    const response = await fetch(apiURL + PRODUCTS_REQUEST, {
        // Query must not be static in case product data changes
        cache: 'no-store',
    });

    if (!response.ok) {
        return [];
    }

    const data: Product[] = await response.json();
    return data;
};

// Retrieve list of products by name
export const getProductsByName = async (apiURL: string, query: string) => {
    const requestURL =
        apiURL + PRODUCTS_REQUEST + `?name=${encodeURIComponent(query)}`;
    const response = await fetch(requestURL, {
        // Query must not be static in case product data changes
        cache: 'no-store',
    });

    if (!response.ok) {
        return [];
    }

    const data: Product[] = await response.json();
    return data;
};

//Gets one product by ID
export const getProductById = async (apiURL: string, id: number) => {
    const requestURL = apiURL + PRODUCTS_REQUEST + `?id=${id}`;
    const res = await fetch(requestURL, {
        cache: 'no-store',
    });
    const data: Product = await res.json();
    return data;
};

// Gets a list of products by category name eg. 'Fruit'
export const getProductsByCategoryName = async (
    apiURL: string,
    query: string
) => {
  // TODO: Implement endpoint for below query
  // const requestURL = PRODUCTS_REQUEST + `?category=${encodeURIComponent(query)}`;

    const category = await getCategoryByName(apiURL, query);
    const requestURL =
        apiURL + PRODUCTS_REQUEST + `?category=${category?.id || 0}`;

    const response = await fetch(requestURL, {
        cache: 'no-store',
    });

    if (!response.ok) {
        return [];
    }

    const data: Product[] = await response.json();
    return data;
};

// Retrieve list of inventory by name
export const getInventoryByName = async (
    apiURL: string,
    query: string,
    postcode: number | null
) => {
    let requestURL =
        apiURL + INVENTORY_REQUEST + `?name=${encodeURIComponent(query)}`;
    if (postcode !== null) {
        requestURL += `&loc=${postcode}`;
    }

    const response = await fetch(requestURL, {
        cache: 'no-store',
    });

    if (!response.ok) {
        return [];
    }

    const data: Inventory[] = await response.json();
    return data;
};

// Gets list of inventory by product id
export const getInventoryByProductId = async (apiURL: string, id: number) => {
    const requestURL = apiURL + INVENTORY_REQUEST + `?id=${id}`;
    const response = await fetch(requestURL, {
        cache: 'no-store',
    });
    if (!response.ok) {
        return [];
    }

    const data: Inventory[] = await response.json();
    return data;
};

// Gets a list of inventory items by category name eg. 'Fruit'
export const getInventoryByCategoryName = async (
    apiURL: string,
    query: string
) => {
    // TODO: Implement endpoint for below query
    // const requestURL = INVENTORY_REQUEST + `?category=${encodeURIComponent(query)}`;

    const products = await getProductsByCategoryName(apiURL, query);
    const inventoryLists = await Promise.all(
        products.map(
            async (product) => await getInventoryByProductId(apiURL, product.id)
        )
    );
    const inventory = inventoryLists.flat();
    return inventory;
};

// Retrieve popular inventory items
export const getPopularInventory = async (apiURL: string, postcode: number | null) => {
    let request = apiURL + POPULAR_INVENTORY_REQUEST;
    if (postcode !== null) {
        request += "?loc=" + postcode.toString();
    }

    const response = await fetch(request, {
        // Query must not be static in case product data changes
        cache: 'no-store',
    });
    if (!response.ok) {
        return [];
    }

    const data: Inventory[] = await response.json();
    return data;
};
