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

  Scenario: 69
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
    And player 2 draw and get 7-S
    And player 2 draw and get 5-H
    Then player 2 turn ended

  Scenario: 70-71
    Given top card is 7-C
    When player 1 try to play 2-C with success
    Then top of discard pile is now 2-C
    And player 2 has following cards:
      |4-H|
    Then player 2 must draw
    And player 2 draw and get 2-H
    And player 2 draw and get 9-D
    And from existing cards, player 2 plays 2-H
    Then player 2 turn ended
    Then top of discard pile is now 2-H
    And player 3 has following cards:
      |7-D|
    Then player 3 must draw
    And player 3 draw and get 5-S
    And player 3 draw and get 6-D
    And player 3 draw and get 6-H
    And player 3 draw and get 7-C
    And from existing cards, player 3 plays 6-H
    Then player 3 turn ended
    Then top of discard pile is now 6-H

  Scenario: 72
    Given top card is 7-C
    When player 1 try to play 2-C with success
    Then top of discard pile is now 2-C
    And player 2 has following cards:
      |4-C|
      |6-C|
      |9-D|
    And from existing cards, player 2 plays 4-C
    And from existing cards, player 2 plays 6-C
    Then player 2 turn ended
