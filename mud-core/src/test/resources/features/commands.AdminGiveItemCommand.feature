Feature: Administrative command for giving items to players

  Scenario: Non admin player should not be allowed to run administrative command
    Given player with empty backpack
    When he executed following command "admin daj zlota moneta"
    Then his backpack has 0 items
    And game responded with following event "Nie mozesz tego zrobic. Nie jestes administratorem"
    And game has not notified anyone else