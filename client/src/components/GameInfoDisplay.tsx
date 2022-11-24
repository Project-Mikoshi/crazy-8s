import React from 'react'
import AccountCircleRoundedIcon from '@mui/icons-material/AccountCircleRounded'
import KeyboardDoubleArrowRightIcon from '@mui/icons-material/KeyboardDoubleArrowRight'
import KeyboardDoubleArrowLeftIcon from '@mui/icons-material/KeyboardDoubleArrowLeft'
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
import { Direction } from '@/types/game'

interface DeckDisplayProps {
  players: Array<Player>,
  remainingDeckCount: number,
  direction?: Direction,
  topCardOnDiscardPile?: Card
}

export default function (props: DeckDisplayProps) {
  // == Props ================================
  const { remainingDeckCount, topCardOnDiscardPile, players, direction } = props

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
                  <AccountCircleRoundedIcon color='primary' />
                </ListItemIcon>
                <ListItemText primary={player.name} secondary={<Typography color='primary' fontWeight={600}>{player.score}</Typography>} />
              </ListItem>
            ))}

            <ListItem>
              <ListItemIcon>
                {direction === Direction.NORMAL && <KeyboardDoubleArrowRightIcon />}
                {direction === Direction.REVERSE && <KeyboardDoubleArrowLeftIcon />}
              </ListItemIcon>
              <ListItemText
                primary='Playing Direction'
                secondary={
                  <Typography color='primary' fontWeight={600} data-testid='game-status-direction'>
                    {direction}
                  </Typography>}
              />
            </ListItem>
          </List>
        </Box>
      </Grid>

      <Grid item xs={8}>
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
