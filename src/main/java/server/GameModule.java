package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import config.GameConfig;
import constant.CardColor;
import constant.CardSuit;
import constant.CardValue;
import constant.Direction;
import constant.SocketEvent;
import lombok.Getter;
import lombok.Setter;
import model.*;
import util.GameUtil;

@Getter
@Setter
@Component
public class GameModule {
  private static final Logger log = LoggerFactory.getLogger(GameModule.class);

  // == Props ================================
  SocketIOServer server;
  Player winner;
  HashMap<UUID, Player> players;
  ArrayList<UUID> playerOrder;
  UUID currentPlayer;
  boolean orderReversed;
  Stack<Card> deck;
  Stack<Card> discardPile;
  int roundNumber;

  // == Constructor ==========================
  @Autowired
  public GameModule (SocketIOServer server) {
    this.server = server;
    players = new HashMap<>();
    winner = null;
    currentPlayer = null;
    deck = new Stack<>();
    discardPile = new Stack<>();
    playerOrder = new ArrayList<>();
    orderReversed = false;
    roundNumber = 0;

    if (this.server != null) {
      server.addEventListener(SocketEvent.GAME_DISCARD_CARD, Card.class, playerDiscardCard());
      server.addEventListener(SocketEvent.GAME_DRAW_CARD, String.class, playerDrawCard());
      server.addEventListener(SocketEvent.GAME_CHANGE_SUIT, Card.class, playerChangeSuit());
    }
  }

  // == Public Method ========================
  public void addPlayer (UUID id, String name) {
    players.putIfAbsent(id, new Player(id, name));
    playerOrder.add(id);
  }

  public void removePlayer (UUID id) {
    players.remove(id);
    playerOrder.remove(id);
  }

  public boolean haveEnoughPlayers () {
    return players.size() == GameConfig.NUM_OF_PLAYERS;
  }

  public void reset () {
    players.clear();
    playerOrder.clear();
    discardPile.clear();
    deck.clear();
    roundNumber = 0;
    winner = null;
    currentPlayer = null;
    orderReversed = false;
  }

