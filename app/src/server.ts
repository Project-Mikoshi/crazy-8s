import next from 'next'
import express from 'express'
import { Server, Socket } from 'socket.io'
import { GameEvent } from './types/api'
import { createServer } from 'http'

// == Configs ==============================
const PORT = (process.env.PORT || 8080) as number

const server = next({
  dev: process.env.NODE_ENV !== 'production'
})

// == Services =============================
const app = express()
const httpServer = createServer(app)
const io = new Server(httpServer)

// == Routes ===============================
async function handleRoutes () {

  // == websocket ==========================
  io.on(GameEvent.CONNECTION, (socket: Socket) => {
    console.log('socket is connected', socket.id)

    socket.on(GameEvent.MESSAGE, (message: string) => {
      console.log('message received', message)
    })

    socket.on(GameEvent.DISCONNECT, (reason: string) => {
      console.log(`socket ${socket.id} disconnected due to ${reason}`);
    })
  })

  // == ui routes ==========================
  app.get('*', (req, res) => {
    return server.getRequestHandler()(req, res)
  })
}

// == Server run ===========================
server
  .prepare()
  .then(() => {
    handleRoutes()

    httpServer.listen(PORT, () => console.log(`> Ready on http://localhost:${PORT}`))
  })
  .catch((ex) => {
    console.error(ex.stack)
    process.exit(1)
  })
