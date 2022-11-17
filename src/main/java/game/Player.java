package game;

import lombok.Data;

@Data
public class Player implements Comparable<Player> {
  // == Props ================================
  String id;
  String name;
  int score;

  // == Constructor ==========================
  public Player (String id, String name) {
    this.id = id;
    this.name = name;
    this.score = 0;
  }

  // == Comparator ===========================
  public int compareTo(Player b) {
    return b.getScore() - score;
  }

  // == Public Method ========================

  // == Private Method =======================
}
