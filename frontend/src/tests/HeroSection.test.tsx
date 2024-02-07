//HeroSection.test.tsx
import { test, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import HeroSection from '@/app/HeroSection';

// Test that HeroSection displays the correct text
test('HeroSection displays Super Welcome', () => {
    render(<HeroSection />);
    expect(screen.getByText('Super Welcome')).toBeTruthy();
});
