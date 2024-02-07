// --- Constants ---

const CATEGORY_IMAGE_URL = '/assets/images/categories';
const CATEGORIES_REQUEST = '/v1/categories';

// --- Interfaces ---

export interface Category {
    id: number;
    name: string;
    children: Category[];
}

// --- Loaders ---

// Used by Image component loader to load a category image
export const categoryImageLoader = ({ src }: { src: string }) => {
    return `${CATEGORY_IMAGE_URL}/${src}`;
};

// --- GET ---

// Retrieve full category trees of parent categories
export const getParentCategories = async (apiURL: string) => {
    console.log('CATEGORIES_REQUEST is ' + apiURL + CATEGORIES_REQUEST);
    const response = await fetch(apiURL + CATEGORIES_REQUEST, {
        // Query must not be static in case category data changes
        cache: 'no-store',
    });

    if (!response.ok) {
        return [];
    }

    const data: Category[] = await response.json();
    return data;
};

// Gets a category tree by name eg. 'Fruit & Vegetables'
export const getCategoryByName = async (apiURL: string, name: string) => {
    // TODO: Implement endpoint for below query
    // const requestURL = CATEGORIES_REQUEST + `?name=${encodeURIComponent(query)}`;
    const response = await fetch(apiURL + CATEGORIES_REQUEST, {
        cache: 'no-store',
    });

    if (!response.ok) {
        return undefined;
    }

    const data: Category[] = await response.json();

    const childCategories: Category[] = data
        .map((parentCategory) => parentCategory.children)
        .flat();
    const grandchildCategories: Category[] = childCategories
        .map((childCategory) => childCategory.children)
        .flat();
    const allCategories = [
        ...data,
        ...childCategories,
        ...grandchildCategories,
    ];

    return allCategories.find((category: Category) => category.name === name);
};
