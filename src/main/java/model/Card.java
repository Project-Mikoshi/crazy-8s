package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
  // == Props ================================
  String suit;
  String value;
  String color;
  Boolean isPlayable;

  // == Constructor ==========================

  // == Public Method ========================
  @Override
  public String toString () {
    return "%s %s %s".formatted(color, suit, value);
  }

  // == Private Method =======================
}
