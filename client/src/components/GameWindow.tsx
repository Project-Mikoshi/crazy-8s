import React, { useEffect, useState } from 'react'
import { Grid, LinearProgress, Typography, Button, TextField, Paper } from '@mui/material'
import ServerMessageBox from '@/components/ServerMessageBox'
import { GameState } from '@/types/game'
import { SocketEvent } from '@/types/api'

interface GameWindowProps {
  socket: any
}

export default function (props: GameWindowProps) {
  // == Props ================================
  const { socket } = props

  // == States ===============================
  const [gameState, setGameState] = useState<GameState>(GameState.READY_TO_JOIN)
  const [playerName, setPlayerName] = useState('')
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
  }, [])

  // == Functions ============================

  // == Actions ==============================
  function sendJoinGameRequest () {
    socket.emit(SocketEvent.GAME_JOIN, playerName)
  }

  function updatePlayerName (e: React.ChangeEvent<HTMLInputElement>) {
    setPlayerName(e.target.value)
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

    default: {
      return (
        <>
        </>
      )
    }
  }
}
