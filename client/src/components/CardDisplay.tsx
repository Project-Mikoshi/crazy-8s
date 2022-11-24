import React from 'react'
import Club from 'src/svgs/Club'
import Diamond from 'src/svgs/Diamond'
import Heart from 'src/svgs/Heart'
import Spade from 'src/svgs/Spade'
import King from 'src/svgs/King'
import Queen from 'src/svgs/Queen'
import Jack from 'src/svgs/Jack'
import { Card, CardSuit, CardValue } from '@/types/card'

interface CardDisplayProps {
  card: Card,
  disabled?: boolean,
  id?: number,
  onSelect?: (id: number) => void
}

export default function (props: CardDisplayProps) {
  // == Props ================================
  const { card, onSelect, id, disabled } = props

  // == States ===============================

  // == Computed Props =======================
  const { color, suit, value, isPlayable } = card
  const playable = isPlayable && !disabled
  const fillOpacity = playable ? 1 : 0.8
  const fillColor = playable ? '#b2dfdb' : '#e3f2fd'

  const CARD_SHAPE_SVGS = {
    [CardSuit.CLUBS]: <Club color={color} />,
    [CardSuit.DIAMONDS]: <Diamond color={color} />,
    [CardSuit.HEARTS]: <Heart color={color} />,
    [CardSuit.SPADES]: <Spade color={color} />,
    [CardValue.KING]: <King color={color} />,
    [CardValue.QUEEN]: <Queen color={color} />,
    [CardValue.JACK]: <Jack color={color} />
  }

  // == Lifecycle ============================

  // == Functions ============================
  function SpecialCardShape () {
    // @ts-expect-error
    return CARD_SHAPE_SVGS[value] || <></>
  }

  function CardShape () {
    return CARD_SHAPE_SVGS[suit]
  }

  // == Actions ==============================
  function handleClick () {
    if (onSelect && id !== undefined) {
      onSelect(id)
    }
  }

  // == Template =============================
  return (
    <svg
      className={`card-display-svg ${!playable ? 'disabled' : ''}`}
      viewBox='0 0 200 250'
      fillOpacity={fillOpacity}
      role='button'
      onClick={handleClick}
      data-testid={`${card.value}-${card.suit}-${playable ? 'enabled' : 'disabled'}`}
    >
      <g>
        <path
          fill={fillColor}
          d='M 199,10 C 199,5 195,1 190,1 L 10,1 C 5,1 1,5 1,10 L 1,240 C 1,245 5,249 10,249 L 190,249 C 195,249 199,245 199,240 L 199,10 z '
        />

        <text x={value === CardValue.TEN ? 10 : 15} y={40} fill={color} stroke={color} fontSize={20}>
          {value}
        </text>
        <text x={value === CardValue.TEN ? -190 : -184} y={-213} transform='scale(-1,-1)' fill={color} stroke={color} fontSize={20}>
          {value}
        </text>

        <CardShape />
        <SpecialCardShape />
      </g>
    </svg>
  )
}
