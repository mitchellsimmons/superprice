export const dynamic = 'force-dynamic'

import { IoMail, IoLogoFacebook } from 'react-icons/io5';
import { AiFillInstagram } from 'react-icons/ai';
import { FaYoutube } from 'react-icons/fa';

import styles from './Footer.module.css';

const Footer = () => { //TODO: Test for display
    return (
        <section className={styles.footer}>
            <div className={styles.footer_links_section}>
                <h4>customer service</h4>
                <a href='#footer'>contact us</a>
                <a href='#footer'>help center</a>
                <a href='#footer'>return policy</a>
                <a href='#footer'>feedback</a>
            </div>
            <div className={styles.footer_links_section}>
                <h4>shop groceries</h4>
                <a href='#footer'>my account</a>
                <a href='#footer'>categories</a>
                <a href='#footer'>deliveries</a>
            </div>
            <div className={styles.footer_links_section}>
                <h4>about us</h4>
                <a href='#footer'>our mission</a>
                <a href='#footer'>careers</a>
                <a href='#footer'>partners</a>
            </div>
            <div className={styles.footer_links_section}>
                <h4>follow us</h4>
                <div className={styles.footer_contact_icons}>
                    <IoMail />
                    <IoLogoFacebook />
                    <AiFillInstagram />
                    <FaYoutube />
                </div>
            </div>
        </section>
    );
};

export default Footer;
