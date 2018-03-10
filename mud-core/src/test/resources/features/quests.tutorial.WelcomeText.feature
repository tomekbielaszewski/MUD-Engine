Feature: Starting location, tutorial quests

  Background:
    Given I have following players:
      | name    | currentLocation |
      | player1 | 1               |

  Scenario: There is welcome text which changes over time

  # First look around command displays longer welcome text
    Given as player with name "player1"
    And he has no parameter "quest:tutorial-text-already-displayed"
    When he executed following command "spojrz"
    Then he has parameter "quest:tutorial-text-already-displayed"
    And game responded with event like "Pobudka zaspany szczurze lądowy!"

  # Second look around command displays shorter welcome text
    Given as player with name "player1"
    When he executed following command "spojrz"
    And game responded with following event "Nie słyszałeś!? Dupa w troki i won **na górę**!"

  # When moved out of the starting area and went back the welcome text is gone
    Given as player with name "player1"
    And he has no parameter "quest:tutorial-welcome-text-deactivated"
    When he executed following command "west"
    And he executed following command "east"
    And he has parameter "quest:tutorial-welcome-text-deactivated"
    And game responded with no events
