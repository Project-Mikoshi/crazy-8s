package game;

import java.util.HashMap;
import java.util.Stack;

import config.GameConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {
  // == Props ================================
  Player winner;
  HashMap<String, Player> players;
  Stack<Card> deck;

  // == Constructor ==========================
  public Game () {
    players = new HashMap<>();
    winner = null;
    deck = Util.shuffleAndBuildCardsStack();
  }

  // == Public Method ========================
  public void addPlayer (String id, String name) {
    players.putIfAbsent(id, new Player(id, name));
  }

  public boolean isReadyToStart () {
    return players.size() == GameConfig.NUM_OF_PLAYERS;
  }

  // == Private Method =======================

}