  public void beginNewRound () {
    log.info("Game has started");
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "Game started");
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_START);
    deck = GameUtil.shuffleAndBuildCardsStack();
    roundNumber++;

    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_ROUND, roundNumber);
    
    dealCardsToPlayer();
    drawFirstCardForDiscardPle();

    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_PLAYERS_INFO, players.values());

    determineCurrentPlayerOnNewRound();

    orderReversed = false;
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_CHANGE_DIRECTION_OF_PLAY, Direction.NORMAL);

    promptPlayerToStart();
  }

  public void endCurrentRound () {
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "Round ended, updating score for all");
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_END_PLAYER_TURN);

    players.forEach((id, player) -> {
      int currentScore = player.getScore();
      int delta = GameUtil.calculateScore(player.getCardsHeld());
      int newScore = currentScore + delta;

      player.setScore(newScore);
      server.getClient(id).sendEvent(SocketEvent.MESSAGE, "Your score have been updated by %d".formatted(delta));
    });

    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_PLAYERS_INFO, players.values());

    checkForWinner();
  }

  public void updateCardsOnPlayersHand () {
    players.forEach((id, player) -> {
      long count = player.getCardsHeld().stream().filter(card -> GameUtil.doesTwoCardsMatch(card, discardPile.peek())).count();
  
      if (player.getNumberOfCardsRequiredToPlayOrDraw() != 0 && count < GameConfig.DRAW_OR_PLAY_REQUIRED_FOR_CARD_TWO) {
        player.getCardsHeld().forEach(card -> {
          card.setIsPlayable(false);
        });
      } else {
        player.getCardsHeld().forEach(card -> {
          card.setIsPlayable(GameUtil.doesTwoCardsMatch(card, discardPile.peek()));
        });
      }

      server.getClient(id).sendEvent(SocketEvent.GAME_UPDATE_CARDS, player.getCardsHeld());
    });
  }

  // == Actions ==============================
  private void dealCardsToPlayer () {
    players.forEach((id, player) -> {
      ArrayList<Card> cards = new ArrayList<>(){{
        for (int i = 0; i < GameConfig.NUM_OF_INITIAL_CARDS; i ++) {
          add(drawCardFromDeck());
        }
      }};

      player.setCardsHeld(cards);
      server.getClient(id).sendEvent(SocketEvent.MESSAGE, "Your initial cards have been dealt");
      server.getClient(id).sendEvent(SocketEvent.GAME_DEAL_CARDS, cards);
    });
  }

  private void drawFirstCardForDiscardPle () {
    Card card = drawCardFromDeck();

    while (card.getValue().equals(CardValue.EIGHT)
      || card.getValue().equals(CardValue.TWO)
      || card.getValue().equals(CardValue.QUEEN)
      || card.getValue().equals(CardValue.A)    
    ) {
      int insertIndex = (int) Math.floor(Math.random() * deck.size());
      deck.add(insertIndex, card);

      card = drawCardFromDeck();
    }

    discardPile.push(card);
    log.info("First card to start the game - {}", card);

    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_DISCARD_PILE, discardPile.peek());
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_REMAINING_DECK, deck.size());
    updateCardsOnPlayersHand();
  }

  private Card drawCardFromDeck () {
    if (deck.isEmpty()) {
      log.info("No card left in the deck");

      return null;
    }

    return deck.pop();
  }

  private void promptPlayerToStart () {
    Player player = players.get(currentPlayer);

    if (!GameUtil.playerHasPlayableCards(player) && deck.isEmpty()) {
      server.getClient(currentPlayer).sendEvent(SocketEvent.MESSAGE, "You have no playable card, thus skipping your turn");
      moveToNextPlayer();
      promptPlayerToStart();
    } else {
      player.setDrawnCardCount(0);
      player.setDiscardCardCount(0);
      server.getClient(currentPlayer).sendEvent(SocketEvent.GAME_START_PLAYER_TURN);
      server.getClient(currentPlayer).sendEvent(SocketEvent.GAME_TOGGLE_PLAYER_DRAW_CARD_ABILITY, true);
      server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "%s is playing".formatted(player.getName()));
    }
  }

  private void endPlayerTurn () {
    Player player = players.get(currentPlayer);
    player.setNumberOfCardsRequiredToPlayOrDraw(0);
    server.getClient(currentPlayer).sendEvent(SocketEvent.GAME_END_PLAYER_TURN);
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "%s has finished playing".formatted(player.getName()));

    if (discardPile.peek().getValue().equals(CardValue.A)) {
      handleA();
    }

    if (discardPile.peek().getValue().equals(CardValue.QUEEN)) {
      handleQueen();
    }
  }

  private void moveToNextPlayer () {
    int currentPlayerIndex = playerOrder.indexOf(currentPlayer);

    if (orderReversed) {
      currentPlayer = currentPlayerIndex - 1 >= 0 ? playerOrder.get(currentPlayerIndex - 1) : playerOrder.get(playerOrder.size() - 1);
    } else {
      currentPlayer = currentPlayerIndex + 1 < playerOrder.size() ?  playerOrder.get(currentPlayerIndex + 1) : playerOrder.get(0);
    }
  }

  private void checkForWinner () {
    ArrayList<Player> sortedPlayer = new ArrayList<>(){{
      addAll(players.values());
    }};

    Collections.sort(sortedPlayer);

    Player playerWithHighestScore = sortedPlayer.get(0);
    Player playerWithLowestScore = sortedPlayer.get(players.size() - 1);

    if (playerWithHighestScore.getScore() >= GameConfig.SCORE_THRESHOLD) {
      winner = playerWithLowestScore;
      server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_DECLARE_WINNER, winner);
    }
  }

  private void determineCurrentPlayerOnNewRound () {
    if (roundNumber <= players.size()) {
      currentPlayer = playerOrder.get(roundNumber - 1);
    } else {
      currentPlayer = playerOrder.get(0);
    }
  }

  // == Event Handler ========================
  private DataListener<Card> playerDiscardCard () {
    return (client, cardToDiscard, ackSender) -> {
      UUID playerId = client.getSessionId();
      Player player = players.get(playerId);

      log.info("receive card to discard {}", cardToDiscard);

      player.getCardsHeld().remove(cardToDiscard);
      player.setDiscardCardCount(player.getDiscardCardCount() + 1);
      client.sendEvent(SocketEvent.MESSAGE, "You have discarded a %s".formatted(cardToDiscard));
      client.sendEvent(SocketEvent.GAME_UPDATE_CARDS, player.getCardsHeld());

      if (player.getDiscardCardCount() < player.getNumberOfCardsRequiredToPlayOrDraw()) {
        handleTwoDuringPlayForDiscard(player, client);
      } else if (player.getNumberOfCardsRequiredToPlayOrDraw() == 0 && cardToDiscard.getValue().equals(CardValue.TWO)) {
        handleTwoWhenDiscard(cardToDiscard);
      } else if (cardToDiscard.getValue().equals(CardValue.EIGHT)) {
        handleEight(client);
      } else {
        discardPile.push(cardToDiscard);
        server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_DISCARD_PILE, cardToDiscard);
        server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_REMAINING_DECK, deck.size());
  
        updateCardsOnPlayersHand();
        handlePostPlayerActions();
      }
    };
  }

  private DataListener<String> playerDrawCard () {
    return (client, clientMessage, ackSender) -> {
      UUID playerId = client.getSessionId();
      Player player = players.get(playerId);

      Card card = drawCardFromDeck();

      if (discardPile.peek().getValue().equals(CardValue.TWO) && player.getNumberOfCardsRequiredToPlayOrDraw() != 0) {
        handleTwoDuringPlayForDraw(player, client, card);
      } else {
        card.setIsPlayable(GameUtil.doesTwoCardsMatch(card, discardPile.peek()));
        log.info("{} draw {} from card deck", player.getName(), card);
  
        player.getCardsHeld().add(card);
        player.setDrawnCardCount(player.getDrawnCardCount() + 1);
  
        if (GameUtil.shouldDrawAbilityBeDisabled(player, card, discardPile.peek())) {
          client.sendEvent(SocketEvent.GAME_TOGGLE_PLAYER_DRAW_CARD_ABILITY, false);
        }
  
        client.sendEvent(SocketEvent.GAME_UPDATE_CARDS, player.getCardsHeld());
        client.sendEvent(SocketEvent.MESSAGE, "You have drawn a %s".formatted(card));
        server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_REMAINING_DECK, deck.size());
  
        handlePostPlayerActions();
      }
    };
  }

  private DataListener<Card> playerChangeSuit () {
    return (client, card, ackSender) -> {
      UUID playerId = client.getSessionId();
      Player player = players.get(playerId);

      discardPile.push(card);
      server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "%s has changed the card on the discard pile to %s".formatted(player.getName(), card));
      server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_DISCARD_PILE, card);
      server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_REMAINING_DECK, deck.size());

      updateCardsOnPlayersHand();
      handlePostPlayerActions();
    };
  }

  // == Action Handler =======================
  private void handlePostPlayerActions () {
    Player player = players.get(currentPlayer);

    if (player.getCardsHeld().isEmpty()) {
      endCurrentRound();

      if (winner == null) {
        beginNewRound();
      }

      return;
    }

    if (deck.isEmpty() && !GameUtil.somePlayerHasPlayableCards(players.values())) {
      endCurrentRound();
      beginNewRound();
      return;
    }

    if (deck.isEmpty() && !GameUtil.playerHasPlayableCards(player)) {
      endPlayerTurn();
      moveToNextPlayer();
      promptPlayerToStart();
      return;
    }
    
    if (player.getDrawnCardCount() >= GameConfig.MAX_DRAW_PER_TURN && !GameUtil.playerHasPlayableCards(player)) {
      endPlayerTurn();
      moveToNextPlayer();
      promptPlayerToStart();
      return;
    }

    if (player.getDiscardCardCount() != 0) {
      endPlayerTurn();
      moveToNextPlayer();
      promptPlayerToStart();
      return;
    }
  }

  // == Special Card Handler =================
  private void handleQueen () {
    moveToNextPlayer();
    server.getClient(currentPlayer).sendEvent(SocketEvent.MESSAGE, "Your turn has been skipped due to the action of previous player");
  }

  private void handleA () {
    orderReversed = !orderReversed;
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_CHANGE_DIRECTION_OF_PLAY, orderReversed ? Direction.REVERSE : Direction.NORMAL);
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "Direction reversed!");
  }

  private void handleEight (SocketIOClient client) {
    client.sendEvent(SocketEvent.GAME_CHOOSE_SUIT, new ArrayList<Card>(){{
      add(new Card(CardSuit.CLUBS, CardValue.EIGHT, CardColor.BLACK, true));
      add(new Card(CardSuit.SPADES, CardValue.EIGHT, CardColor.BLACK, true));
      add(new Card(CardSuit.DIAMONDS, CardValue.EIGHT, CardColor.RED, true));
      add(new Card(CardSuit.HEARTS, CardValue.EIGHT, CardColor.RED, true));
    }});

    log.info("request sent to player to choose suit");
  }

  private void handleTwoWhenDiscard (Card cardToDiscard) {
    Card previousTopOfDiscardPile = discardPile.peek();

    discardPile.push(cardToDiscard);
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_DISCARD_PILE, cardToDiscard);
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_REMAINING_DECK, deck.size());

    updateCardsOnPlayersHand();
    endPlayerTurn();
    moveToNextPlayer();

    Player player = players.get(currentPlayer);
    if (previousTopOfDiscardPile.getValue().equals(CardValue.TWO) && cardToDiscard.getValue().equals(CardValue.TWO)) {
      player.setNumberOfCardsRequiredToPlayOrDraw(GameConfig.DRAW_OR_PLAY_REQUIRED_FOR_CARD_TWO * 2);
    } else {
      player.setNumberOfCardsRequiredToPlayOrDraw(GameConfig.DRAW_OR_PLAY_REQUIRED_FOR_CARD_TWO);
    }

    promptPlayerToStart();
  }

  private void handleTwoDuringPlayForDiscard (Player player, SocketIOClient client) {
    int numberOfRemainingCard = player.getNumberOfCardsRequiredToPlayOrDraw() - player.getDiscardCardCount();
    client.sendEvent(SocketEvent.GAME_UPDATE_CARDS, player.getCardsHeld());

    if (numberOfRemainingCard == 0) {
      player.setNumberOfCardsRequiredToPlayOrDraw(0);
      handlePostPlayerActions();
    }

    client.sendEvent(SocketEvent.MESSAGE, "You still need to discard %d card".formatted(numberOfRemainingCard));
  }

  private void handleTwoDuringPlayForDraw (Player player, SocketIOClient client, Card cardDrawn) {
    player.setDrawnCardCount(player.getDrawnCardCount() + 1);

    int numberOfRemainingCard = player.getNumberOfCardsRequiredToPlayOrDraw() - player.getDrawnCardCount();

    if (numberOfRemainingCard > 0) {
      client.sendEvent(SocketEvent.MESSAGE, "You still need to draw %d card".formatted(numberOfRemainingCard));
      cardDrawn.setIsPlayable(false);
    } else {
      if (numberOfRemainingCard == 0) {
        player.setDrawnCardCount(0);
        player.setNumberOfCardsRequiredToPlayOrDraw(0);
        updateCardsOnPlayersHand();
      }

      cardDrawn.setIsPlayable(GameUtil.doesTwoCardsMatch(cardDrawn, discardPile.peek()));
    }


    log.info("{} draw {} from card deck", player.getName(), cardDrawn);
    player.getCardsHeld().add(cardDrawn);
    client.sendEvent(SocketEvent.GAME_UPDATE_CARDS, player.getCardsHeld());
    client.sendEvent(SocketEvent.MESSAGE, "You have drawn a %s".formatted(cardDrawn));
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_REMAINING_DECK, deck.size());

    client.sendEvent(SocketEvent.GAME_UPDATE_CARDS, player.getCardsHeld());
  }
}
