package old.org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import old.org.grizz.game.TestContext;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.model.enums.Direction;
import old.org.grizz.game.model.impl.EquipmentEntity;
import old.org.grizz.game.model.impl.PlayerContextImpl;
import old.org.grizz.game.model.impl.PlayerResponseImpl;
import old.org.grizz.game.model.repository.ItemRepo;
import old.org.grizz.game.service.complex.MovementService;
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

        move(Direction.NORTH, "2", context, response);
        move(Direction.WEST, "3", context, response);
        move(Direction.EAST, "2", context, response);
        move(Direction.EAST, "4", context, response);
        move(Direction.WEST, "2", context, response);
        move(Direction.NORTH, "5", context, response);
        move(Direction.SOUTH, "2", context, response);
        move(Direction.NORTH, "5", context, response);
        move(Direction.NORTH, "6", context, response);
        move(Direction.SOUTH, "5", context, response);
        move(Direction.NORTH, "6", context, response);
        move(Direction.EAST, "7", context, response);
        move(Direction.WEST, "6", context, response);
        move(Direction.EAST, "7", context, response);
        move(Direction.NORTH, "8", context, response);
        move(Direction.NORTH, "9", context, response);
        move(Direction.EAST, "12", context, response);
        move(Direction.EAST, "13", context, response);
        move(Direction.WEST, "12", context, response);
        move(Direction.WEST, "9", context, response);
        move(Direction.WEST, "10", context, response);
        move(Direction.NORTH, "14", context, response);
        move(Direction.EAST, "15", context, response);
        move(Direction.EAST, "16", context, response);
        move(Direction.EAST, "17", context, response);
        move(Direction.WEST, "16", context, response);
        move(Direction.WEST, "15", context, response);
        move(Direction.SOUTH, "9", context, response);
        move(Direction.NORTH, "15", context, response);
        move(Direction.WEST, "14", context, response);
        move(Direction.SOUTH, "10", context, response);
        move(Direction.WEST, "11", context, response);
        move(Direction.WEST, "19", context, response);
        move(Direction.NORTH, "20", context, response);
        move(Direction.EAST, "18", context, response);
        move(Direction.SOUTH, "11", context, response);
        move(Direction.WEST, "19", context, response);
        move(Direction.NORTH, "20", context, response);
        move(Direction.NORTH, "21", context, response);
        move(Direction.NORTH, "22", context, response);
        move(Direction.EAST, "25", context, response);
        move(Direction.SOUTH, "26", context, response);
        move(Direction.NORTH, "25", context, response);
        move(Direction.WEST, "22", context, response);
        move(Direction.NORTH, "23", context, response);
        move(Direction.EAST, "27", context, response);
        move(Direction.WEST, "23", context, response);
        move(Direction.NORTH, "24", context, response);
        move(Direction.EAST, "28", context, response);
        move(Direction.WEST, "24", context, response);
        move(Direction.SOUTH, "23", context, response);
        move(Direction.SOUTH, "22", context, response);
        move(Direction.SOUTH, "21", context, response);
        move(Direction.SOUTH, "20", context, response);
        move(Direction.SOUTH, "19", context, response);
        move(Direction.SOUTH, "29", context, response);
        move(Direction.SOUTH, "32", context, response);
        move(Direction.SOUTH, "34", context, response);
        move(Direction.NORTH, "32", context, response);
        move(Direction.EAST, "33", context, response);
        move(Direction.WEST, "32", context, response);
        move(Direction.NORTH, "29", context, response);
        move(Direction.EAST, "30", context, response);
        move(Direction.EAST, "31", context, response);
        move(Direction.WEST, "30", context, response);
        move(Direction.WEST, "29", context, response);
        move(Direction.NORTH, "19", context, response);
        move(Direction.EAST, "11", context, response);
        move(Direction.EAST, "10", context, response);
        move(Direction.EAST, "9", context, response);
        move(Direction.SOUTH, "8", context, response);
        move(Direction.SOUTH, "7", context, response);
        move(Direction.WEST, "6", context, response);
        move(Direction.SOUTH, "5", context, response);
        move(Direction.SOUTH, "2", context, response);
    }

    private void move(Direction dir, String targetLocationId, PlayerContext context, PlayerResponse response) {
        movementService.moveRunningScripts(dir, context, response);
        Assert.assertEquals(targetLocationId, context.getCurrentLocation());
    }
}
