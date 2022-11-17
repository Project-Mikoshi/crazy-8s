package game;

import lombok.Data;

@Data
public class Card {
  // == Props ================================
  String suit;
  String value;
  String color;

  // == Constructor ==========================
  public Card (String suit, String value, String color) {
    this.suit = suit;
    this.value = value;
    this.color = color;
  }

  // == Public Method ========================

  // == Private Method =======================
}
