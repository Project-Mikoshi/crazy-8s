import React from 'react'
import { Grid, Typography } from '@mui/material'
import CardDisplay from './CardDisplay'
import { Card } from '@/types/card'

interface DeckDisplayProps {
  remainingDeckCount: number,
  topCardOnDiscardPile: Card
}

export default function (props: DeckDisplayProps) {
  // == Props ================================
  const { remainingDeckCount, topCardOnDiscardPile } = props

  // == States ===============================

  // == Lifecycle ============================

  // == Functions ============================

  // == Actions ==============================

  // == Template =============================
  return (
    <Grid container spacing={2}>
      <Grid item xs={4}>
        <Typography variant='h6'>Remaining Deck Count: {remainingDeckCount}</Typography>
      </Grid>

      <Grid item xs={8}>
        <CardDisplay card={topCardOnDiscardPile} disabled />
      </Grid>

    </Grid>
  )
}
