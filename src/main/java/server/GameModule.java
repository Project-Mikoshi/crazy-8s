package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import config.GameConfig;
import constant.CardValue;
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

    if (this.server != null) {
      server.addEventListener(SocketEvent.GAME_DISCARD_CARD, Card.class, playerDiscardCard());
      server.addEventListener(SocketEvent.GAME_DRAW_CARD, String.class, playerDrawCard());
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

  public void beginNewRound () {
    log.info("Game has started");
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "Game started");
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_START);

    deck = GameUtil.shuffleAndBuildCardsStack();

    dealCardsToPlayer();
    drawFirstCardForDiscardPle();

    currentPlayer = playerOrder.get(0);
    orderReversed = false;

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
  }

  // == Private Method =======================
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

    while (card.getValue().equals(CardValue.EIGHT)) {
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

  private void updateCardsOnPlayersHand () {
    players.forEach((id, player) -> {
      player.getCardsHeld().forEach(card -> {
        card.setIsPlayable(GameUtil.doesTwoCardsMatch(card, discardPile.peek()));
      });

      server.getClient(id).sendEvent(SocketEvent.GAME_UPDATE_CARDS, player.getCardsHeld());
    });
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
      server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "%s is playing".formatted(player.getName()));
    }
  }

  private void endPlayerTurn () {
    Player player = players.get(currentPlayer);
    server.getClient(currentPlayer).sendEvent(SocketEvent.GAME_END_PLAYER_TURN);
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "%s has finished playing".formatted(player.getName()));

    if (discardPile.peek().getValue().equals(CardValue.A)) {
      orderReversed = !orderReversed;
      server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "Direction reversed!");
    }

    if (discardPile.peek().getValue().equals(CardValue.QUEEN)) {
      moveToNextPlayer();
      server.getClient(currentPlayer).sendEvent(SocketEvent.MESSAGE, "Your turn has been skipped due to the action of previous player");
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

  private void handlePostPlayerActions () {
    Player player = players.get(currentPlayer);

    if (player.getCardsHeld().isEmpty()) {
      endCurrentRound();
      beginNewRound();
      return;
    }

    if (deck.isEmpty() && !GameUtil.somePlayerHasPlayableCards(players.values())) {
      endCurrentRound();
      beginNewRound();
      return;
    }
    
    if (player.getDrawnCardCount() >= GameConfig.MAX_DRAW_PER_TURN && !GameUtil.playerHasPlayableCards(player)) {
      endPlayerTurn();
      moveToNextPlayer();
      promptPlayerToStart();
      return;
    }

    if (player.getDiscardCardCount() == 1) {
      endPlayerTurn();
      moveToNextPlayer();
      promptPlayerToStart();
      return;
    }
  }

  // == Event Handler ========================
  private DataListener<Card> playerDiscardCard () {
    return (client, cardToDiscard, ackSender) -> {
      UUID playerId = client.getSessionId();
      Player player = players.get(playerId);

      if (player != null) {
        log.info("receive card to discard {}", cardToDiscard);

        player.getCardsHeld().remove(cardToDiscard);
        player.setDiscardCardCount(player.getDiscardCardCount() + 1);
        client.sendEvent(SocketEvent.MESSAGE, "You have discarded a %s".formatted(cardToDiscard));
        client.sendEvent(SocketEvent.GAME_UPDATE_CARDS, player.getCardsHeld());

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
      card.setIsPlayable(GameUtil.doesTwoCardsMatch(card, discardPile.peek()));
      log.info("{} draw {} from card deck", player.getName(), card);

      player.getCardsHeld().add(card);
      player.setDrawnCardCount(player.getDrawnCardCount() + 1);
      client.sendEvent(SocketEvent.GAME_UPDATE_CARDS, player.getCardsHeld());
      client.sendEvent(SocketEvent.MESSAGE, "You have drawn a %s".formatted(card));
      server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_UPDATE_REMAINING_DECK, deck.size());

      handlePostPlayerActions();
    };
  }

}
