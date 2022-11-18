import React, { useEffect, useState } from 'react'
// @ts-expect-error
import io from 'socket.io-client'
import { Button, Grid, LinearProgress, Paper, TextField, Typography } from '@mui/material'
import { SOCKET_SERVER_ADDRESS } from '@/constants/network'
import { SocketEvent } from '@/types/api'
import { Card } from '@/types/card'
import { GameState } from '@/types/game'
import MessageBox from '@/components/MessageBox'
import DeckDisplay from '@/components/DeckDisplay'
import './styles.scss'

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
        transports: ['websocket']
      }))
    }

    if (socket) {
      startConnection()
    }
  }, [socket])

  // == Functions ============================
  function startConnection () {
    socket.on(SocketEvent.CONNECTION, () => {
      setConnected(true)
    })

    socket.on(SocketEvent.MESSAGE, (message: string) => {
      serverMessages.push(message)
      setServerMessages(serverMessages)
    })

    socket.on(SocketEvent.GAME_JOIN, () => {
      setGameState(GameState.READY)
    })

    socket.on(SocketEvent.GAME_START, () => {
      setGameState(GameState.STARTED)
    })

    socket.on(SocketEvent.GAME_DEAL_CARDS, (cards: Array<Card>) => {
      setPlayerDeck(cards)
    })

    socket.on(SocketEvent.DISCONNECT, () => {
      setConnected(false)
    })
  }

  // == Actions ==============================
  function sendJoinGameRequest () {
    socket.emit(SocketEvent.GAME_JOIN, playerName)
  }

  // == Template =============================
  function ServerErrorMessage () {
    return (
      <>
        <Grid item md={10} textAlign='center'>
          <Typography variant='h3'>Error: unable to connect to server</Typography>
        </Grid>
      </>
    )
  }

  function GameWaitingRoom () {
    return (
      <>
        <Grid item md={12}>
          <Typography variant='h6'>Waiting for more players to join</Typography>
          <LinearProgress />
        </Grid>
        <Grid item md={2}>
          <MessageBox messages={serverMessages} />
        </Grid>
      </>
    )
  }

  function GameWindow () {
    return (
      <>
        <Grid item md={8}>
          {/* game box */}
        </Grid>
        <Grid item md={4}>
          <MessageBox messages={serverMessages} />
        </Grid>
        <Grid item md={12}>
          <DeckDisplay deck={playerDeck} />
        </Grid>
      </>
    )
  }

  return (
    <div className='app'>
      <div className='outlet'>
        <Grid container justifyContent='center' alignItems='center' height='100%' width='100%' padding={5}>
          {!isConnected && <ServerErrorMessage />}
          {isConnected && gameState === GameState.WAITING && (
            <>
              <Grid item md={10}>
                <Typography className='typewriter' variant='h4'>Welcome to the Crazy 8s online game.</Typography>
              </Grid>

              <Grid item md={6}>
                <Paper>
                  <TextField
                    id='player-name-input-box'
                    fullWidth
                    placeholder='Enter your name'
                    value={playerName}
                    onChange={(e) => setPlayerName(e.target.value)}
                  />
                </Paper>
              </Grid>

              <Grid item md={10} textAlign='center'>
                <Button id='join-button' variant='contained' onClick={sendJoinGameRequest} disabled={!playerName}>Join Game</Button>
              </Grid>
            </>
          )}

          {isConnected && gameState === GameState.READY && <GameWaitingRoom />}
          {isConnected && gameState === GameState.STARTED && <GameWindow />}
        </Grid>
      </div>
    </div>
  )
}
