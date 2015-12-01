package org.grizz.game.service.complex.impl;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.complex.AdministratorService;
import org.grizz.game.service.complex.MovementService;
import org.grizz.game.service.complex.MultiplayerNotificationService;
import org.grizz.game.service.complex.ScriptRunnerService;
import org.grizz.game.service.simple.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-11-30.
 */
@Slf4j
@Service
public class AdministratorServiceImpl implements AdministratorService {
    private static final String AUTHORIZATION_CHECK_SCRIPT_ID = "authorization-check";

    @Autowired
    private ScriptRunnerService scriptRunnerService;

    @Autowired
    private MovementService movementService;

    @Autowired
    private LocationRepo locationRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private EventService eventService;

    @Autowired
    private MultiplayerNotificationService multiplayerNotificationService;

    @Override
    public void teleport(String player, String targetLocationId, PlayerContext admin, PlayerResponse adminResponse) {
        if(isAuthorized(admin, adminResponse)) {
            Location targetLocation = null;

            try {
                targetLocation = locationRepo.get(targetLocationId);
            } catch (IllegalArgumentException e) {
                String event = eventService.getEvent("admin.command.location.not.found", targetLocationId);
                adminResponse.getPlayerEvents().add(event);
                return;
            }
            PlayerContext teleportedPlayer = playerRepo.findByNameIgnoreCase(player); //TODO Does it throw anything when not found?

            if (teleportedPlayer == null) {
                String event = eventService.getEvent("admin.command.player.not.found", player);
                adminResponse.getPlayerEvents().add(event);
            } else {
                log.info("Teleporting [{}] to [{}]", player, targetLocationId);
                teleport((PlayerContextImpl) teleportedPlayer, targetLocation, admin, adminResponse);
            }

        } else {
            log.warn("{} is NOT authorized!", admin.getName());
        }
    }

    private void teleport(PlayerContextImpl teleportedPlayer, Location targetLocation, PlayerContext admin, PlayerResponse adminResponse) {
        String currentLocation = teleportedPlayer.getCurrentLocation();
        teleportedPlayer.setPastLocation(currentLocation);
        teleportedPlayer.setCurrentLocation(targetLocation.getId());

        notifyTeleportedPlayer(teleportedPlayer, targetLocation, admin);
        notifyPlayersOnPastLocation(teleportedPlayer);
        notifyPlayersOnTargetLocation(teleportedPlayer);
        notifyTeleportingAdmin(teleportedPlayer, targetLocation, adminResponse);

        if(teleportedPlayer.getName().equals(admin.getName())) { //self teleport?
            PlayerContextImpl _admin = (PlayerContextImpl) admin;
            _admin.setCurrentLocation(teleportedPlayer.getCurrentLocation());
            _admin.setPastLocation(teleportedPlayer.getPastLocation());
        } else {
            playerRepo.save(teleportedPlayer);
        }
    }

    private void notifyTeleportingAdmin(PlayerContextImpl teleportedPlayer, Location targetLocation, PlayerResponse adminResponse) {
        String adminEvent = eventService.getEvent("admin.command.success.teleportation.notification", teleportedPlayer.getName(), targetLocation.getName());
        adminResponse.getPlayerEvents().add(adminEvent);
    }

    private void notifyPlayersOnTargetLocation(PlayerContextImpl teleportedPlayer) {
        String playersOnTargetLocationEvent = eventService.getEvent("admin.command.player.teleportation.notification.broadcast.in", teleportedPlayer.getName());
        multiplayerNotificationService.broadcast(locationRepo.get(teleportedPlayer.getCurrentLocation()), playersOnTargetLocationEvent, teleportedPlayer);
    }

    private void notifyPlayersOnPastLocation(PlayerContextImpl teleportedPlayer) {
        String playersOnSourceLocationEvent = eventService.getEvent("admin.command.player.teleportation.notification.broadcast.out", teleportedPlayer.getName());
        multiplayerNotificationService.broadcast(locationRepo.get(teleportedPlayer.getPastLocation()), playersOnSourceLocationEvent, teleportedPlayer);
    }

    private void notifyTeleportedPlayer(PlayerContextImpl teleportedPlayer, Location targetLocation, PlayerContext admin) {
        PlayerResponse teleportedPlayerResponse = new PlayerResponseImpl();
        movementService.showCurrentLocation(teleportedPlayer, teleportedPlayerResponse);
        String playerEvent = eventService.getEvent("admin.command.player.teleportation.notification", admin.getName(), targetLocation.getName());
        teleportedPlayerResponse.getPlayerEvents().add(playerEvent);
        multiplayerNotificationService.send(teleportedPlayer, teleportedPlayerResponse);
    }

    @Override
    public void give(String player, int amount, String itemName, PlayerContext admin, PlayerResponse adminResponse) {
        if(isAuthorized(admin, adminResponse)) {

        }
    }

    @Override
    public void give(int amount, String itemName, PlayerContext admin, PlayerResponse adminResponse) {
        this.give(admin.getName(), amount, itemName, admin, adminResponse);
    }

    @Override
    public void put(String locationId, int amount, String itemName, PlayerContext admin, PlayerResponse adminResponse) {
        if(isAuthorized(admin, adminResponse)) {

        }
    }

    @Override
    public void put(int amount, String itemName, PlayerContext admin, PlayerResponse adminResponse) {
        this.put(admin.getCurrentLocation(), amount, itemName, admin, adminResponse);
    }

    private boolean isAuthorized(PlayerContext player, PlayerResponse response) {
        boolean isAuthorized = (boolean) scriptRunnerService.execute(AUTHORIZATION_CHECK_SCRIPT_ID, player, response);
        if(!isAuthorized) {
            log.warn("Player {} tried to execute secured command!", player.getName());
        }
        return isAuthorized;
    }
}
