import { setupServer } from 'msw/node';
import { rest } from 'msw';

process.env.API_URL = 'http://localhost:8080';
process.env.CLIENT_API_URL = 'http://localhost:8080';

// TODO: JSON format example - NOT FINAL FORMAT!!!

const mockInventoryProductId1 = [
  {
    inventoryId: 277,
    productId: 1,
    storeId: 2,
    categoryId: 11,
    name: 'Cavendish Bananas Per Kg',
    description: 'Ripe and fresh bananas',
    time: '08:56:42',
    priceInCents: 500,
    stockLevel: 127,
    storeName: 'Grocery Haven',
    postcode: 3000,
  },
  {
    inventoryId: 3,
    productId: 1,
    storeId: 3,
    categoryId: 11,
    name: 'Cavendish Bananas Per Kg',
    description: 'Ripe and fresh bananas',
    time: '08:38:41',
    priceInCents: 400,
    stockLevel: 84,
    storeName: 'MegaMart',
    postcode: 3000,
  },
  {
    inventoryId: 4,
    productId: 1,
    storeId: 4,
    categoryId: 11,
    name: 'Cavendish Bananas Per Kg',
    description: 'Ripe and fresh bananas',
    time: '05:56:31',
    priceInCents: 415,
    stockLevel: 122,
    storeName: 'FreshGrocer',
    postcode: 3000,
  },
];

export const mockPopularInventoryAll = [
  {
    inventoryId: 290,
    productId: 3,
    storeId: 5,
    categoryId: 13,
    name: 'Naval Oranges Per Kg',
    description: 'Tangy and juicy oranges',
    time: '10:57:41',
    priceInCents: 300,
    stockLevel: 63,
    storeName: 'QuickGrocery',
    postcode: 3000,
  },
  {
    inventoryId: 28,
    productId: 6,
    storeId: 3,
    categoryId: 16,
    name: 'Strawberries Punnet 300g',
    description: 'Plump and juicy strawberries',
    time: '12:08:56',
    priceInCents: 160,
    stockLevel: 10,
    storeName: 'MegaMart',
    postcode: 3000,
  },
  {
    inventoryId: 302,
    productId: 6,
    storeId: 2,
    categoryId: 16,
    name: 'Strawberries Punnet 300g',
    description: 'Plump and juicy strawberries',
    time: '11:47:11',
    priceInCents: 160,
    stockLevel: 78,
    storeName: 'Grocery Haven',
    postcode: 3000,
  },
];

const mockProducts = {
  products: [
    {
      ID: '000000000000',
      name: 'Bananas',
      category: 'Fruit',
      storeID: '000000',
      storeName: 'Aldi',
      storePostcode: 3012,
      price: 2.75,
      unit: 'kg',
      stock: 100,
    },
    {
      ID: '000000000000',
      name: 'Bananas',
      category: 'Fruit',
      storeID: '000001',
      storeName: 'Coles',
      storePostcode: 3000,
      price: 2.8,
      unit: 'kg',
      stock: 230,
    },
  ],
};

export const mockCategories = [
  {
    id: 1,
    name: 'Cavendish Bananas Per Kg',
    categoryId: 11,
    description: 'Ripe and fresh bananas',
  },
  {
    id: 2,
    name: 'Jazz Apples Per Kg',
    categoryId: 12,
    description: 'Crisp and sweet red apples',
  },
  {
    id: 3,
    name: 'Naval Oranges Per Kg',
    categoryId: 13,
    description: 'Tangy and juicy oranges',
  },
];

//Define handlers here - these will mock the responses of the backend server to API calls
export const handlers = [
  // TODO: remove this example handler when conducting actual testing
  //E.G.: Handler to mock the backend server response to an HTTP GET request with status 200 (OK).
  rest.get(process.env.API_URL + '/v1/products', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(mockProducts));
  }),

  rest.get(process.env.API_URL + '/v1/categories', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(mockCategories));
  }),

  rest.get(process.env.API_URL + '/v1/products/inventory', (req, res, ctx) => {
    req.url.searchParams.set('id', '1');
    return res(ctx.status(200), ctx.json(mockInventoryProductId1));
  }),

  rest.get(process.env.API_URL + '/v1/products/popular', (req, res, ctx) => {
    console.log('popular endpoint hit');
    return res(ctx.status(200), ctx.json(mockPopularInventoryAll));
  }),
];

// This configures a Service Worker with the given request handlers.
export const server = setupServer(...handlers);
