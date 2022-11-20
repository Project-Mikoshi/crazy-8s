import React, { useEffect, useState } from 'react'
import { Grid, LinearProgress, Typography, Button, TextField, Paper } from '@mui/material'
import ServerMessageBox from '@/components/ServerMessageBox'
import PlayerActionDisplay from '@/components/PlayerActionDisplay'
import DeckDisplay from '@/components/DeckDisplay'
import { Card } from '@/types/card'
import { GameState } from '@/types/game'
import { SocketEvent } from '@/types/api'

interface GameWindowProps {
  socket: any
}

export default function (props: GameWindowProps) {
  // == Props ================================
  const { socket } = props

  // == States ===============================
  const [remainingDeckCount, setRemainingDeckCount] = useState(0)
  const [topDiscardedCard, setTopDiscardedCard] = useState<Card>()
  const [gameState, setGameState] = useState<GameState>(GameState.READY_TO_JOIN)
  const [playerName, setPlayerName] = useState('')
  const [playerCards, setPlayerCards] = useState<Array<Card>>([])
  const [serverMessages, setServerMessages] = useState<Array<string>>([])

  // == Lifecycle ============================
  useEffect(() => {
    socket.on(SocketEvent.MESSAGE, (message: string) => {
      serverMessages.push(message)
      setServerMessages(serverMessages)
    })

    socket.on(SocketEvent.GAME_JOIN, () => {
      setGameState(GameState.WAITING)
    })

    socket.on(SocketEvent.GAME_START, () => {
      setGameState(GameState.STARTED)
    })

    socket.on(SocketEvent.GAME_DEAL_CARDS, (cards: Array<Card>) => {
      setPlayerCards(cards)
    })

    socket.on(SocketEvent.GAME_UPDATE_CARDS, (cards: Array<Card>) => {
      setPlayerCards(cards)
    }),

    socket.on(SocketEvent.GAME_UPDATE_DISCARD_PILE, (card: Card) => {
      setTopDiscardedCard(card)
    })

    socket.on(SocketEvent.GAME_UPDATE_REMAINING_DECK, (count: number) => {
      setRemainingDeckCount(count)
    })
  }, [])

  // == Functions ============================

  // == Actions ==============================
  function sendJoinGameRequest () {
    socket.emit(SocketEvent.GAME_JOIN, playerName)
  }

  function updatePlayerName (e: React.ChangeEvent<HTMLInputElement>) {
    setPlayerName(e.target.value)
  }

  function discardCard (card: Card) {
    socket.emit(SocketEvent.GAME_DISCARD_CARD, card)
  }

  // == Template =============================
  switch (gameState) {
    case (GameState.READY_TO_JOIN): {
      return (
        <>
          <Grid item xs={10}>
            <Typography className='typewriter' variant='h4'>Welcome to the Crazy 8s online game.</Typography>
          </Grid>

          <Grid item xs={6}>
            <Paper>
              <TextField
                id='player-name-input-box'
                fullWidth
                placeholder='Enter your name'
                value={playerName}
                onChange={updatePlayerName}
              />
            </Paper>
          </Grid>

          <Grid item xs={10} textAlign='center'>
            <Button id='join-button' variant='contained' onClick={sendJoinGameRequest} disabled={!playerName}>Join Game</Button>
          </Grid>
        </>
      )
    }

    case (GameState.WAITING): {
      return (
        <>
          <Grid item xs={12}>
            <Typography variant='h6'>Waiting for more players to join</Typography>
            <LinearProgress />
          </Grid>
          <Grid item xs={2}>
            <ServerMessageBox messages={serverMessages} />
          </Grid>
        </>
      )
    }

    case (GameState.STARTED): {
      return (
        <>
          <Grid item md={8}>
            <DeckDisplay topCardOnDiscardPile={topDiscardedCard} remainingDeckCount={remainingDeckCount} />
          </Grid>
          <Grid item md={4}>
            <ServerMessageBox messages={serverMessages} />
          </Grid>
          <Grid item md={12}>
            <PlayerActionDisplay cards={playerCards} playerName={playerName} onDiscardCard={discardCard} />
          </Grid>
        </>
      )
    }
  }
}
