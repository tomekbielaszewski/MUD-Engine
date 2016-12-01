package org.grizz.game.service.script;

import com.google.common.collect.Lists;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.utils.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import java.io.Reader;
import java.util.Map;

import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScriptRunnerTest {
    private static final String MASTER_SCRIPT_ID = "master-script-id";
    private static final String SCRIPT_ID = "script-id";
    private static final String BINDING_NAME = "binding name";
    private static final Object BINDING_OBJECT = "binding object";

    private static String MASTER_SCRIPT_PATH = "master";
    private static String SCRIPT_PATH = "script";

    @Mock
    private ScriptRepo scriptRepo;
    @Mock
    private ScriptBindingProvider bindingProvider;
    @Mock
    private ScriptEngine engine;
    @Mock
    private FileUtils fileUtils;
    @Mock
    private Reader masterScriptReader;

    @InjectMocks
    private ScriptRunner scriptRunner = new ScriptRunner();

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(scriptRunner, "masterScriptId", MASTER_SCRIPT_ID);
        when(scriptRepo.get(MASTER_SCRIPT_ID)).thenReturn(dummyScript(MASTER_SCRIPT_ID, MASTER_SCRIPT_PATH));
        when(fileUtils.getReader(MASTER_SCRIPT_PATH)).thenReturn(masterScriptReader);
        scriptRunner.init();
    }

    @Test
    public void bindsGivenScriptId() throws Exception {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();
        when(bindingProvider.provide(player, response)).thenReturn(Lists.newArrayList());

        scriptRunner.execute(dummyScript(SCRIPT_ID, SCRIPT_ID), player, response);

        verify(engine).eval(eq(masterScriptReader), (Bindings) (Map) argThat(hasEntry("scriptId", SCRIPT_ID)));
    }

    @Test
    public void bindsGivenAdditionalBindingsToEngine() throws Exception {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();
        when(bindingProvider.provide(player, response)).thenReturn(Lists.newArrayList(dummyScriptBinding()));

        scriptRunner.execute(dummyScript(SCRIPT_ID, SCRIPT_ID), player, response);

        verify(engine).eval(eq(masterScriptReader), (Bindings) argThat(hasEntry(BINDING_NAME, BINDING_OBJECT)));
    }

    @Test
    public void bindsProvidedBindingsToEngine() throws Exception {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();
        when(bindingProvider.provide(player, response)).thenReturn(Lists.newArrayList());

        scriptRunner.execute(dummyScript(SCRIPT_ID, SCRIPT_ID), player, response, Lists.newArrayList(dummyScriptBinding()));

        verify(engine).eval(eq(masterScriptReader), (Bindings) argThat(hasEntry(BINDING_NAME, BINDING_OBJECT)));
    }

    private ScriptBinding dummyScriptBinding() {
        return ScriptBinding.builder().name(BINDING_NAME).object(BINDING_OBJECT).build();
    }

    private static Script dummyScript(String id, String path) {
        return Script.builder().id(id).path(path).build();
    }
}