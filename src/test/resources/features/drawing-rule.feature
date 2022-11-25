Feature: card-playability

  Background:
    Given there are 4 players
    And all player joined the game room and game started

  Scenario: 58
    Given top card is 7-C
    When player 1 has following cards:
      |3-H|
    Then player 1 must draw
    When player 1 draw and get 6-C
    Then player 1 must play a card

  Scenario: 59
    Given top card is 7-C
    When player 1 has following cards:
      |3-H|
    Then player 1 must draw
    When player 1 draw and get 6-D
    Then player 1 must draw
    When player 1 draw and get 5-C
    Then player 1 must play a card
