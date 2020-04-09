Feature: Administrative command for giving items to players

  Background:
    Given I have standard set of players on starting location

  Scenario: Admin player can give normal items to players
    Given player with name "GameMaster"
    And he has empty backpack
    When he executed following command "admin daj zlota moneta graczowi player1"
    Then he has empty backpack
    And as player with name "player1"
    And he has "Złota moneta" in his backpack
    And game responded with following event "Przekazales 1szt. zlota moneta graczowi player1"
    And game has notified "player1" with events:
      | "Otrzymane przedmioty:"                              |
      | "   1x Złota moneta"                                 |
      | "Przedmioty otrzymales od administratora GameMaster" |

  Scenario: Admin player can give normal items to himself
    Given player with name "GameMaster"
    And he has empty backpack
    When he executed following command "admin daj zlota moneta"
    Then he has 1 items in his backpack
    And he has "Złota moneta" in his backpack
    And game responded with following events:
      | "Otrzymane przedmioty:" |
      | "   1x Złota moneta"    |

  Scenario: Non admin player should not be allowed to run administrative command
    Given player with name "player1"
    And he has empty backpack
    When he executed following command "admin daj zlota moneta"
    Then he has empty backpack
    And game responded with following event "Nie mozesz tego zrobic. Nie jestes administratorem"
    And game has not notified anyone else

  Scenario: Admin player should not be allowed to give static items to players
    Given player with name "GameMaster"
    And he has empty backpack
    When he executed following command "admin daj kowadlo graczowi player1"
    Then he has empty backpack
    And as player with name "player1"
    And he has empty backpack
    And game responded with following event "Nie mozesz dac statycznego przedmiotu!"
    And game has not notified anyone else

  Scenario: Admin player should not be allowed to give static items to himself
    Given player with name "GameMaster"
    And he has empty backpack
    When he executed following command "admin daj kowadlo"
    Then he has empty backpack
    And game responded with following event "Nie mozesz dac statycznego przedmiotu!"
    And game has not notified anyone else