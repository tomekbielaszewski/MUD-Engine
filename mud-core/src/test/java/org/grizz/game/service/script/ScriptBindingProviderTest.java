package org.grizz.game.service.script;

import com.google.common.collect.Lists;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScriptBindingProviderTest {
    private static final ScriptBinding FIRST_BINDING = ScriptBinding.builder().name("1").build();
    private static final ScriptBinding SECOND_BINDING = ScriptBinding.builder().name("2").build();

    @Mock
    private SystemScriptBindingProvider systemScriptBindingProvider;

    @InjectMocks
    private ScriptBindingProvider provider = new ScriptBindingProvider();

    @Before
    public void setUp() throws Exception {
        when(systemScriptBindingProvider.provide()).thenReturn(Lists.newArrayList(FIRST_BINDING, SECOND_BINDING));
        this.provider.initSystemBindings();
    }

    @Test
    public void provide() throws Exception {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();

        List<ScriptBinding> result = provider.provide(player, response);

        assertThat(result, hasSize(4));
        assertThat(result, hasItems(FIRST_BINDING, SECOND_BINDING));
        verify(systemScriptBindingProvider).provide();
    }
}