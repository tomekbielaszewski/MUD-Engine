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
  # Player1 cannot exit ships lower and message appears when he tries
  # Package appears/respawns on first collecting point and quest hint is shown
  # Package appears/respawns on second collecting point and quest hint is shown
  # Player1 can pick up package on first location
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