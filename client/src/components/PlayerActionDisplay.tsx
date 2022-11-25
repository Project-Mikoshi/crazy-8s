import React from 'react'
import { Scroll } from '@mikoshi/core-components'
import { Fab, Grid, Stack, Tooltip, Typography } from '@mui/material'
import HandIcon from '@mui/icons-material/AddRounded'
import CardDisplay from './CardDisplay'
import { Card } from '@/types/card'

interface PlayerDisplayProps {
  isPlaying: boolean,
  isAbleToDraw: boolean,
  cards: Array<Card>,
  playerName: string,
  deckCount: number,
  onDiscardCard: (card: Card) => void,
  onDrawCard: () => void
}

export default function (props: PlayerDisplayProps) {
  // == Props ================================
  const { cards, deckCount, playerName, onDiscardCard, isPlaying, onDrawCard, isAbleToDraw } = props

  // == Computed Props =======================

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
        <Typography variant='h4' color='primary' data-testid='user-name-display-text'>{playerName}</Typography>
        {!isPlaying && (
          <Typography variant='subtitle1' color='warning' data-testid='user-status-waiting'>You must wait for other player to finish their turn</Typography>
        )}
        {isPlaying && (
          <Typography variant='subtitle1' color='success' data-testid='user-status-playing'>Your turn</Typography>
        )}
      </Grid>

      <Grid item xs={10}>
        <Scroll>
          <Stack direction='row' sx={{ margin: '1rem', width: 'fit-content' }} spacing={1}>
            {cards.map((card, index) => (
              <CardDisplay disabled={!isPlaying} key={index} card={card} id={index} onSelect={onSelect}/>
            ))}
          </Stack>
        </Scroll>
      </Grid>

      {isPlaying && isAbleToDraw && deckCount > 0 && (
        <Grid item xs={2} container justifyContent='center' alignItems='center' flexDirection='column'>
          <Tooltip title='draw a new card from deck'>
            <Fab color='primary' onClick={onDrawCard} id='draw-card-button'>
              <HandIcon />
            </Fab>
          </Tooltip>
          <Typography>You need to draw a card</Typography>
        </Grid>
      )}
    </Grid>
  )
}
