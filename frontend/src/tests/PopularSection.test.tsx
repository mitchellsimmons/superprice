// import { vi, test, expect, describe, beforeEach } from 'vitest';
// import { render, screen, waitFor } from '@testing-library/react';
// import PopularSection from '@/app/PopularSection';
// import { mockPopularInventoryAll } from './mocks/server';

// import GlobalContext from '@/app/context';
// import { EnvProvider } from '@/app/EnvProvider';

// describe('PopularSection', () => {
//     const env = {
//         CLIENT_API_URL: 'http://localhost:8080',
//     };

//     test('PopularSection renders without crashing', async () => {
//         // const result = await PopularSection();
//         const page = render(
//             <EnvProvider env={env}>
//                 <GlobalContext>
//                     <PopularSection />
//                     <></>
//                 </GlobalContext>
//             </EnvProvider>
//         );
//         expect(page).toBeTruthy();
//     });

//     test(
//         'PopularSection displays correct amount of PopularCards when no location provided',
//         async () => {
//             //   Mock the InView component
//             vi.mock('react-intersection-observer', () => {
//                 return {
//                     InView: ({ children }: HTMLElement) => {
//                         return children;
//                     },
//                 };
//             });

//             render(
//                 <EnvProvider env={env}>
//                     <GlobalContext>
//                         <PopularSection />
//                         <></>
//                     </GlobalContext>
//                 </EnvProvider>
//             );

//             await waitFor(() => {
//                 const cards = screen.getAllByTestId('popular-card');
//                 expect(cards.length).toBe(mockPopularInventoryAll.length);
//             });
//         },
//         { timeout: 100000 }
//     );
// });
