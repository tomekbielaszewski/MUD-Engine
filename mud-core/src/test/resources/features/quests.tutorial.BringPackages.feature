Feature: Starting location, tutorial quests

  Background:
    Given I have following players:
      | name       | currentLocation |
      | player1    | 1               |
      | GameMaster | 1               |

  Scenario: Player has to find and bring two packages to collecting point

  # First location - the location with package respawn located on the west
  # Second location - the location with package respawn located on the east

  # Player1 cannot pickup package created by admin on starting location
    Given as player with name "GameMaster"
    When he executed following command "admin poloz wor z towarem"
    Then game responded with following event "Dodales 1 Wór z towarem na tej lokacji"
    And game has notified "player1" with event "Administrator GameMaster polozyl 1szt. Wór z towarem na tej lokacji"

    Given as player with name "player1"
    And he has empty backpack
    And current location has "Wór z towarem"
    When he executed following command "wez wor z towarem"
    Then he has empty backpack
    And current location has "Wór z towarem"
    And game responded with following event "Tej roboty nie masz w umowie. Zostaw to!"

  # Player1 cannot exit ships lower and message appears when he tries
    Given as player with name "player1"
    When he executed following command "idź na górę"
    Then current locationId has not changed
    And game responded with event like "Gdzie leziesz z pustymi łapami?!"
    And game did not respond with following event "Nie słyszałeś!? Dupa w troki i won **na górę**!"

  # Package appears/respawns on first collecting point and quest hint is shown
    Given as player with name "player1"
    And location with id "2" has 0 items
    When he executed following command "idź na zachód"
    Then current location id is "2"
    Then current location has 1 items
    And current location has "Wór z towarem"
    And game responded with following event "Zanieś ten towar na pokład!"

  # Package appears/respawns on second collecting point and quest hint is shown
    Given as player with name "player1"
    And location with id "4" has 0 items
    When he executed following commands
      | "idź na wschód" |
      | "east"          |
      | "wschod"        |
    Then current location id is "4"
    Then current location has 1 items
    And current location has "Wór z towarem"
    And game responded with following event "Zanieś ten towar na pokład!"

  # Player1 can pick up package on first location
    Given as player with name "player1"
    When he executed following commands
      | "west"              |
      | "west"              |
      | "west"              |
      | "wez wor z towarem" |
    Then current location id is "2"
    And current location had "Wór z towarem" before command
    And current location has 0 items
    And his backpack had 0 items before command
    And his backpack has 1 items
    And he has parameter "quest:tutorial-ship-package-picked-on-locations-2"
    And he had no parameter "quest:tutorial-ship-package-picked-on-locations-2" before command

  # Quest hint is no longer visible on first location
  # Quest hint is still visible on second location
  # Player1 can't pick up package on second location when holding package from first location
  # Player1 can't drop package on package-respawn location as it is not a package collecting point
  # Player1 can't drop package on some other location which is not a package collecting point
  # Player1 can drop package on the package collecting point
  # Player1 can't pick up package on first location for the second time
  # Player1 can pick up package on second location
  # Player1 can drop second package on the package collecting point
  # Game shows message about next guest
  # Quest hint is not visible on second location anymore but package is respawned
  # Player1 can't pick up package on second location anymore