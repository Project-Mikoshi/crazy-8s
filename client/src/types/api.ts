/* eslint-disable no-unused-vars */
export enum SocketEvent {
  CONNECTION = 'connection',
  MESSAGE = 'message',
  DISCONNECT = 'disconnect',
  GAME_JOIN = 'game-join',
  GAME_START = 'game-start',
  GAME_DEAL_CARDS = 'game-deal-cards',
  GAME_DISCARD_CARD = 'game-discard-card',
  GAME_UPDATE_CARDS = 'game-update-cards',
  GAME_UPDATE_DISCARD_PILE = 'game-update-discard-pile',
  GAME_UPDATE_REMAINING_DECK = 'game-update-remaining-deck',
  GAME_START_PLAYER_TURN = 'game-start-player-turn',
  GAME_END_PLAYER_TURN = 'game-end-player-turn',
  GAME_DRAW_CARD = 'game-draw-card',
  GAME_UPDATE_PLAYERS_INFO = 'game-update-player-info',
  GAME_CHOOSE_SUIT = 'game-choose-suit',
  GAME_CHANGE_SUIT = 'game-change-suit',
  GAME_CHANGE_DIRECTION_OF_PLAY = 'game-change-direction-of-play'
}
