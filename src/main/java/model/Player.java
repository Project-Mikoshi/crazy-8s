package model;

import java.util.ArrayList;
import java.util.UUID;
import lombok.Data;

@Data
public class Player implements Comparable<Player> {
  // == Props ================================
  UUID id;
  String name;
  int score;
  ArrayList<Card> cardsHeld;
  int drawnCardCount;
  int discardCardCount;

  // == Constructor ==========================
  public Player (UUID id, String name) {
    this.id = id;
    this.name = name;
    this.score = 0;
    this.drawnCardCount = 0;
    this.discardCardCount = 0;
    cardsHeld = new ArrayList<>();
  }

  // == Comparator ===========================
  public int compareTo(Player b) {
    return b.getScore() - score;
  }

  // == Public Method ========================

  // == Private Method =======================
}
