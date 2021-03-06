package org.grizz.game.command.provider;

import org.grizz.game.command.Command;
import org.grizz.game.command.parsers.admin.AdminGiveItemCommand;
import org.grizz.game.command.parsers.admin.AdminPutItemCommand;
import org.grizz.game.command.parsers.admin.AdminShowActivePlayerListCommand;
import org.grizz.game.command.parsers.admin.AdminTeleportCommand;
import org.grizz.game.command.parsers.system.DropCommand;
import org.grizz.game.command.parsers.system.LookAroundCommand;
import org.grizz.game.command.parsers.system.ShowEquipmentCommand;
import org.grizz.game.command.parsers.system.movement.*;
import org.grizz.game.model.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SystemCommandsProviderTest {
    @Mock
    private LookAroundCommand lookAroundCommand;
    @Mock
    private NorthMoveCommand northMoveCommand;
    @Mock
    private SouthMoveCommand southMoveCommand;
    @Mock
    private WestMoveCommand westMoveCommand;
    @Mock
    private EastMoveCommand eastMoveCommand;
    @Mock
    private DownMoveCommand downMoveCommand;
    @Mock
    private UpMoveCommand upMoveCommand;
    @Mock
    private ShowEquipmentCommand showEquipmentCommand;
    @Mock
    private DropCommand dropCommand;
    @Mock
    private DropCommand pickUpCommand;
    @Mock
    private AdminShowActivePlayerListCommand adminShowActivePlayerListCommand;
    @Mock
    private AdminTeleportCommand adminTeleportCommand;
    @Mock
    private AdminPutItemCommand adminPutItemCommand;
    @Mock
    private AdminGiveItemCommand adminGiveItemCommand;

    @InjectMocks
    private SystemCommandsProvider commandsProvider = new SystemCommandsProvider();

    @Before
    public void setUp() throws Exception {
        commandsProvider.setCommands(lookAroundCommand, northMoveCommand, southMoveCommand, westMoveCommand,
                eastMoveCommand, upMoveCommand, downMoveCommand, showEquipmentCommand, dropCommand, pickUpCommand,
                adminShowActivePlayerListCommand, adminTeleportCommand, adminPutItemCommand, adminGiveItemCommand);
    }

    @Test
    public void testProvide() throws Exception {
        Player player = Player.builder().build();

        List<Command> result = commandsProvider.provide(player);

        assertThat(result, hasSize(14));
        assertThat(result, hasItems(
                lookAroundCommand,
                northMoveCommand,
                southMoveCommand,
                westMoveCommand,
                eastMoveCommand,
                upMoveCommand,
                downMoveCommand,
                showEquipmentCommand,
                dropCommand,
                pickUpCommand,
                adminShowActivePlayerListCommand,
                adminTeleportCommand,
                adminPutItemCommand,
                adminGiveItemCommand
        ));
    }
}