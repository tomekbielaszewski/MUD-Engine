Feature: Administrative command for giving items to players

  Scenario: Non admin player should not be allowed to run administrative command
    Given player with name "player1"
    And he has empty backpack
    When he executed following command "admin daj zlota moneta"
    Then his backpack has 0 items
    And game responded with following event "Nie mozesz tego zrobic. Nie jestes administratorem"
    And game has not notified anyone else

  Scenario: Admin player should not be allowed to give static items to players
    Given player with name "GameMaster"
    And he has empty backpack
    When he executed following command "admin daj kowadlo graczowi player1"
    Then his backpack has 0 items
    And as player with name "player1"
    And his backpack has 0 items
    And game responded with following event "Nie mozesz dac statycznego przedmiotu!"
    And game has not notified anyone else

  Scenario: Admin player should not be allowed to give static items to himself
    Given player with name "GameMaster"
    And he has empty backpack
    When he executed following command "admin daj kowadlo"
    Then his backpack has 0 items
    And game responded with following event "Nie mozesz dac statycznego przedmiotu!"
    And game has not notified anyone else