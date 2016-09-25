package org.grizz.game.command.executors.system;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.grizz.game.exception.CantGoThereException;
import org.grizz.game.model.Direction;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.EventService;
import org.grizz.game.service.notifier.MultiplayerNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MoveCommandExecutor {
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private Environment env;
    @Autowired
    private LookAroundCommandExecutor lookAroundCommandExecutor;
    @Autowired
    private EventService eventService;
    @Autowired
    private MultiplayerNotificationService notificationService;

    public void move(Direction direction, Player player, PlayerResponse response) {
        Location sourceLocation = locationRepo.get(player.getCurrentLocation());
        String targetLocationId = direction.from(sourceLocation);

        if (StringUtils.isBlank(targetLocationId))
            throw new CantGoThereException("cant.go.there", env.getProperty("go.to." + direction.name().toLowerCase()));

        Location targetLocation = locationRepo.get(targetLocationId);

        runMovementScripts();
        move(sourceLocation, targetLocation, player, response);
        broadcastMovement(sourceLocation, targetLocation, direction, player);
    }

    private void move(Location sourceLocation, Location targetLocation, Player player, PlayerResponse response) {
        player.setPastLocation(sourceLocation.getId());
        player.setCurrentLocation(targetLocation.getId());

        lookAroundCommandExecutor.lookAround(player, response);
    }

    private void broadcastMovement(Location sourceLocation, Location targetLocation, Direction direction, Player player) {
        String locationLeaveEvent = eventService.getEvent("multiplayer.event.player.left.location." + direction.name().toLowerCase(), player.getName());
        notificationService.broadcast(sourceLocation, locationLeaveEvent, player);

        String locationEnterEvent = eventService.getEvent("multiplayer.event.player.entered.location", player.getName());
        notificationService.broadcast(targetLocation, locationEnterEvent, player);
    }

    private void runMovementScripts() {
        //if before leave script allows to leave location. Not - > throw exception
        //on leave script
        //if on enter script allows to enter location. Not - > throw exception
        //on enter script
    }
}
