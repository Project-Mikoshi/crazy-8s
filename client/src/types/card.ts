/* eslint-disable no-unused-vars */
export enum CardSuit {
  CLUBS = 'clubs',
  HEARTS = 'hearts',
  SPADES = 'spades',
  DIAMONDS = 'diamonds'
}

export enum CardValue {
  A = 'A',
  TWO = 2,
  THREE = 3,
  FOUR = 4,
  FIVE = 5,
  SIX = 6,
  SEVEN = 7,
  EIGHT = 8,
  NINE = 9,
  TEN = 10,
  JACK = 'J',
  QUEEN = 'Q',
  KING = 'K'
}

export enum CardColor {
  RED = '#ef5350',
  BLACK = '#212121'
}

export interface Card {
  value: CardValue,
  suit: CardSuit,
  color: CardColor
}
