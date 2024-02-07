import React from "react";
import type { Metadata } from 'next'
import { Inter } from 'next/font/google'

import './globals.css'
import AppProvider from './context';
import { EnvProvider } from './EnvProvider';
import { Footer, Navbar } from '@/components';

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'SuperPrice - Your Ultimate Grocery Price Comparison and Delivery Solution',
  description: 'Experience seamless grocery shopping with SuperPrice - the all-in-one platform that lets you compare prices, find the best deals, and have your groceries delivered right to your doorstep.',
}

export default function RootLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    const env = {
        CLIENT_API_URL: process.env.CLIENT_API_URL,
    };

    return (
        <html lang='en'>
            <body className={inter.className}>
                <EnvProvider env={env}>
                    <AppProvider>
                        <Navbar />
                        {children}
                        <Footer />
                    </AppProvider>
                </EnvProvider>
            </body>
        </html>
    );
}
