import { vi, describe, expect, test } from 'vitest';
import { render, screen } from '@testing-library/react';
import ProductPage from '@/app/product/[id]/page';

describe('Testing Product Page', async () => {
    test('Test structure', async () => {
        const component = await ProductPage({ params: { id: '1' } });
        const page = render(component);
        expect(page).toBeTruthy();
    });
});
