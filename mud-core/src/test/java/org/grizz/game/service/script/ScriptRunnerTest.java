package org.grizz.game.service.script;

import org.grizz.game.model.Script;
import org.grizz.game.model.repository.ScriptRepo;
import org.junit.Before;
import org.junit.Test;
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
    private static final Script MASTER_SCRIPT = Script.builder().id(MASTER_SCRIPT_ID).build();
    @Mock
    private ScriptRepo scriptRepo;
    @Mock
    private ScriptBindingProvider bindingProvider;
    @Mock
    private ScriptEngine engine;

    @InjectMocks
    private ScriptRunner scriptRunner = new ScriptRunner();

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(scriptRunner, "masterScriptId", MASTER_SCRIPT_ID);
        when(scriptRepo.get(MASTER_SCRIPT_ID)).thenReturn(MASTER_SCRIPT);
    }

    @Test
    public void execute() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void execute1() throws Exception {
        throw new NotImplementedException();
    }
}