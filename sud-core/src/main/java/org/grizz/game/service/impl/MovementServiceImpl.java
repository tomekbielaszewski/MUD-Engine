package org.grizz.game.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.exception.CantGoThereException;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.Direction;
import org.grizz.game.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * Created by Grizz on 2015-04-26.
 */
@Slf4j
@Service
public class MovementServiceImpl implements MovementService {
    @Autowired
    private LocationRepo locationRepo;

    @Override
    public void move(@NonNull Direction dir, @NonNull PlayerContext playerContext) {
        Location currentLocation = locationRepo.get(playerContext.getCurrentLocation());

        try {
            switch (dir) {
                case NORTH:
                    move(currentLocation::getNorth, playerContext, currentLocation);
                    break;
                case SOUTH:
                    move(currentLocation::getSouth, playerContext, currentLocation);
                    break;
                case EAST:
                    move(currentLocation::getEast, playerContext, currentLocation);
                    break;
                case WEST:
                    move(currentLocation::getWest, playerContext, currentLocation);
                    break;
                case UP:
                    move(currentLocation::getUp, playerContext, currentLocation);
                    break;
                case DOWN:
                    move(currentLocation::getDown, playerContext, currentLocation);
                    break;
            }
        } catch (IllegalArgumentException e) {
            log.warn("{} tried to go from ID[{}] to [{}]: {}", playerContext.getName(), currentLocation.getId(), dir, e.getMessage());
            throw new CantGoThereException();
        }
    }

    private void move(Supplier<String> locationSupplier, PlayerContext playerContext, Location currentLocation) {
        PlayerContextImpl player = (PlayerContextImpl) playerContext;
        Location targetLocation = locationRepo.get(locationSupplier.get());

        log.info("{} moved from ID[{}] to ID[{}]", player.getName(), currentLocation.getName(), targetLocation.getName());

        player.setTo(player.copy()
                        .pastLocation(currentLocation.getId())
                        .currentLocation(targetLocation.getId())
                        .build()
        );
    }
}
