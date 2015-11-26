package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.grizz.game.TestContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.enums.Direction;
import org.grizz.game.model.impl.EquipmentEntity;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.complex.MovementService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
@ContextConfiguration(classes = {TestContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class MovementServiceImplIntegrationTest {
    @Autowired
    private MovementService movementService;
    @Autowired
    private ItemRepo itemRepo;

    @Test
    public void mainCityWalkthroughTest() {
        PlayerContextImpl context = PlayerContextImpl.builder()
                .name("TestUser")
                .currentLocation("1")
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(
                                itemRepo.getByName("Zardzewiały klucz")
                        ))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();

        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("2", context.getCurrentLocation());// 2
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("3", context.getCurrentLocation());// 3
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("2", context.getCurrentLocation());// 2
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("4", context.getCurrentLocation());// 4
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("2", context.getCurrentLocation());// 2
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("5", context.getCurrentLocation());// 5
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("2", context.getCurrentLocation());// 2
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("5", context.getCurrentLocation());// 5
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("6", context.getCurrentLocation());// 6
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("5", context.getCurrentLocation());// 5
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("6", context.getCurrentLocation());// 6
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("7", context.getCurrentLocation());// 7
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("6", context.getCurrentLocation());// 6
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("7", context.getCurrentLocation());// 7
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("8", context.getCurrentLocation());// 8
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("9", context.getCurrentLocation());// 9
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("12", context.getCurrentLocation());// 12
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("13", context.getCurrentLocation());// 13
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("12", context.getCurrentLocation());// 12
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("9", context.getCurrentLocation());// 9
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("10", context.getCurrentLocation());// 10
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("14", context.getCurrentLocation());// 14
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("15", context.getCurrentLocation());// 15
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("16", context.getCurrentLocation());// 16
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("17", context.getCurrentLocation());// 17
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("16", context.getCurrentLocation());// 16
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("15", context.getCurrentLocation());// 15
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("9", context.getCurrentLocation());// 9
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("15", context.getCurrentLocation());// 15
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("14", context.getCurrentLocation());// 14
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("10", context.getCurrentLocation());// 10
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("11", context.getCurrentLocation());// 11
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("19", context.getCurrentLocation());// 19
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("20", context.getCurrentLocation());// 20
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("18", context.getCurrentLocation());// 18
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("11", context.getCurrentLocation());// 11
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("19", context.getCurrentLocation());// 19
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("20", context.getCurrentLocation());// 20
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("21", context.getCurrentLocation());// 21
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("22", context.getCurrentLocation());// 22
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("25", context.getCurrentLocation());// 25
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("26", context.getCurrentLocation());// 26
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("25", context.getCurrentLocation());// 25
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("22", context.getCurrentLocation());// 22
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("23", context.getCurrentLocation());// 23
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("27", context.getCurrentLocation());// 27
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("23", context.getCurrentLocation());// 23
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("24", context.getCurrentLocation());// 24
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("28", context.getCurrentLocation());// 28
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("24", context.getCurrentLocation());// 24
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("23", context.getCurrentLocation());// 23
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("22", context.getCurrentLocation());// 22
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("21", context.getCurrentLocation());// 21
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("20", context.getCurrentLocation());// 20
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("19", context.getCurrentLocation());// 19
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("29", context.getCurrentLocation());// 29
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("32", context.getCurrentLocation());// 32
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("34", context.getCurrentLocation());// 34
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("32", context.getCurrentLocation());// 32
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("33", context.getCurrentLocation());// 33
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("32", context.getCurrentLocation());// 32
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("29", context.getCurrentLocation());// 29
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("30", context.getCurrentLocation());// 30
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("31", context.getCurrentLocation());// 31
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("30", context.getCurrentLocation());// 30
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("29", context.getCurrentLocation());// 29
        movementService.move(Direction.NORTH, context, response);	Assert.assertEquals("19", context.getCurrentLocation());// 19
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("11", context.getCurrentLocation());// 11
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("10", context.getCurrentLocation());// 10
        movementService.move(Direction.EAST, context, response);	Assert.assertEquals("9", context.getCurrentLocation());// 9
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("8", context.getCurrentLocation());// 8
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("7", context.getCurrentLocation());// 7
        movementService.move(Direction.WEST, context, response);	Assert.assertEquals("6", context.getCurrentLocation());// 6
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("5", context.getCurrentLocation());// 5
        movementService.move(Direction.SOUTH, context, response);	Assert.assertEquals("2", context.getCurrentLocation());// 2
    }
}
