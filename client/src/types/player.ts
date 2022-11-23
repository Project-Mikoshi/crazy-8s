import { Card } from '@/types/card'

export interface Player {
  id: string,
  name: string,
  score: number
  cardsHeld: Array<Card>,
  drawnCardCount: number,
  discardCardCount: number
}
