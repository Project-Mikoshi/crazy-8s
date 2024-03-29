import React, { useEffect, useState } from 'react'
// @ts-expect-error
import io from 'socket.io-client'
import { AppBar, Grid, Toolbar, Typography } from '@mui/material'
import { SOCKET_SERVER_ADDRESS } from '@/constants/network'
import { SocketEvent } from '@/types/api'
import GameWindow from '@/components/GameWindow'
import './styles.scss'

export default function App () {
  // == Props ================================

  // == States ===============================
  const [socket, setSocket] = useState<any>()
  const [isConnected, setConnected] = useState(false)

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

    socket.on(SocketEvent.DISCONNECT, () => {
      setConnected(false)
    })
  }

  // == Actions ==============================

  // == Template =============================
  return (
    <div className='app'>
      <AppBar position='static' sx={{ padding: '0.5rem' }}>
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
              Crazy 8s
          </Typography>
        </Toolbar>
      </AppBar>
      <div className='outlet'>
        <Grid container justifyContent='center' alignItems='center' height='100%' width='100%' padding={5}>
          {!isConnected && (
            <>
              <Grid item xs={10} textAlign='center'>
                <Typography variant='h3'>Error: unable to connect to server</Typography>
              </Grid>
            </>
          )}
          {isConnected && <GameWindow socket={socket}/>}
        </Grid>
      </div>
    </div>
  )
}
