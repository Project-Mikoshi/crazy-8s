Feature: playing-order

  Background:
    Given there are 4 players
    And all player joined the game room and game started

  Scenario: 41
    When player 1 plays 3-C
    Then next player is player 2

  Scenario: 42-43
    When player 1 plays A-H
    Then next player is player 4
    Then game is playing in opposite direction
    When player 4 plays 7-H
    Then next player is player 3

  Scenario: 44
    When player 1 plays Q-C
    Then next player is player 3

  Scenario: 45
    When player 1 plays 3-C
    Then next player is player 2
    When player 2 plays 4-C
    Then next player is player 3
    When player 3 plays 5-C
    Then next player is player 4
    When player 4 plays 3-C
    Then next player is player 1

  Scenario: 46-47
    When player 1 plays 3-C
    Then next player is player 2
    When player 2 plays 4-C
    Then next player is player 3
    When player 3 plays 5-C
    Then next player is player 4
    When player 4 plays A-H
    Then game is playing in opposite direction
    Then next player is player 3
    When player 3 plays 7-H
    Then next player is player 2

  Scenario: 48
    When player 1 plays 3-C
    Then next player is player 2
    When player 2 plays 4-C
    Then next player is player 3
    When player 3 plays 5-C
    Then next player is player 4
    When player 4 plays Q-C
    Then next player is player 2
