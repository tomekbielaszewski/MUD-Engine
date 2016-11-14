package org.grizz.game.command.provider;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.grizz.game.command.Command;
import org.grizz.game.model.Location;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.Player;
import org.grizz.game.model.items.ScriptCommandDto;
import org.grizz.game.model.items.Static;
import org.grizz.game.model.repository.LocationRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocationCommandsProviderTest {
    private static final String LOCATION = "location";
    @Mock
    private LocationRepo locationRepo;

    @InjectMocks
    private LocationCommandsProvider provider = new LocationCommandsProvider();

    @Test
    public void collectsSingleCommandFromSingleStatic() throws Exception {
        when(locationRepo.get(LOCATION)).thenReturn(dummyLocation(
                dummyStatic(dummyCommandScriptDto("1"))
        ));
        Player player = dummyPlayer();

        List<Command> commands = provider.provide(player);

        assertThat(commands, hasSize(1));
    }

    @Test
    public void collectsMultipleCommandsFromSingleStatic() throws Exception {
        when(locationRepo.get(LOCATION)).thenReturn(dummyLocation(
                dummyStatic(
                        dummyCommandScriptDto("1"),
                        dummyCommandScriptDto("2"),
                        dummyCommandScriptDto("3")
                )
        ));
        Player player = dummyPlayer();

        List<Command> commands = provider.provide(player);

        assertThat(commands, hasSize(3));
        assertThat(Sets.newHashSet(commands), hasSize(3));
    }

    @Test
    public void collectsSingleCommandInTotalFromManyStatics() throws Exception {
        when(locationRepo.get(LOCATION)).thenReturn(dummyLocation(
                dummyStatic(dummyCommandScriptDto("1")),
                dummyStatic(),
                dummyStatic()
        ));
        Player player = dummyPlayer();

        List<Command> commands = provider.provide(player);

        assertThat(commands, hasSize(1));
    }

    @Test
    public void collectsZeroCommandsInTotalFromManyStatics() throws Exception {
        when(locationRepo.get(LOCATION)).thenReturn(dummyLocation(
                dummyStatic(),
                dummyStatic(),
                dummyStatic()
        ));
        Player player = dummyPlayer();

        List<Command> commands = provider.provide(player);

        assertThat(commands, hasSize(0));
    }

    @Test
    public void collectsZeroCommandsFromEmptyLocation() throws Exception {
        when(locationRepo.get(LOCATION)).thenReturn(dummyLocation());
        Player player = dummyPlayer();

        List<Command> commands = provider.provide(player);

        assertThat(commands, hasSize(0));
    }

    private Player dummyPlayer() {
        return Player.builder().currentLocation(LOCATION).build();
    }

    private Location dummyLocation(Static... statics) {
        return Location.builder()
                .items(LocationItems.builder()
                        .staticItems(Lists.newArrayList(statics))
                        .build())
                .build();
    }

    private Static dummyStatic(ScriptCommandDto... commands) {
        return Static.builder()
                .commands(Lists.newArrayList(commands))
                .build();
    }

    private ScriptCommandDto dummyCommandScriptDto(String id) {
        return ScriptCommandDto.builder()
                .command(id)
                .scriptId(id)
                .build();
    }
}