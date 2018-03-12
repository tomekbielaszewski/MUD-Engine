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
    And current location had "Wór z towarem" before last command
    And current location has 0 items
    And he had empty backpack before last command
    And he has 1 items in his backpack
    And he has parameter "quest:tutorial-ship-package-picked-on-locations-2"
    And he had no parameter "quest:tutorial-ship-package-picked-on-locations-2" before command

  # Quest hint is no longer visible on first location
    Given as player with name "player1"
    When he executed following commands
      | "east" |
      | "west" |
    Then current location id is "2"
    And game responded with no events

  # Quest hint is still visible on second location
    Given as player with name "player1"
    And current location id is "2"
    When he executed following commands
      | "east" |
      | "east" |
      | "east" |
    Then current location id is "4"
    And game responded with following event "Zanieś ten towar na pokład!"

  # Player1 can't pick up package on second location when holding package from first location
    Given as player with name "player1"
    When he executed following command "Weź wór z towarem"
    Then he had 1 "Wór z towarem" in his backpack before last command
    And he has 1 "Wór z towarem" in his backpack
    And game responded with following event "Masz już jeden pakunek w rękach!"
    And current location id is "4"

  # Player1 can't drop package on package-respawn location as it is not a package collecting point
    Given as player with name "player1"
    And current location id is "4"
    When he executed following commands
      | "west"                 |
      | "west"                 |
      | "wyrzuc wor z towarem" |
    Then he had 1 "Wór z towarem" in his backpack before last command
    And he has 1 "Wór z towarem" in his backpack
    And current location id is "1"
    And game responded with following event "Gdzie to kładziesz?! Punkt rozładunku jest na pokładzie!"

  # Player1 can't drop package on some other location which is not a package collecting point
    Given as player with name "player1"
    And current location id is "1"
    When he executed following commands
      | "east"                 |
      | "wyrzuc wor z towarem" |
    Then he had 1 "Wór z towarem" in his backpack before last command
    And he has 1 "Wór z towarem" in his backpack
    And current location id is "3"
    And game responded with following event "Gdzie to kładziesz?! Punkt rozładunku jest na pokładzie!"

  # Player1 can drop package on the package collecting point
    Given as player with name "player1"
    And current location id is "3"
    When he executed following commands
      | "west"  |
      | "up"    |
      | "south" |
    Then game responded with following event "Dawaj to tu! **Połóż wór z towarem** tutaj!"
    And game did not respond with following event "Dobry z ciebie majtek! Poloz to i robota skończona!"
    And game did not respond with following event "Zanieś wreszcie te dokumenty sternikowi! Zaraz odpływamy!"
    And current location id is "6"
    When he executed following command "wyrzuc wor z towarem"
    Then he had "wór z towarem" in his backpack before last command
    And he has empty backpack
    And game responded with following events:
      | "Dobry majtek! Jeszcze jeden taki wór i wystarczy." |
      | "Straciles 1 wor z towarem"                         |

  # Package does not respawn when Player1 has already took it from here
    Given as player with name "player1"
    And current location id is "6"
    When he executed following commands
      | "north" |
      | "down"  |
      | "west"  |
    Then current location id is "2"
    And current location has no items

  # Player1 can't pick up package on first location for the second time
    Given as player with name "GameMaster"
    And current location id is "1"
    When he executed following commands
      | "west" |
      | "east" |
    Given as player with name "player1"
    And current location id is "2"
    And current location has "Wór z towarem"
    And he executed following command "Wez wór z towarem"
    Then current location has "Wór z towarem"
    And he had empty backpack before last command
    And he has empty backpack
    And game responded with following event "Już zaniosłeś stąd pakunek. Zostaw trochę roboty dla innych!"

  # Player1 can pick up package on second location
    Given as player with name "player1"
    And current location id is "2"
    When he executed following commands
      | "east" |
      | "east" |
      | "east" |
    Then current location id is "4"
    And game responded with following event "Zanieś ten towar na pokład!"
    When he executed following command "wez wor z towarem"
    Then current location had "Wór z towarem" before last command
    And current location has no items
    And he had empty backpack before last command
    And he has "Wór z towarem" in his backpack

  # Player1 can drop second package on the package collecting point and game shows message about next guest
    Given as player with name "player1"
    And current location id is "4"
    When he executed following commands
      | "west"  |
      | "west"  |
      | "up"    |
      | "south" |
    Then game responded with following event "Dobry z ciebie majtek! Poloz to i robota skończona!"
    And game did not respond with following event "Dawaj to tu! **Połóż wór z towarem** tutaj!"
    And game did not respond with following event "Zanieś wreszcie te dokumenty sternikowi! Zaraz odpływamy!"
    And current location id is "6"
    When he executed following command "wyrzuc wor z towarem"
    Then he had "Wór z towarem" in his backpack before last command
    And he has 4 "Brazowa moneta" in his backpack
    And current location has 2 "wor z towarem"
    And game responded with events like:
      | "Masz tu kilka miedziaków za uczciwą pracę!" |
      | "Otrzymane przedmioty:"                      |
      | "4x Brązowa moneta"                          |
      | "Weź te papiery i zanieś je sternikowi!"     |
    And game did not respond with following event "Dobry majtek! Jeszcze jeden taki wór i wystarczy."

  # Quest hint is not visible on second location anymore and package is not respawned
    Given as player with name "player1"
    And current location id is "6"
    When he executed following commands
      | "north" |
      | "down"  |
      | "east"  |
      | "east"  |
    Then current location id is "4"
    And current location has no items
    And game responded with no events

  # Player1 can't pick up package on second location anymore