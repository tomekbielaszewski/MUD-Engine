package org.grizz.game.command.executors.system;

import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LookAroundCommandExecutorTest {

    @InjectMocks
    private LookAroundCommandExecutor executor = new LookAroundCommandExecutor();

    @Test
    public void addsCurrentLocationToPlayerResponse() throws Exception {
        PlayerResponse response = new PlayerResponse();
        Player player = Player.builder().build();

        executor.lookAround(player, response);

        assertThat(response.getCurrentLocation().getId(), is(player.getCurrentLocation()));
        throw new NotImplementedException();
    }
}