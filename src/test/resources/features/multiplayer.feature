Feature: multiplayer
  Background:
    Given game started

  Scenario: 1
    When play joins the game
    Then game ended

