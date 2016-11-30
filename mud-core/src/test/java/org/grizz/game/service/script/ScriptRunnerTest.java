package org.grizz.game.service.script;

import com.google.common.collect.Lists;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.utils.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.script.ScriptEngine;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScriptRunnerTest {
    private static final String MASTER_SCRIPT_ID = "master-script-id";
    private static final String SCRIPT_ID = "script-id";

    private static String MASTER_SCRIPT_PATH = "master";
    private static String SCRIPT_PATH = "script";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Mock
    private ScriptRepo scriptRepo;
    @Mock
    private ScriptBindingProvider bindingProvider;
    @Mock
    private ScriptEngine engine;
    @Mock
    private FileUtils fileUtils;

    @InjectMocks
    private ScriptRunner scriptRunner = new ScriptRunner();

    @Before
    public void setUp() throws Exception {
        MASTER_SCRIPT_PATH = folder.newFile(MASTER_SCRIPT_PATH).getAbsolutePath();
        SCRIPT_PATH = folder.newFile(SCRIPT_PATH).getAbsolutePath();

        ReflectionTestUtils.setField(scriptRunner, "masterScriptId", MASTER_SCRIPT_ID);
        when(scriptRepo.get(MASTER_SCRIPT_ID)).thenReturn(dummyScript(MASTER_SCRIPT_ID, MASTER_SCRIPT_PATH));
        scriptRunner.init();
    }

    @After
    public void tearDown() {
        folder.delete();
    }

    @Test
    public void bindsGivenScriptId() throws Exception {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();
        when(bindingProvider.provide(player, response)).thenReturn(Lists.newArrayList());

        scriptRunner.execute(dummyScript(SCRIPT_ID, SCRIPT_ID), player, response);
    }

    @Test
    public void bindsGivenAdditionalBindingsToEngine() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void bindsProvidedBindingsToEngine() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void evalsMasterScript() throws Exception {
        throw new NotImplementedException();
    }

    private static Script dummyScript(String id, String path) {
        return Script.builder().id(id).path(path).build();
    }
}