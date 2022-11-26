Feature: complete-game

  Background:
    Given there are 4 players
    And all player joined the game room and game started

  Scenario: 81-147
    Given top card is 4-D

    And player 1 has following cards:
      |4-H|
      |7-S|
      |5-D|
      |6-D|
      |9-D|
    And player 2 has following cards:
      |4-S|
      |6-S|
      |K-C|
      |8-H|
      |10-D|
    And player 3 has following cards:
      |9-S|
      |6-C|
      |9-C|
      |J-D|
      |3-H|
    And player 4 has following cards:
      |7-D|
      |J-H|
      |Q-H|
      |K-H|
      |5-C|

    And from existing cards, player 1 plays 4-H
    And from existing cards, player 2 plays 4-S
    And from existing cards, player 3 plays 9-S
    Then player 4 must draw
    And player 4 draw and get 2-C
    Then player 4 must draw
    And player 4 draw and get 3-C
    Then player 4 must draw
    And player 4 draw and get 4-C
    Then player 4 turn ended

    And from existing cards, player 1 plays 7-S
    And from existing cards, player 2 plays 6-S
    And from existing cards, player 3 plays 6-C
    And from existing cards, player 4 plays 2-C
    Then player 1 must draw
    And player 1 draw and get 10-C
    And player 1 draw and get J-C
    And from existing cards, player 1 plays J-C
    And from existing cards, player 2 plays K-C
    And from existing cards, player 3 plays 9-C
    And from existing cards, player 4 plays 3-C

    And player 1 draw and get 7-C
    And from existing cards, player 1 plays 7-C
    And from existing cards, player 2 plays 8-H
    Then player 2 is prompted to chose a suit
    And player 2 choose 8-D from the prompt
    And from existing cards, player 3 plays J-D
    And from existing cards, player 4 plays 7-D

    And from existing cards, player 1 plays 9-D
    And from existing cards, player 2 plays 10-D
    Then player 1 scores is 21
    Then player 2 scores is 0
    Then player 3 scores is 3
    Then player 4 scores is 39
    Then game round advanced to 2

    Given top card is 10-D

    And player 1 has following cards:
      |7-D|
      |4-S|
      |7-C|
      |4-H|
      |5-D|
    And player 2 has following cards:
      |9-D|
      |3-S|
      |9-C|
      |3-H|
      |J-C|
    And player 3 has following cards:
      |3-D|
      |9-S|
      |3-C|
      |9-H|
      |5-H|
    And player 4 has following cards:
      |4-D|
      |7-S|
      |4-C|
      |5-S|
      |8-D|

    And from existing cards, player 2 plays 9-D
    And from existing cards, player 3 plays 3-D
    And from existing cards, player 4 plays 4-D
    And from existing cards, player 1 plays 4-S

    And from existing cards, player 2 plays 3-S
    And from existing cards, player 3 plays 9-S
    And from existing cards, player 4 plays 7-S
    And from existing cards, player 1 plays 7-C

    And from existing cards, player 2 plays 9-C
    And from existing cards, player 3 plays 3-C
    And from existing cards, player 4 plays 4-C
    And from existing cards, player 1 plays 4-H

    And from existing cards, player 2 plays 3-H
    And from existing cards, player 3 plays 9-H
    And player 4 draw and get K-S
    And player 4 draw and get Q-S
    And player 4 draw and get K-H
    And from existing cards, player 4 plays K-H
    And player 1 draw and get 6-D
    And player 1 draw and get Q-D
    And player 1 draw and get J-D

    And player 2 draw and get 6-S
    And player 2 draw and get J-S
    And player 2 draw and get 10-S
    And from existing cards, player 3 plays 5-H

    Then player 1 scores is 59
    Then player 2 scores is 36
    Then player 3 scores is 3
    Then player 4 scores is 114

    Then game is over with player 3 being the winner
