import { describe, expect, test } from 'vitest';
import { render, screen } from '@testing-library/react';
import Home from '@/app/page';
import ProductPage from '@/app/product/[id]/page';

describe('Testing Landing Page', () => {
    test('Test structure', async () => {
        // If the names or structure of the children components change, this test will need to be updated
        const expectedChildren = ['HeroSection', 'PopularSection', 'CategorySection'];

        const result = await Home();
        const renderedChildren = result.props.children;

        // Test parent component
        expect(result.type).toBe('main');

        // Test children components
        for (let i = 0; i < renderedChildren.length; i++) {
            expect(renderedChildren[i].type.name).toBe(expectedChildren[i]);
        }
    });
});
