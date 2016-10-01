package org.grizz.game.command.executors;

import org.grizz.game.model.Equipment;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ShowEquipmentCommandExecutorTest {
    @InjectMocks
    private ShowEquipmentCommandExecutor commandExecutor = new ShowEquipmentCommandExecutor();

    @Test
    public void showEquipment() throws Exception {
        Equipment equipment = Equipment.builder().build();
        Player player = Player.builder().equipment(equipment).build();
        PlayerResponse response = new PlayerResponse();

        commandExecutor.showEquipment(player, response);

        assertThat(response.getEquipment(), is(player.getEquipment()));
    }
}