package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.corundumstudio.socketio.SocketIOServer;
import config.GameConfig;
import constant.CardValue;
import constant.SocketEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {
  private static final Logger log = LoggerFactory.getLogger(Game.class);

  // == Props ================================
  SocketIOServer server;
  Player winner;
  HashMap<UUID, Player> players;
  Stack<Card> deck;

  // == Constructor ==========================
  public Game (SocketIOServer server) {
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

  public void dealCardsToPlayer () {
    players.forEach((id, player) -> {
      ArrayList<Card> cards = new ArrayList<>(){{
        for (int i = 0; i < GameConfig.NUM_OF_INITIAL_CARDS; i ++) {
          add(deck.pop());
        }
      }};

      server.getClient(id).sendEvent(SocketEvent.MESSAGE, "Your initial cards have been dealt");
      server.getClient(id).sendEvent(SocketEvent.GAME_DEAL_CARDS, cards);
    });
  }

  public void displayCardDeck () {
    players.forEach((id, player) -> {
      ArrayList<Card> cards = new ArrayList<>(){{
        for (int i = 0; i < GameConfig.NUM_OF_INITIAL_CARDS; i ++) {
          add(deck.pop());
        }
      }};

      player.setCardsHeld(cards);
      server.getClient(id).sendEvent(SocketEvent.MESSAGE, "Your initial cards have been dealt");
      server.getClient(id).sendEvent(SocketEvent.GAME_DEAL_CARDS, cards);
    });
  }

  // == Private Method =======================
  private Card drawFirstCardToStartGame () {
    Card card = deck.pop();

    if (card.value.equals(CardValue.EIGHT)) {
      int insertIndex = (int) Math.floor(Math.random() * deck.size());
      deck.add(insertIndex, card);
    }

    return card;
  }

}
