'use client';

import { FaLocationDot, FaXmark, FaCheck } from 'react-icons/fa6';
import { useEffect, useState } from 'react';

import { useEnvContext } from '@/app/EnvProvider';
import { useGlobalContext } from '@/app/context';
import { getPostcodes } from '@/api/postcodes';
import styles from './PostcodeSelector.module.css';

const PostcodeSelector = () => {
    const { CLIENT_API_URL } = useEnvContext();
    const { postcode, setPostcode } = useGlobalContext();
    const [value, setValue] = useState<number | null>(null);
    const [isValid, setIsValid] = useState(postcode !== null);
    const [isEditing, setIsEditing] = useState(false);

    const handleFocus = () => {
        // Toggle edit state
        setIsEditing(true);
    };

    const handleBlur = () => {
        // Toggle edit state
        setIsEditing(false);

        if (value === null) {
            // Unset postcode
            setPostcode(null);
        } else if (isValid) {
            // Set postcode
            setPostcode(value);
        } else {
            // Reset value to last valid postcode (or null)
            setValue(postcode);
        }
    };

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const valueStr = event.target.value.slice(0, 4);
        const valueNum = Number.parseInt(valueStr) || null;
        setValue(valueNum);
    };

    // Verify what the user has typed is a valid postcode
    useEffect(() => {
        if (value === null || value.toString().length !== 4) {
            setIsValid(false);
            return;
        }

        const verifyPostcode = async () => {
            const postcodes = await getPostcodes(CLIENT_API_URL || '');

            // Only cache valid postcode
            if (postcodes.includes(value)) {
                setIsValid(true);
            }
        };

        verifyPostcode();
    }, [value]);

    // Local storage is not available on initial mount
    // We must ensure state updates after global context updates
    useEffect(() => {
        setValue(postcode);
    }, [postcode]);

    return (
        <div
            className={`${styles.postcode_container} ${
                postcode !== null && styles.edited
            } ${isEditing && styles.editing}`}
        >
            <input
                type='text'
                value={value?.toString() ?? ''}
                placeholder={isEditing && value === null ? '3000' : ''}
                onChange={handleChange}
                onFocus={handleFocus}
                onBlur={handleBlur}
                className={`${styles.postcode_input} ${
                    postcode !== null && styles.edited
                } ${isEditing && styles.editing}`}
            />
            <FaLocationDot className={styles.location} />
            {isEditing && value !== null && !isValid && (
                <FaXmark className={styles.cross} />
            )}
            {isEditing && value !== null && isValid && (
                <FaCheck className={styles.check} />
            )}
        </div>
    );
};

export default PostcodeSelector;
