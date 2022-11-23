import React from 'react'
import DirectionsRunRoundedIcon from '@mui/icons-material/DirectionsRunRounded'
import {
  Typography,
  CardMedia,
  Divider,
  Stack,
  List,
  ListSubheader,
  ListItem,
  ListItemIcon,
  ListItemText,
  Box,
  Grid
} from '@mui/material'
import CardDisplay from '@/components/CardDisplay'
import { Card } from '@/types/card'
import { Player } from '@/types/player'

interface DeckDisplayProps {
  players: Array<Player>,
  remainingDeckCount: number,
  topCardOnDiscardPile?: Card
}

export default function (props: DeckDisplayProps) {
  // == Props ================================
  const { remainingDeckCount, topCardOnDiscardPile, players } = props

  // == States ===============================

  // == Lifecycle ============================

  // == Functions ============================

  // == Actions ==============================

  // == Template =============================
  return (
    <Grid container spacing={2}>
      <Grid item xs={4}>
        <Box sx={{ width: '100%', maxWidth: 360, bgcolor: 'background.paper' }}>
          <List subheader={<ListSubheader>Score Board</ListSubheader>}>
            {players.map(player => (
              <ListItem key={player.id}>
                <ListItemIcon>
                  <DirectionsRunRoundedIcon />
                </ListItemIcon>
                <ListItemText primary={player.name} secondary={player.score} />
              </ListItem>
            ))}
          </List>
        </Box>
      </Grid>

      <Grid item xs={6}>
        <Stack sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }} spacing={2}>
          <Divider flexItem>
            <Typography>Top of Discarded Pile</Typography>
          </Divider>
          {topCardOnDiscardPile && (
            <CardMedia>
              <CardDisplay card={topCardOnDiscardPile} disabled />
            </CardMedia>
          )}

          <Divider flexItem>
            <Typography>Remaining Cards in Deck</Typography>
            <Typography variant='h6' color='primary'>{remainingDeckCount}</Typography>
          </Divider>
        </Stack>
      </Grid>
    </Grid>
  )
}
