package org.grizz.game.command.executors.admin;

import org.grizz.game.command.executors.system.LookAroundCommandExecutor;
import org.grizz.game.exception.PlayerDoesNotExistException;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.AdminRightsService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.notifier.MultiplayerNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminTeleportCommandExecutor {
    @Autowired
    private AdminRightsService adminRightsService;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private EventService eventService;
    @Autowired
    private MultiplayerNotificationService notificationService;
    @Autowired
    private LookAroundCommandExecutor lookAroundCommandExecutor;

    public void teleport(String playerName, String locationId, Player admin, PlayerResponse response) {
        adminRightsService.checkAdminRights(admin);
        Location targetLocation = locationRepo.get(locationId);

        if (isSelfTeleporting(playerName, admin)) {
            selfTeleport(targetLocation, admin, response);
        } else {
            playerTeleport(playerName, targetLocation, admin, response);
        }
    }

    private boolean isSelfTeleporting(String playerName, Player admin) {
        return playerName.equalsIgnoreCase(admin.getName());
    }

    private void selfTeleport(Location targetLocation, Player admin, PlayerResponse response) {
        changePlayerLocation(targetLocation, admin);

        notifySourceLocationPlayers(admin);
        notifyTargetLocationPlayers(targetLocation, admin);

        showLocation(admin, response);
    }

    private void playerTeleport(String playerName, Location targetLocation, Player admin, PlayerResponse adminResponse) {
        Player player = getPlayer(playerName);
        PlayerResponse playerResponse = new PlayerResponse();

        changePlayerLocation(targetLocation, player);

        notifySourceLocationPlayers(player);
        notifyTeleportedPlayer(admin.getName(), targetLocation, playerResponse);
        notifyTargetLocationPlayers(targetLocation, player);
        notifyAdmin(playerName, targetLocation, admin, adminResponse);

        showLocation(player, playerResponse);

        sendPlayerNotifications(player, playerResponse);
        savePlayer(player);
    }

    private Player getPlayer(String playerName) {
        Player player = playerRepository.findByNameIgnoreCase(playerName);
        if (player == null) {
            throw new PlayerDoesNotExistException("admin.command.player.not.found", playerName);
        }
        return player;
    }

    private void changePlayerLocation(Location targetLocation, Player player) {
        String sourceLocation = player.getCurrentLocation();
        player.setPastLocation(sourceLocation);
        player.setCurrentLocation(targetLocation.getId());
    }

    private void notifyAdmin(String playerName, Location targetLocation, Player admin, PlayerResponse adminResponse) {
        String event = eventService.getEvent("admin.command.success.teleportation.notification",
                playerName, targetLocation.getName(), targetLocation.getId());
        adminResponse.getPlayerEvents().add(event);
    }

    private void notifySourceLocationPlayers(Player teleportedPlayer) {
        Location sourceLocation = locationRepo.get(teleportedPlayer.getPastLocation());
        String event = eventService.getEvent("admin.command.player.teleportation.notification.broadcast.out", teleportedPlayer.getName());
        notificationService.broadcast(sourceLocation, event, teleportedPlayer);
    }

    private void notifyTeleportedPlayer(String adminName, Location targetLocation, PlayerResponse playerResponse) {
        String event = eventService.getEvent("admin.command.player.teleportation.notification",
                adminName, targetLocation.getName());
        playerResponse.getPlayerEvents().add(event);
    }

    private void notifyTargetLocationPlayers(Location targetLocation, Player teleportedPlayer) {
        String event = eventService.getEvent("admin.command.player.teleportation.notification.broadcast.in", teleportedPlayer.getName());
        notificationService.broadcast(targetLocation, event, teleportedPlayer);
    }

    private void showLocation(Player player, PlayerResponse response) {
        lookAroundCommandExecutor.lookAround(player, response);
    }

    private void sendPlayerNotifications(Player player, PlayerResponse playerResponse) {
        notificationService.send(player, playerResponse);
    }

    private void savePlayer(Player player) {
        playerRepository.save(player);
    }
}
