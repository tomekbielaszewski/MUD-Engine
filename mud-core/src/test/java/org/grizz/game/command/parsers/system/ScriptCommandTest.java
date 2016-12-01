package org.grizz.game.command.parsers.system;

import com.google.common.collect.Lists;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.items.ScriptCommandDto;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.script.ScriptBinding;
import org.grizz.game.service.script.ScriptRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScriptCommandTest {
    private static final String SCRIPT_ID = "id";
    private static final String STRING_VAR_NAME = "stringVar";
    private static final String INT_VAR_NAME = "intVar";
    private static final String REGEXP_PATTERN = "regexp (?<" + STRING_VAR_NAME + ">[\\D]+) and (?<" + INT_VAR_NAME + ">[\\d]+) pattern";
    private static final String STRING_VAR_VALUE = "text";
    private static final String INT_VAR_VALUE = "10";
    private static final String COMMAND = "regexp " + STRING_VAR_VALUE + " and " + INT_VAR_VALUE + " pattern";
    private static final Script SCRIPT = Script.builder().build();
    private static final Player PLAYER = Player.builder().build();
    private static final PlayerResponse RESPONSE = new PlayerResponse();


    @Mock
    private ScriptCommandDto scriptCommandDto;
    @Mock
    private ScriptRepo scriptRepo;
    @Mock
    private ScriptRunner scriptRunner;
    @Mock
    private Environment environment;

    @InjectMocks
    private ScriptCommand scriptCommand = new ScriptCommand(scriptCommandDto, scriptRepo, scriptRunner, environment);

    @Test
    public void acceptsCommandBasingOnExplicitPatternInScriptCommandDto() throws Exception {
        String pattern = "explicit pattern";
        String command = "explicit pattern";
        when(scriptCommandDto.getCommand()).thenReturn(pattern);

        boolean result = scriptCommand.accept(command);

        assertTrue(result);
    }

    @Test
    public void acceptsCommandBasingOnRegExpPatternInScriptCommandDto() throws Exception {
        when(scriptCommandDto.getCommand()).thenReturn(REGEXP_PATTERN);

        boolean result = scriptCommand.accept(COMMAND);

        assertTrue(result);
    }

    @Test
    public void doesNotAcceptCommand() throws Exception {
        String command = "regexp text and no int pattern";
        when(scriptCommandDto.getCommand()).thenReturn(REGEXP_PATTERN);

        boolean result = scriptCommand.accept(command);

        assertFalse(result);
    }

    @Test
    public void bindsCommandVariables() throws Exception {
        when(scriptCommandDto.getCommand()).thenReturn(REGEXP_PATTERN);
        when(scriptCommandDto.getScriptId()).thenReturn(SCRIPT_ID);
        when(scriptRepo.get(SCRIPT_ID)).thenReturn(SCRIPT);
        ScriptBinding stringVarBinding = ScriptBinding.builder().name(STRING_VAR_NAME).object(STRING_VAR_VALUE).build();
        ScriptBinding intVarBinding = ScriptBinding.builder().name(INT_VAR_NAME).object(INT_VAR_VALUE).build();

        scriptCommand.execute(COMMAND, PLAYER, RESPONSE);

        verify(scriptRunner).execute(eq(SCRIPT), eq(PLAYER), eq(RESPONSE), (List<ScriptBinding>) argThat(containsInAnyOrder(intVarBinding, stringVarBinding)));
    }

    @Test
    public void additionalBindingsIsEmptyWhenNoVariablesInCommand() throws Exception {
        String pattern = "explicit pattern";
        String command = "explicit pattern";
        when(scriptCommandDto.getCommand()).thenReturn(pattern);
        when(scriptCommandDto.getScriptId()).thenReturn(SCRIPT_ID);
        when(scriptRepo.get(SCRIPT_ID)).thenReturn(SCRIPT);

        scriptCommand.execute(command, PLAYER, RESPONSE);

        verify(scriptRunner).execute(SCRIPT, PLAYER, RESPONSE, Lists.newArrayList());
    }
}