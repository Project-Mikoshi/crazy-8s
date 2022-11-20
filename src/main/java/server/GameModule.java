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
import config.GameConfig;
import constant.SocketEvent;
import lombok.Getter;
import lombok.Setter;
import model.*;
import util.Util;

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

  // == Constructor ==========================
  @Autowired
  public GameModule (SocketIOServer server) {
    this.server = server;
    players = new HashMap<>();
    winner = null;
    deck = Util.shuffleAndBuildCardsStack();
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

  private Card drawCardFromDeck () {
    if (deck.isEmpty()) {
      log.info("No card left in the deck, round will end");

      return null;
    }

    return deck.pop();
  }

  // == Event Handler ========================
}
