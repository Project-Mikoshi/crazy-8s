import React from 'react'
import { Container, Stack, Typography } from '@mui/material'
import CardDisplay from './CardDisplay'
import { Card } from '@/types/card'

interface DeckDisplayProps {
  cards: Array<Card>
}

export default function (props: DeckDisplayProps) {
  // == Props ================================
  const { cards } = props

  // == States ===============================

  // == Lifecycle ============================

  // == Functions ============================

  // == Actions ==============================

  // == Template =============================

  return (
    <Container>
      <Typography variant='h5'>Your Cards</Typography>
      <br />
      <Stack direction='row'>
        {cards.map((card, index) => (
          <CardDisplay key={index} card={card} isSelected={index === 0} />
        ))}
      </Stack>
    </Container>
  )
}
