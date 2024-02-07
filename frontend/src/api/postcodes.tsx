// --- Constants ---

const POSTCODES_REQUEST = '/v1/postcodes';
const POSTCODE_KEY = 'SUPERPRICE_POSTCODE';
const DEFAULT_POSTCODE = 3000;

// --- Interfaces ---

export interface Postcode {
    postcode: number;
}

// --- Utils ---

// Get postcode from localStorage
// Returns DEFAULT_POSTCODE if not set
export const getPostcodeFromStorage = () => {
    if (typeof window !== 'undefined') {
        const postcodeStr = localStorage.getItem(POSTCODE_KEY);
        return postcodeStr ? JSON.parse(postcodeStr) : DEFAULT_POSTCODE;
    } else {
        return DEFAULT_POSTCODE;
    }
};

// Store postcode in localStorage or remove if null
// WARNING: This should only be called by the global context provider (state must be kept in sync)
export const updatePostcodeStorage = (postcode_: number | null) => {
    if (postcode_ === null) {
        localStorage.removeItem(POSTCODE_KEY);
    } else {
        localStorage.setItem(POSTCODE_KEY, postcode_.toString());
    }
};

// --- GET ---

// Retrieve all postcodes
export const getPostcodes = async (apiURL: string) => {
    const response = await fetch(apiURL + POSTCODES_REQUEST, {
        // Query must not be static in case product data changes
        cache: 'no-store',
    });

    if (!response.ok) {
        return [];
    }

    const data: Postcode[] = await response.json();
    return data.map((postcode) => postcode.postcode);
};
