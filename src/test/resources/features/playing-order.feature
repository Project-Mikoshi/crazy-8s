Feature: playing-order

  Background:
    Given there are 4 players
    And all player joined the game room and game started

  Scenario: 41
    When player 1 plays 3-C
    Then next player is player 2
    Then test finished

  Scenario: 42-43
    When player 1 plays A-H
    Then next player is player 4
    Then game is playing in opposite direction
    When player 4 plays 7-H
    Then next player is player 3
    Then test finished
