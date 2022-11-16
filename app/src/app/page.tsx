'use client';

import React, { useEffect, useState } from 'react'
import { Button, Grid, Typography } from '@mui/material'
import { SERVER_CONFIG } from '@/constants/network'
import { io, Socket } from "socket.io-client"
import './styles.scss'
import { GameEvent } from '@/types/api';

export default function Home () {
  // == Props ================================

  // == Hooks ================================
  const [socket, setSocket] = useState<Socket>()

  console.log(socket)

  useEffect(() => {
    if (!socket) {
      setSocket(io(SERVER_CONFIG.WEBSOCKET_SERVER))
      startConnection()
    }

    return () => {
      endConnection()
    }
  }, [socket?.connected])

  // == Functions ============================
  function startConnection () {
    socket?.connect()

    socket?.on(GameEvent.MESSAGE, (message: string) => {
      console.log('server message', message)
    })
  }

  function endConnection () {
    socket?.off(GameEvent.MESSAGE)
  }

  // == Actions ==============================
  function sendMessage () {
    socket?.emit(GameEvent.MESSAGE, 'hello world')
  }

  // == Template =============================
  return (
    <>
      <Grid container justifyContent='center' alignItems='center' height='100%' width='100%' padding={5}>
        {!socket?.connected && (
          <Grid item md={10} lg={7} xl={6}>
            <Typography className='typewriter' variant='h4'>Welcome to the Crazy 8s online game.</Typography>
          </Grid>
        )}

        {socket?.connected && (
          <Grid item md={10} lg={7} xl={6}>
            <Button variant='contained' onClick={sendMessage}>Click me</Button>
          </Grid>
        )}
      </Grid>
    </>
  )
}
