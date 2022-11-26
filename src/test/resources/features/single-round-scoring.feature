Feature: single-round-scoring

  Background:
    Given there are 4 players
    And all player joined the game room and game started

  Scenario: 77-78
    Given top card is 7-C
    And player 1 has following cards:
      |7-C|
      |A-S|
    And player 2 has following cards:
      |7-C|
    And player 3 has following cards:
      |8-H|
      |J-H|
      |6-H|
      |K-H|
      |K-S|
    And player 4 has following cards:
      |8-C|
      |8-D|
      |2-D|
    And from existing cards, player 1 plays 7-C
    Then player 1 turn ended
    And from existing cards, player 2 plays 7-C
    Then player 2 turn ended
    Then player 1 scores is 1
    Then player 2 scores is 0
    Then player 3 scores is 86
    Then player 4 scores is 102
    Then game is over with player 2 being the winner
