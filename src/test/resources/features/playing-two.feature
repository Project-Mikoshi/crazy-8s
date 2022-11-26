Feature: playing-two

  Background:
    Given there are 4 players
    And all player joined the game room and game started

  Scenario: 67
    Given top card is 7-C
    When player 1 try to play 2-C with success
    Then top of discard pile is now 2-C
    And player 2 has following cards:
      |4-H|
    Then player 2 must draw
    And player 2 draw and get 6-C
    And player 2 draw and get 9-D
    And from existing cards, player 2 plays 6-C
    Then top of discard pile is now 6-C

  Scenario: 68
    Given top card is 7-C
    When player 1 try to play 2-C with success
    Then top of discard pile is now 2-C
    And player 2 has following cards:
      |4-H|
    Then player 2 must draw
    And player 2 draw and get 6-S
    And player 2 draw and get 9-D
    Then player 2 must draw
    And player 2 draw and get 9-H
    And player 2 draw and get 6-C
    Then player 2 must play a card
    And from existing cards, player 2 plays 6-C
    Then top of discard pile is now 6-C
