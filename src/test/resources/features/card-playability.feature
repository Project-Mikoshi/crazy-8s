Feature: card-playability

  Background:
    Given there are 4 players
    And all player joined the game room and game started

  Scenario: 51
    Given top card is K-C
    When player 1 try to play K-H with success
    Then top of discard pile is now K-H

  Scenario: 52
    Given top card is K-C
    When player 1 try to play 7-C with success
    Then top of discard pile is now 7-C

  Scenario: 53
    Given top card is K-C
    When player 1 try to play 8-H with success
    Then player 1 is prompted to chose a suit

  Scenario: 54
    Given top card is K-C
    When player 1 try to play 5-S with failure
    Then top of discard pile is now K-C
