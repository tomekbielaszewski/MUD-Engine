package org.grizz.game.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.exception.CantGoThereException;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.enums.Direction;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.LocationService;
import org.grizz.game.service.MovementService;
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

    @Override
    public void move(@NonNull final Direction dir, @NonNull final PlayerContext playerContext, @NonNull final PlayerResponse response) {
        Location currentLocation = locationRepo.get(playerContext.getCurrentLocation());

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
            throw new CantGoThereException();
        }
    }

    @Override
    public void showCurrentLocation(PlayerContext playerContext, PlayerResponse _response) {
        PlayerResponseImpl response = (PlayerResponseImpl) _response;
        Location currentLocation = locationRepo.get(playerContext.getCurrentLocation());
        List<String> locationExits = locationService.getLocationExits(playerContext);
        List<Item> locationItems = locationService.getLocationItems(playerContext);

        response.setPossibleExits(locationExits);
        response.setLocationItems(locationItems);
        response.setCurrentLocation(currentLocation);
    }

    private void move(Supplier<String> locationSupplier, PlayerContext _context, Location currentLocation, PlayerResponse _response) {
        PlayerContextImpl context = (PlayerContextImpl) _context;
        PlayerResponseImpl response = (PlayerResponseImpl) _response;
        Location targetLocation = locationRepo.get(locationSupplier.get());

        //TODO: Here add events: beforeEnter, onEnter, beforeLeave, onLeave
        log.info("{} moved from ID[{}] to ID[{}]", context.getName(), currentLocation.getName(), targetLocation.getName());

        context.setTo(context.copy()
                        .pastLocation(currentLocation.getId())
                        .currentLocation(targetLocation.getId())
                        .build()
        );

        showCurrentLocation(_context, response);
    }
}
