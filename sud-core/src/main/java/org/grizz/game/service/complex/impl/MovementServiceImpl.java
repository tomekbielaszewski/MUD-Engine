package org.grizz.game.service.complex.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.grizz.game.exception.CantGoThereException;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.enums.Direction;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.complex.MovementService;
import org.grizz.game.service.complex.MultiplayerNotificationService;
import org.grizz.game.service.complex.ScriptRunnerService;
import org.grizz.game.service.simple.EventService;
import org.grizz.game.service.simple.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

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

    @Override
    public void move(@NonNull final Direction dir, @NonNull final PlayerContext playerContext, @NonNull final PlayerResponse response) {
        Location currentLocation = locationService.getCurrentLocation(playerContext);

        try {
            switch (dir) {
                case NORTH:
                    move(currentLocation::getNorth, playerContext, currentLocation, response);
                    break;
                case SOUTH:
                    move(currentLocation::getSouth, playerContext, currentLocation, response);
                    break;
                case EAST:
                    move(currentLocation::getEast, playerContext, currentLocation, response);
                    break;
                case WEST:
                    move(currentLocation::getWest, playerContext, currentLocation, response);
                    break;
                case UP:
                    move(currentLocation::getUp, playerContext, currentLocation, response);
                    break;
                case DOWN:
                    move(currentLocation::getDown, playerContext, currentLocation, response);
                    break;
            }
        } catch (IllegalArgumentException e) {
            log.warn("{} tried to go from ID[{}] to [{}]: {}", playerContext.getName(), currentLocation.getId(), dir, e.getMessage());
            throw new CantGoThereException("cant.go.there");
        }
    }

    @Override
    public void showCurrentLocation(PlayerContext _context, PlayerResponse _response) {
        PlayerResponseImpl response = (PlayerResponseImpl) _response;
        Location currentLocation = locationService.getCurrentLocation(_context);
        List<String> locationExits = locationService.getExits(currentLocation);
        List<Item> locationItems = locationService.getCurrentLocationItems(_context);
        List<Item> locationStaticItems = locationService.getCurrentLocationStaticItems(_context);

        response.setPossibleExits(locationExits);
        response.setLocationStaticItems(locationStaticItems);
        response.setLocationItems(locationItems);
        response.setCurrentLocation(currentLocation);

        actionScript(currentLocation::getOnShow, _context, _response);
    }

    private void move(Supplier<String> locationSupplier, PlayerContext _context, Location currentLocation, PlayerResponse _response) {
        PlayerContextImpl context = (PlayerContextImpl) _context;
        PlayerResponseImpl response = (PlayerResponseImpl) _response;

        if (predicateScript(currentLocation::getBeforeLeave, _context, _response)) {
            actionScript(currentLocation::getOnLeave, _context, _response);

            Location targetLocation = locationRepo.get(locationSupplier.get());

            if (predicateScript(targetLocation::getBeforeEnter, _context, _response)) {
                context.setPastLocation(currentLocation.getId());
                context.setCurrentLocation(targetLocation.getId());

                actionScript(targetLocation::getOnEnter, _context, _response);

                showCurrentLocation(_context, response);
                log.info("{} moved from [{}] to [{}]", context.getName(), currentLocation.getName(), targetLocation.getName());

                String locationLeaveEvent = eventService.getEvent("multiplayer.event.player.left.location", context.getName());
                notificationService.broadcast(currentLocation, locationLeaveEvent, context);

                String locationEnterEvent = eventService.getEvent("multiplayer.event.player.entered.location", context.getName());
                notificationService.broadcast(targetLocation, locationEnterEvent, context);
            } else {
                log.info("{} was denied to move from [{}] to [{}]", context.getName(), currentLocation.getName(), targetLocation.getName());
            }
        } else {
            log.info("{} was denied to move from [{}]", context.getName(), currentLocation.getName());
        }
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
