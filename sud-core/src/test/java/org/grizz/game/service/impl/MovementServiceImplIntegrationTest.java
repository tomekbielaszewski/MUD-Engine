package org.grizz.game.service.impl;

import org.grizz.game.config.GameConfig;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.service.Direction;
import org.grizz.game.service.MovementService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
@ContextConfiguration(classes = {GameConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class MovementServiceImplIntegrationTest {
    @Autowired
    private MovementService movementService;

    @Test
    public void mainCityWalkthroughTest() {
        PlayerContextImpl context = PlayerContextImpl.builder()
                .currentLocation("1")
                .build();
        movementService.move(Direction.NORTH, context);// 2
        movementService.move(Direction.SOUTH, context);// 1
        movementService.move(Direction.NORTH, context);// 2
        movementService.move(Direction.WEST, context);// 3
        movementService.move(Direction.EAST, context);// 2
        movementService.move(Direction.EAST, context);// 4
        movementService.move(Direction.WEST, context);// 2
        movementService.move(Direction.NORTH, context);// 5
        movementService.move(Direction.SOUTH, context);// 2
        movementService.move(Direction.NORTH, context);// 5
        movementService.move(Direction.NORTH, context);// 6
        movementService.move(Direction.SOUTH, context);// 5
        movementService.move(Direction.NORTH, context);// 6
        movementService.move(Direction.EAST, context);// 7
        movementService.move(Direction.WEST, context);// 6
        movementService.move(Direction.EAST, context);// 7
        movementService.move(Direction.NORTH, context);// 8
        movementService.move(Direction.SOUTH, context);// 7
        movementService.move(Direction.WEST, context);// 6
        movementService.move(Direction.SOUTH, context);// 5
        movementService.move(Direction.SOUTH, context);// 2
        movementService.move(Direction.SOUTH, context);// 1

        Assert.assertEquals("1", context.getCurrentLocation());
    }
}
