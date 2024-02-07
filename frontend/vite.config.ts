/// <reference types="vitest" />
import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import { resolve } from 'path';

export default defineConfig(({command, mode}) => {
    const env = loadEnv(mode, process.cwd(), '');

    return {
        plugins: [react()],
        test: {
          globals: true,
          environment: 'happy-dom',
          setupFiles: './src/tests/setup.ts', //Setup files used for testing (where beforeAll() etc. are found)
        },
        resolve: {
          alias: [{ find: '@', replacement: resolve(__dirname, './src') }],
        },
    }
});
