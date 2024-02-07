import { vi, test, expect, describe, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import CategorySection from '@/app/CategorySection';
import { mockCategories } from './mocks/server';

describe('CategorySection', () => {
    test('CategorySection renders without crashing', async () => {
        const result = await CategorySection();
        const page = render(result);
        expect(page).toBeTruthy();
    });

    test(
        'CategorySection displays correct amount of CategoryCards',
        async () => {
            //   Mock the InView component
            vi.mock('react-intersection-observer', () => {
                return {
                    InView: ({ children }: HTMLElement) => {
                        return children;
                    },
                };
            });

            render(await CategorySection());

            await waitFor(() => {
                const cards = screen.getAllByTestId('category-card');
                expect(cards.length).toBe(mockCategories.length);
            });
        },
        { timeout: 100000 }
    );
});
