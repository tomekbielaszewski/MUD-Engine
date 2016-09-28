package org.grizz.game.command.executors.system;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.grizz.game.exception.CantGoThereException;
import org.grizz.game.model.Direction;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.EventService;
import org.grizz.game.service.notifier.MultiplayerNotificationService;
import org.grizz.game.service.script.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    @Autowired
    private ScriptRunner scriptRunner;
    @Autowired
    private ScriptRepo scriptRepo;

    public void move(Direction direction, Player player, PlayerResponse response) {
        Location sourceLocation = locationRepo.get(player.getCurrentLocation());
        String targetLocationId = direction.goFrom(sourceLocation);

        if (StringUtils.isBlank(targetLocationId))
            throw new CantGoThereException("cant.go.there", env.getProperty("go.to." + direction.name().toLowerCase()));

        Location targetLocation = locationRepo.get(targetLocationId);

        if (playerCanMove(sourceLocation, targetLocation, player, response)) {
            move(sourceLocation, targetLocation, player, response);
            broadcastMovement(sourceLocation, targetLocation, direction, player);
        }
        lookAroundCommandExecutor.lookAround(player, response);
    }

    private void move(Location sourceLocation, Location targetLocation, Player player, PlayerResponse response) {
        player.setPastLocation(sourceLocation.getId());
        player.setCurrentLocation(targetLocation.getId());
    }

    private void broadcastMovement(Location sourceLocation, Location targetLocation, Direction direction, Player player) {
        String locationLeaveEvent = eventService.getEvent("multiplayer.event.player.left.location." + direction.name().toLowerCase(), player.getName());
        notificationService.broadcast(sourceLocation, locationLeaveEvent, player);

        String locationEnterEvent = eventService.getEvent("multiplayer.event.player.entered.location", player.getName());
        notificationService.broadcast(targetLocation, locationEnterEvent, player);
    }

    private boolean playerCanMove(Location sourceLocation, Location targetLocation, Player player, PlayerResponse response) {
        if (runMovementEventScript(sourceLocation.getBeforeLeaveScript(), player, response) &&
                runMovementEventScript(targetLocation.getBeforeEnterScript(), player, response)) {
            runMovementEventScript(sourceLocation.getOnLeaveScript(), player, response);
            runMovementEventScript(targetLocation.getOnEnterScript(), player, response);
        } else {
            return false;
        }
        return true;
    }

    private boolean runMovementEventScript(String id, Player player, PlayerResponse response) {
        boolean allowMovementByDefault = true;
        Optional<Boolean> optionalResponse = Optional.ofNullable(id)
                .map(scriptId -> scriptRepo.get(scriptId))
                .map(script -> scriptRunner.execute(script, player, response, Boolean.class));
        return optionalResponse.orElse(allowMovementByDefault);
    }
}
