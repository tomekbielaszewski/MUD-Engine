package org.grizz.game.command.provider;

import org.grizz.game.command.Command;
import org.grizz.game.command.parsers.system.LookAroundCommand;
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

    @InjectMocks
    private SystemCommandsProvider commandsProvider = new SystemCommandsProvider();

    @Before
    public void setUp() throws Exception {
        commandsProvider.setCommands(lookAroundCommand);
    }

    @Test
    public void testProvide() throws Exception {
        Player player = Player.builder().build();

        List<Command> result = commandsProvider.provide(player);

        assertThat(result, hasSize(1));
        assertThat(result, hasItems(
                lookAroundCommand
        ));
    }
}