package org.grizz.game.command.parsers.system;

import org.grizz.game.command.executors.system.ShowEquipmentCommandExecutor;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShowEquipmentCommandTest {
    @Mock
    private Environment environment;
    @Mock
    private ShowEquipmentCommandExecutor commandExecutor;

    @InjectMocks
    private ShowEquipmentCommand command = new ShowEquipmentCommand(environment);

    @Before
    public void setUp() {
        when(environment.getProperty(command.getClass().getCanonicalName()))
                .thenReturn("ekwi;" +
                        "ekwipunek;" +
                        "pokaz ekwipunek;" +
                        "otworz ekwipunek;" +
                        "przejrzyj ekwipunek");
    }

    @Test
    public void accept() throws Exception {
        assertTrue(command.accept("ekwi"));
        assertTrue(command.accept("ekwipunek"));
        assertTrue(command.accept("pokaz ekwipunek"));
        assertTrue(command.accept("otworz ekwipunek"));
        assertTrue(command.accept("przejrzyj ekwipunek"));
    }

    @Test
    public void execute() throws Exception {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();

        command.execute("ekwi", player, response);

        verify(commandExecutor).showEquipment(player, response);
    }
}