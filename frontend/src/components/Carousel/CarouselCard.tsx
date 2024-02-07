export const dynamic = 'force-dynamic'

import React from 'react';
import { InView } from 'react-intersection-observer';

import styles from './CarouselCard.module.css';

interface PropsType {
  firstOnPage: boolean;
  lastOnPage: boolean;
  handleInView: Function;
  children: React.ReactNode[] | React.ReactNode;
}

const CarouselCard = ({
  firstOnPage,
  lastOnPage,
  handleInView,
  children,
}: PropsType) => {
  // Ensure scroll snaps to the first card on each page
  const style = firstOnPage
    ? ({
        scrollSnapAlign: 'start',
      } as React.CSSProperties)
    : {};

  // The InView component wraps the last card on each page
  // It tells an IntersectionObserver to monitor the element
  // When the intersection state changes, we call a function to update the page index
  return (
    <article
      className={styles.carousel_card}
      style={style}
      data-testid="carousel-card"
    >
      {lastOnPage ? (
        <InView
          as="div"
          onChange={(inView, entry) => handleInView(inView, entry)}
          className={styles.container}
        >
          {children}
        </InView>
      ) : (
        <div className={styles.container}>{children}</div>
      )}
    </article>
  );
};

export default CarouselCard;
