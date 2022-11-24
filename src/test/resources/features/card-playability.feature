Feature: card-playability

  Background:
    Given there are 4 players
    And all player joined the game room and game started

  Scenario: 51
    Given top card is K-C
    When player 1 try to play 3-C with success
    Then top of discard pile is now 3-C

  Scenario: 52
    Given top card is K-C
    When player 1 try to play 7-C with success
    Then top of discard pile is now 7-C
