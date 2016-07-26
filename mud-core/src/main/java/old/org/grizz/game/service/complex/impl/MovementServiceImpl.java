package old.org.grizz.game.service.complex.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import old.org.grizz.game.exception.CantGoThereException;
import old.org.grizz.game.exception.GameExceptionHandler;
import old.org.grizz.game.exception.NoSuchLocationException;
import old.org.grizz.game.model.Location;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.model.enums.Direction;
import old.org.grizz.game.model.impl.PlayerContextImpl;
import old.org.grizz.game.model.impl.PlayerResponseImpl;
import old.org.grizz.game.model.items.Item;
import old.org.grizz.game.model.repository.LocationRepo;
import old.org.grizz.game.service.complex.MovementService;
import old.org.grizz.game.service.complex.MultiplayerNotificationService;
import old.org.grizz.game.service.complex.ScriptRunnerService;
import old.org.grizz.game.service.simple.EventService;
import old.org.grizz.game.service.simple.LocationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by Grizz on 2015-04-26.
 */
@Slf4j
@Service
public class MovementServiceImpl implements MovementService {
    @Autowired
    private LocationRepo locationRepo;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ScriptRunnerService scriptRunnerService;

    @Autowired
    private MultiplayerNotificationService notificationService;

    @Autowired
    private EventService eventService;

    @Autowired
    private GameExceptionHandler gameExceptionHandler;

    @Override
    public void moveRunningScripts(@NonNull final Direction dir, @NonNull final PlayerContext playerContext, @NonNull final PlayerResponse response) {
        Location currentLocation = locationService.getCurrentLocation(playerContext);

        try {
            switch (dir) {
                case NORTH:
                    moveRunningScripts(currentLocation.getNorth(), playerContext, currentLocation, response);
                    break;
                case SOUTH:
                    moveRunningScripts(currentLocation.getSouth(), playerContext, currentLocation, response);
                    break;
                case EAST:
                    moveRunningScripts(currentLocation.getEast(), playerContext, currentLocation, response);
                    break;
                case WEST:
                    moveRunningScripts(currentLocation.getWest(), playerContext, currentLocation, response);
                    break;
                case UP:
                    moveRunningScripts(currentLocation.getUp(), playerContext, currentLocation, response);
                    break;
                case DOWN:
                    moveRunningScripts(currentLocation.getDown(), playerContext, currentLocation, response);
                    break;
            }
        } catch (NoSuchLocationException e) {
            String exceptionMessage = gameExceptionHandler.handle(e);
            log.warn("{} tried to go from ID[{}] to [{}]: {}", playerContext.getName(), currentLocation.getId(), dir, exceptionMessage);
            throw new CantGoThereException("cant.go.there");
        }
    }

    @Override
    public void teleport(@NonNull final String targetLocationId, @NonNull final PlayerContext _context, @NonNull final PlayerResponse _response) {
        PlayerContextImpl context = (PlayerContextImpl) _context;
        PlayerResponseImpl response = (PlayerResponseImpl) _response;

        Location currentLocation = locationService.getCurrentLocation(context);
        Location targetLocation = locationRepo.get(targetLocationId);

        move(currentLocation, targetLocation, context, response);
    }

    @Override
    public void showCurrentLocation(PlayerContext _context, PlayerResponse _response) {
        PlayerContextImpl context = (PlayerContextImpl) _context;
        PlayerResponseImpl response = (PlayerResponseImpl) _response;

        Location currentLocation = locationService.getCurrentLocation(context);
        List<String> locationExits = locationService.getExits(currentLocation);
        List<String> players = locationService.getPlayersOnLocation(currentLocation);
        List<Item> locationItems = locationService.getCurrentLocationItems(context);
        List<Item> locationStaticItems = locationService.getCurrentLocationStaticItems(context);

        List<String> playersExceptCurrent = players.stream()
                .filter(playerName -> !playerName.equals(context.getName()))
                .collect(Collectors.toList());

        response.setPossibleExits(locationExits);
        response.setPlayersOnLocation(playersExceptCurrent);
        response.setLocationStaticItems(locationStaticItems);
        response.setLocationItems(locationItems);
        response.setCurrentLocation(currentLocation);

        actionScript(currentLocation::getOnShow, context, response);
    }

    private void moveRunningScripts(String targetLocationId, PlayerContext _context, Location currentLocation, PlayerResponse _response) {
        PlayerContextImpl context = (PlayerContextImpl) _context;
        PlayerResponseImpl response = (PlayerResponseImpl) _response;

        if (predicateScript(currentLocation::getBeforeLeave, context, response)) {
            actionScript(currentLocation::getOnLeave, context, response);

            Location targetLocation = locationRepo.get(targetLocationId);

            if (predicateScript(targetLocation::getBeforeEnter, context, response)) {
                actionScript(targetLocation::getOnEnter, context, response);
                move(currentLocation, targetLocation, context, response);
                broadcastMovement(currentLocation, targetLocation, context);
            } else {
                log.info("{} was denied to move from [{}] to [{}]", context.getName(), currentLocation.getName(), targetLocation.getName());
            }
        } else {
            log.info("{} was denied to move from [{}]", context.getName(), currentLocation.getName());
        }
    }

    private void move(Location currentLocation, Location targetLocation, PlayerContextImpl context, PlayerResponseImpl response) {
        context.setPastLocation(currentLocation.getId());
        context.setCurrentLocation(targetLocation.getId());

        showCurrentLocation(context, response);
        log.info("{} moved from [{}] to [{}]", context.getName(), currentLocation.getName(), targetLocation.getName());
    }

    private void broadcastMovement(Location currentLocation, Location targetLocation, PlayerContextImpl context) {
        String locationLeaveEvent = eventService.getEvent("multiplayer.event.player.left.location", context.getName());
        notificationService.broadcast(currentLocation, locationLeaveEvent, context);

        String locationEnterEvent = eventService.getEvent("multiplayer.event.player.entered.location", context.getName());
        notificationService.broadcast(targetLocation, locationEnterEvent, context);
    }

    private boolean predicateScript(Supplier<String> scriptSupplier, PlayerContext context, PlayerResponse response) {
        if (StringUtils.isEmpty(scriptSupplier.get())) {
            return true;
        }
        return (boolean) scriptRunnerService.execute(scriptSupplier.get(), context, response);
    }

    private void actionScript(Supplier<String> scriptSupplier, PlayerContext context, PlayerResponse response) {
        if (StringUtils.isEmpty(scriptSupplier.get())) {
            return;
        }
        scriptRunnerService.execute(scriptSupplier.get(), context, response);
    }
}
