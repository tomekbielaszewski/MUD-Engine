package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.grizz.game.config.GameConfig;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.enums.Direction;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.impl.items.ItemStackEntity;
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
                .name("TestUser")
                .currentLocation("1")
                .equipment(Lists.newArrayList(
                        ItemStackEntity.builder()//zardzewialy klucz jest potrzebny aby wejsc na lokacje 4
                                .itemId("7")
                                .quantity(1)
                                .build()
                ))
                .build();
        PlayerResponse response = new PlayerResponseImpl();

        movementService.move(Direction.NORTH, context, response);// 2
        movementService.move(Direction.WEST, context, response);// 3
        movementService.move(Direction.EAST, context, response);// 2
        movementService.move(Direction.EAST, context, response);// 4
        movementService.move(Direction.WEST, context, response);// 2
        movementService.move(Direction.NORTH, context, response);// 5
        movementService.move(Direction.SOUTH, context, response);// 2
        movementService.move(Direction.NORTH, context, response);// 5
        movementService.move(Direction.NORTH, context, response);// 6
        movementService.move(Direction.SOUTH, context, response);// 5
        movementService.move(Direction.NORTH, context, response);// 6
        movementService.move(Direction.EAST, context, response);// 7
        movementService.move(Direction.WEST, context, response);// 6
        movementService.move(Direction.EAST, context, response);// 7
        movementService.move(Direction.NORTH, context, response);// 8
        movementService.move(Direction.SOUTH, context, response);// 7
        movementService.move(Direction.WEST, context, response);// 6
        movementService.move(Direction.SOUTH, context, response);// 5
        movementService.move(Direction.SOUTH, context, response);// 2
        movementService.move(Direction.SOUTH, context, response);// 2

        Assert.assertEquals("2", context.getCurrentLocation());
    }
}
