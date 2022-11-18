package game;

import java.util.UUID;
import lombok.Data;

@Data
public class Player implements Comparable<Player> {
  // == Props ================================
  UUID id;
  String name;
  int score;

  // == Constructor ==========================
  public Player (UUID id, String name) {
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
