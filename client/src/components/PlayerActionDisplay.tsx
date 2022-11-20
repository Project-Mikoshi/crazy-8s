import React from 'react'
import { Fab, Grid, Stack, Typography } from '@mui/material'
import HandIcon from '@mui/icons-material/PanToolRounded'
import CardDisplay from './CardDisplay'
import { Card } from '@/types/card'

interface PlayerDisplayProps {
  cards: Array<Card>,
  playerName: string,
  onDiscardCard: (card: Card) => void
}

export default function (props: PlayerDisplayProps) {
  // == Props ================================
  const { cards, playerName, onDiscardCard } = props

  // == States ===============================

  // == Lifecycle ============================

  // == Functions ============================

  // == Actions ==============================
  function onSelect (index: number) {
    onDiscardCard(cards[index])
  }

  // == Template =============================
  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <Typography variant='h4'>{playerName}</Typography>
      </Grid>

      <Grid item xs={10}>
        <Stack direction='row'>
          {cards.map((card, index) => (
            <CardDisplay key={index} card={card} id={index} onSelect={onSelect}/>
          ))}
        </Stack>
      </Grid>

      <Grid item xs= {2} container alignItems='center'>
        <Fab variant='extended' color='primary'>
          <HandIcon sx={{ mr: 1 }} />
          <Typography>Draw Card</Typography>
        </Fab>
      </Grid>
    </Grid>
  )
}
