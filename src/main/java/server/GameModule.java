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
  Stack<Card> deck;
  Stack<Card> discardPile;

  // == Constructor ==========================
  @Autowired
  public GameModule (SocketIOServer server) {
    this.server = server;
    players = new HashMap<>();
    winner = null;
    deck = GameUtil.shuffleAndBuildCardsStack();
    discardPile = new Stack<>();

    if (this.server != null) {
      server.addEventListener(SocketEvent.GAME_DISCARD_CARD, Card.class, playerDiscardCard());
    }
  }

  // == Public Method ========================
  public void addPlayer (UUID id, String name) {
    players.putIfAbsent(id, new Player(id, name));
  }

  public void removePlayer (UUID id) {
    players.remove(id);
  }

  public boolean isReadyToStart () {
    return players.size() == GameConfig.NUM_OF_PLAYERS;
  }

  public void start () {
    log.info("Game has started");
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "Game started");
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_START);

    dealCardsToPlayer();
    drawFirstCardForDiscardPle();
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
      log.info("No card left in the deck, round will end");

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

  // == Event Handler ========================
  private DataListener<Card> playerDiscardCard () {
    return (client, cardToDiscard, ackSender) -> {
      UUID playerId = client.getSessionId();
      Player player = players.get(playerId);

      if (player != null) {
        log.info("receive card to discard {}", cardToDiscard);

        player.getCardsHeld().remove(cardToDiscard);
        client.sendEvent(SocketEvent.MESSAGE, "You have discarded a %s".formatted(cardToDiscard));
        client.sendEvent(SocketEvent.GAME_UPDATE_CARDS, player.getCardsHeld());

        discardPile.push(cardToDiscard);
        server.getBroadcastOperations().sendEvent(SocketEvent.GAME_UPDATE_DISCARD_PILE, cardToDiscard);
        server.getBroadcastOperations().sendEvent(SocketEvent.GAME_UPDATE_REMAINING_DECK, deck.size());
        updateCardsOnPlayersHand();
      }
    };
  }

}
