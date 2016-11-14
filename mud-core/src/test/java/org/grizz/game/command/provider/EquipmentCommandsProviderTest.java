package org.grizz.game.command.provider;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.grizz.game.command.Command;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.Player;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.ScriptCommandDto;
import org.grizz.game.model.items.Weapon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentCommandsProviderTest {

    @InjectMocks
    private EquipmentCommandsProvider provider = new EquipmentCommandsProvider();

    @Test
    public void collectsCommandsFromEquippedItems() throws Exception {
        Equipment equipment = Equipment.builder()
                .headItem(dummyArmor(dummyCommandScriptDto("1")))
                .torsoItem(dummyArmor(dummyCommandScriptDto("2")))
                .handsItem(dummyArmor(dummyCommandScriptDto("3")))
                .legsItem(dummyArmor(dummyCommandScriptDto("4")))
                .feetItem(dummyArmor(dummyCommandScriptDto("5")))
                .meleeWeapon(dummyWeapon(dummyCommandScriptDto("6")))
                .rangeWeapon(dummyWeapon(dummyCommandScriptDto("7")))
                .backpack(Lists.newArrayList())
                .build();
        Player player = dummyPlayer(equipment);

        List<Command> commands = provider.provide(player);

        assertThat(commands, hasSize(7));
        assertThat(Sets.newHashSet(commands), hasSize(7));
    }

    @Test
    public void collectsCommandsFromBackpackItems() throws Exception {
        Equipment equipment = Equipment.builder()
                .backpack(Lists.newArrayList(
                        dummyArmor(dummyCommandScriptDto("1")),
                        dummyArmor(dummyCommandScriptDto("2")),
                        dummyWeapon(dummyCommandScriptDto("3"))
                ))
                .build();
        Player player = dummyPlayer(equipment);

        List<Command> commands = provider.provide(player);

        assertThat(commands, hasSize(3));
        assertThat(Sets.newHashSet(commands), hasSize(3));
    }

    @Test
    public void collectsMultipleCommandsFromSingleItem() throws Exception {
        Equipment equipment = Equipment.builder()
                .backpack(Lists.newArrayList(
                        dummyArmor(
                                dummyCommandScriptDto("1"),
                                dummyCommandScriptDto("2"),
                                dummyCommandScriptDto("3"),
                                dummyCommandScriptDto("4")
                        )
                ))
                .build();
        Player player = dummyPlayer(equipment);

        List<Command> commands = provider.provide(player);

        assertThat(commands, hasSize(4));
        assertThat(Sets.newHashSet(commands), hasSize(4));
    }

    @Test
    public void collectsZeroCommandsFromNonEmptyBackpack() throws Exception {
        Equipment equipment = Equipment.builder()
                .backpack(Lists.newArrayList(
                        dummyArmor(),
                        dummyArmor(),
                        dummyArmor(),
                        dummyWeapon()
                ))
                .build();
        Player player = dummyPlayer(equipment);

        List<Command> commands = provider.provide(player);

        assertThat(commands, hasSize(0));
    }

    @Test
    public void collectsZeroCommandsFromEmptyEquipment() throws Exception {
        Equipment equipment = Equipment.builder()
                .backpack(Lists.newArrayList())
                .build();
        Player player = dummyPlayer(equipment);

        List<Command> commands = provider.provide(player);

        assertThat(commands, hasSize(0));
    }

    private Player dummyPlayer(Equipment equipment) {
        return Player.builder().equipment(equipment).build();
    }

    private ScriptCommandDto dummyCommandScriptDto(String id) {
        return ScriptCommandDto.builder()
                .command(id)
                .scriptId(id)
                .build();
    }

    private Armor dummyArmor(ScriptCommandDto... commands) {
        return Armor.builder()
                .commands(Lists.newArrayList(commands))
                .build();
    }

    private Weapon dummyWeapon(ScriptCommandDto... commands) {
        return Weapon.builder()
                .commands(Lists.newArrayList(commands))
                .build();
    }
}