package org.grizz.game.command.engine;

import com.google.common.collect.Lists;
import org.grizz.game.command.Command;
import org.grizz.game.command.provider.SystemCommandsProvider;
import org.grizz.game.model.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandListBuilderTest {
    private final Player player = dummyPlayer();

    @Mock
    private Command command;

    @Mock
    private SystemCommandsProvider systemCommandsProvider;

    @InjectMocks
    private CommandListBuilder commandListBuilder = new CommandListBuilder();

    @Test
    public void buildsListWithSystemCommands() {
        ArrayList<Command> commands = Lists.newArrayList(command);
        when(systemCommandsProvider.provide(player)).thenReturn(commands);

        List<Command> builtList = commandListBuilder.build(player);

//        assertThat(builtList, contain);
    }

    private Player dummyPlayer() {
        return new Player();
    }
}