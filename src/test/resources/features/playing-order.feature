Feature: playing-order

  Background:
    Given there are 4 players
    And all player joined the game room and game started

  Scenario: 41
    When player 1 plays 3-C
    Then next player is player 2
    Then test finished
