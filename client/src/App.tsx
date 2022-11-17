import React, { useEffect, useState } from 'react'
// @ts-expect-error
import io from 'socket.io-client'
import { Button, Grid, Paper, TextField, Typography } from '@mui/material'
import { SOCKET_SERVER_ADDRESS } from '@/constants/network'
import { SocketEvent } from '@/types/api'
import { Card, CardColor, CardSuit, CardValue } from '@/types/card'
import { GameState } from '@/types/game'
import MessageBox from '@/components/MessageBox'
import DeckDisplay from '@/components/DeckDisplay'
import './App.scss'

export default function App () {
  // == Props ================================

  // == States ===============================
  const [socket, setSocket] = useState<any>()

  const [gameState, setGameState] = useState<GameState>(GameState.WAITING)
  const [isConnected, setConnected] = useState(false)

  const [serverMessages, setServerMessages] = useState<Array<string>>([])

  const [playerName, setPlayerName] = useState('')
  const [playerDeck, setPlayerDeck] = useState<Array<Card>>([])

  // == Lifecycle ===========================
  useEffect(() => {
    if (!socket) {
      setSocket(io(SOCKET_SERVER_ADDRESS,  {
        transports: ['websocket'],
        forceNew: true
      }))
    }

    if (socket) {
      startConnection()
    }
  }, [socket])

  // == Functions ============================
  function startConnection () {
    socket.on(SocketEvent.CONNECTION, function () {
      setConnected(true)
    })

    socket.on(SocketEvent.MESSAGE, (message: string) => {
      setServerMessages([...serverMessages, message])
    })

    socket.on(SocketEvent.JOIN_GAME, () => {
      setGameState(GameState.READY)

      setPlayerDeck([
        {
          value: CardValue.A,
          color: CardColor.RED,
          suit: CardSuit.SPADES
        },
        {
          value: CardValue.TEN,
          color: CardColor.BLACK,
          suit: CardSuit.HEARTS
        },
        {
          value: CardValue.NINE,
          color: CardColor.RED,
          suit: CardSuit.DIAMONDS
        },
        {
          value: CardValue.JACK,
          color: CardColor.BLACK,
          suit: CardSuit.CLUBS
        }
      ])
    })

    socket.on(SocketEvent.DISCONNECT, function () {
      setConnected(false)
    })
  }

  // == Actions ==============================
  function sendJoinGameRequest () {
    socket.emit(SocketEvent.JOIN_GAME, playerName)
  }

  // == Template =============================

  return (
    <div className='app'>
      <div className='outlet'>
        <Grid container justifyContent='center' alignItems='center' height='100%' width='100%' padding={5}>
          {!isConnected && (
            <>
              <Grid item md={10} textAlign='center'>
                <Typography variant='h3'>Error: unable to connect to server</Typography>
              </Grid>
            </>
          )}

          {isConnected && gameState === GameState.WAITING && (
            <>
              <Grid item md={10}>
                <Typography className='typewriter' variant='h4'>Welcome to the Crazy 8s online game.</Typography>
              </Grid>

              <Grid item md={6}>
                <Paper sx={{ backgroundColor: 'aliceblue' }}>
                  <TextField
                    value={playerName}
                    placeholder='Enter your name'
                    fullWidth
                    onChange={(e) => setPlayerName(e.target.value)}
                  />
                </Paper>
              </Grid>

              <Grid item md={10} textAlign='center'>
                <Button variant='contained' onClick={sendJoinGameRequest} disabled={!playerName}>Join Game</Button>
              </Grid>
            </>
          )}

          {isConnected && gameState === GameState.READY && (
            <>
              <Grid item md={10}>
              </Grid>
              <Grid item md={2}>
                <MessageBox messages={serverMessages} />
              </Grid>
              <Grid item md={12}>
                <DeckDisplay deck={playerDeck} />
              </Grid>
            </>
          )}
        </Grid>
      </div>
    </div>
  )
}
