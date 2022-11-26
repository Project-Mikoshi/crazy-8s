package model;

import java.util.ArrayList;
import java.util.UUID;
import lombok.Data;

@Data
public class Player implements Comparable<Player> {
  // == Props ================================
  UUID id;
  String name;
  ArrayList<Card> cardsHeld;
  int score;
  int drawnCardCount;
  int discardCardCount;
  int numberOfCardsRequiredToPlayOrDraw;

  // == Constructor ==========================
  public Player (UUID id, String name) {
    this.id = id;
    this.name = name;
    this.score = 0;
    this.drawnCardCount = 0;
    this.discardCardCount = 0;
    this.numberOfCardsRequiredToPlayOrDraw = 0;
    cardsHeld = new ArrayList<>();
  }

  // == Comparator ===========================
  public int compareTo(Player b) {
    return b.getScore() - score;
  }

  // == Public Method ========================

  // == Private Method =======================
}
