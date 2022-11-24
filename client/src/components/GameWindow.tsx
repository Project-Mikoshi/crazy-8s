import React, { useEffect, useState } from 'react'
import { SimpleDialog } from '@mikoshi/application-components'
import { Grid, LinearProgress, Typography, Button, TextField, Stack } from '@mui/material'
import ServerMessageCard from '@/components/ServerMessageCard'
import PlayerActionDisplay from '@/components/PlayerActionDisplay'
import GameInfoDisplay from '@/components/GameInfoDisplay'
import CardDisplay from '@/components/CardDisplay'
import { Card } from '@/types/card'
import { GameState } from '@/types/game'
import { SocketEvent } from '@/types/api'
import { Player } from '@/types/player'

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
  const [isPlaying, setIsPlaying] = useState(false)
  const [playerName, setPlayerName] = useState('')
  const [players, setPlayers] = useState<Array<Player>>([])
  const [playerCards, setPlayerCards] = useState<Array<Card>>([])
  const [serverMessages, setServerMessages] = useState<Array<string>>([])
  const [isModalOpen, setISModelOpen] = useState(false)
  const [cardChoices, setCardChoices] = useState<Array<Card>>([])

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

    socket.on(SocketEvent.GAME_START_PLAYER_TURN, () => {
      setIsPlaying(true)
    })

    socket.on(SocketEvent.GAME_END_PLAYER_TURN, () => {
      setIsPlaying(false)
    })

    socket.on(SocketEvent.GAME_UPDATE_REMAINING_DECK, (count: number) => {
      setRemainingDeckCount(count)
    })

    socket.on(SocketEvent.GAME_UPDATE_PLAYERS_INFO, (players: Array<Player>) => {
      setPlayers(players)
    })

    socket.on(SocketEvent.GAME_CHOOSE_SUIT, (cards: Array<Card>) => {
      setCardChoices(cards)
      setISModelOpen(true)
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

  function drawCard () {
    socket.emit(SocketEvent.GAME_DRAW_CARD)
  }

  function onChangeSuit (index: number) {
    setISModelOpen(false)
    socket.emit(SocketEvent.GAME_CHANGE_SUIT, cardChoices[index])
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
            <TextField
              id='player-name-input-box'
              fullWidth
              helperText='Enter Player Name'
              value={playerName}
              onChange={updatePlayerName}
            />
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
        </>
      )
    }

    case (GameState.STARTED): {
      return (
        <>
          <SimpleDialog title='You have played a card that allows you to change the suit' isOpen={isModalOpen} onCancel={() => alert('you have to choose a suit')}>
            <Typography variant='h6'>Please Choose a Suit</Typography>
            <Stack sx={{ margin: '1rem', alignItems: 'center', flexDirection: 'column' }} spacing={1}>
              {cardChoices.map((card, index) => (
                <CardDisplay key={index} card={card} id={index} onSelect={onChangeSuit}/>
              ))}
            </Stack>
          </SimpleDialog>
          <Grid item container md={9}>
            <GameInfoDisplay topCardOnDiscardPile={topDiscardedCard} remainingDeckCount={remainingDeckCount} players={players} />
          </Grid>
          <Grid item container md={3}>
            <ServerMessageCard messages={serverMessages} />
          </Grid>
          <Grid item container md={12}>
            <PlayerActionDisplay deckCount={remainingDeckCount} isPlaying={isPlaying} cards={playerCards} playerName={playerName} onDiscardCard={discardCard} onDrawCard={drawCard} />
          </Grid>
        </>
      )
    }
  }
}
