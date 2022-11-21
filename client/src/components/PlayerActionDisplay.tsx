import React from 'react'
import { Fab, Grid, Stack, Tooltip, Typography } from '@mui/material'
import HandIcon from '@mui/icons-material/PanToolRounded'
import CardDisplay from './CardDisplay'
import { Card } from '@/types/card'

interface PlayerDisplayProps {
  isPlaying: boolean,
  cards: Array<Card>,
  playerName: string,
  onDiscardCard: (card: Card) => void
}

export default function (props: PlayerDisplayProps) {
  // == Props ================================
  const { cards, playerName, onDiscardCard, isPlaying } = props

  // == Computed Props =======================
  const noCardToPlay = cards.every(card => !card.isPlayable)

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
        {!isPlaying && (
          <Typography variant='subtitle1'>You must wait for other player to finish their turn</Typography>
        )}
      </Grid>

      <Grid item xs={10} container sx={{ overflowX: 'auto', overflowY: 'hidden' }}>
        <Stack direction='row'>
          {cards.map((card, index) => (
            <CardDisplay disabled={!isPlaying} key={index} card={card} id={index} onSelect={onSelect}/>
          ))}
        </Stack>
      </Grid>

      {isPlaying && noCardToPlay && (
        <Grid item xs= {2} container justifyContent='center' flexDirection='column'>
          <Tooltip title='draw a new card from deck'>
            <Fab variant='extended' color='primary'>
              <HandIcon />
            </Fab>
          </Tooltip>
          <Typography variant='body1'>You need to draw a card</Typography>
        </Grid>
      )}
    </Grid>
  )
}
