import React from 'react'
import AccountCircleRoundedIcon from '@mui/icons-material/AccountCircleRounded'
import KeyboardDoubleArrowRightIcon from '@mui/icons-material/KeyboardDoubleArrowRight'
import KeyboardDoubleArrowLeftIcon from '@mui/icons-material/KeyboardDoubleArrowLeft'
import VideogameAssetIcon from '@mui/icons-material/VideogameAsset'
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
  round: number,
  winner?: Player,
  direction?: Direction,
  topCardOnDiscardPile?: Card
}

export default function (props: DeckDisplayProps) {
  // == Props ================================
  const { remainingDeckCount, topCardOnDiscardPile, players, direction, round, winner } = props

  // == States ===============================

  // == Lifecycle ============================

  // == Functions ============================

  // == Actions ==============================

  // == Template =============================
  return (
    <Grid container spacing={2}>
      <Grid item xs={3}>
        <Box sx={{ bgcolor: 'background.paper'  }}>
          <List subheader={<ListSubheader>Score Board</ListSubheader>} disablePadding>
            {players.map(player => (
              <ListItem key={player.id}>
                <ListItemIcon>
                  <AccountCircleRoundedIcon />
                </ListItemIcon>
                <ListItemText primary={player.name} secondary={
                  <Typography color='primary' fontWeight={600} data-testid={`player-score-${player.id}`}>
                    {player.score}
                  </Typography>}
                />
              </ListItem>
            ))}
          </List>
        </Box>
      </Grid>

      <Grid item xs={3}>
        <Box sx={{ bgcolor: 'background.paper'  }}>
          <List subheader={<ListSubheader>Game Status</ListSubheader>} disablePadding>
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

            <ListItem>
              <ListItemIcon>
                <VideogameAssetIcon />
              </ListItemIcon>
              <ListItemText primary='Current Round' secondary={
                <Typography color='primary' fontWeight={600} data-testid='game-status-round'>
                  {round}
                </Typography>}
              />
            </ListItem>

            <ListItem>
              <ListItemIcon>
                <AccountCircleRoundedIcon />
              </ListItemIcon>
              <ListItemText primary='Winner' secondary={
                <Typography color='primary' fontWeight={600} data-testid='game-status-winner'>
                  {winner?.name}
                </Typography>}
              />
            </ListItem>
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
