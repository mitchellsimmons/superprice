'use client';

export const dynamic = 'force-dynamic'

import React, { useEffect, useState, useRef } from 'react';

import { useGlobalContext } from '@/app/context';
import { CarouselCard } from '@/components';
import styles from './Carousel.module.css';
import cardStyles from './CarouselCard.module.css';

// --- Component ---

interface PropsType {
  mobileCardsPerPage: number;
  break0CardsPerPage: number;
  break1CardsPerPage: number;
  break2CardsPerPage: number;
  break3CardsPerPage: number;
  children: React.ReactNode[];
}

const Carousel = ({
  mobileCardsPerPage,
  break0CardsPerPage,
  break1CardsPerPage,
  break2CardsPerPage,
  break3CardsPerPage,
  children,
}: PropsType) => {
  const { isBreak0, isBreak1, isBreak2, isBreak3 } = useGlobalContext();
  const [pageIndex, setPageIndex] = useState(0);
  const carousel = useRef<HTMLDivElement>(null);

  // Reset the page index when the media changes
  useEffect(() => setPageIndex(0), [isBreak0, isBreak1, isBreak2, isBreak3]);

  // Update number of cards per page based on media size
  let cardsPerPage = mobileCardsPerPage;
  if (isBreak3) cardsPerPage = break3CardsPerPage;
  else if (isBreak2) cardsPerPage = break2CardsPerPage;
  else if (isBreak1) cardsPerPage = break1CardsPerPage;
  else if (isBreak0) cardsPerPage = break0CardsPerPage;
  let cardCount = children.length;
  let pageCount = Math.ceil(cardCount / cardsPerPage);

  // Fill the last page
  // If there are 4 cards per page and 5 cards total,
  // this will ensure the second page displays only one card
  // Default behaviour is to fill the second page with cards from the first page
  // NOTE: Do not add objects to children array (this messes up React rendering)
  let fillers: React.ReactNode[] = [];
  let fillCount = cardsPerPage - (cardCount % cardsPerPage);
  if (fillCount !== cardsPerPage) {
    for (let i = 0; i < fillCount; ++i) {
      fillers.push(<div />);
    }
  }

  // Update page index when page button is clicked
  const handleClick = (index: number) => {
    // Scroll to the first card on the current page
    let firstCardIndex = index * cardsPerPage;
    let cards: NodeListOf<HTMLElement> | undefined =
      carousel?.current?.querySelectorAll(`.${cardStyles.carousel_card}`);

    if (cards) {
      carousel?.current?.scrollTo({
        left: cards[firstCardIndex].offsetLeft,
        behavior: 'smooth',
      });
    }
  };

  // Update page index when the last card on the the page is scrolled into view
  const handleInView = (
    inView: boolean,
    entry: IntersectionObserverEntry,
    cardIndex: number
  ) => {
    if (inView) {
      setPageIndex(Math.ceil((cardIndex + 1) / cardsPerPage) - 1);
    }
  };

  const getButtons = () => {
    let buttons = [];

    for (let i = 0; i < pageCount; ++i) {
      buttons.push(
        <button
          key={i}
          onClick={() => handleClick(i)}
          className={`${styles.carousel_btn} ${
            i === pageIndex && styles.active_btn
          }`}
        ></button>
      );
    }

    return buttons;
  };

  // Card count is used to determine a per card width
  const style = {
    '--card-count': cardsPerPage,
  } as React.CSSProperties;

  return (
    <>
      <div className={styles.carousel} ref={carousel} style={style}>
        {children.map((child, index) => {
          return (
            <CarouselCard
              key={index}
              firstOnPage={index % cardsPerPage === 0}
              lastOnPage={
                (index + 1) % cardsPerPage === 0 ||
                index === children.length - 1
              }
              handleInView={(
                inView: boolean,
                entry: IntersectionObserverEntry
              ) => handleInView(inView, entry, index)}
            >
              {child}
            </CarouselCard>
          );
        })}
        {fillers.map((child, index) => {
          index += children.length;
          return (
            <CarouselCard
              key={index}
              firstOnPage={index % cardsPerPage === 0}
              lastOnPage={
                (index + 1) % cardsPerPage === 0 ||
                index === children.length - 1
              }
              handleInView={(
                inView: boolean,
                entry: IntersectionObserverEntry
              ) => handleInView(inView, entry, index)}
            >
              {child}
            </CarouselCard>
          );
        })}
      </div>
      <div className={styles.carousel_selector}>{getButtons()}</div>
    </>
  );
};

export default Carousel;
