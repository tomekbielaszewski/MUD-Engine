package org.grizz.game.service;

import org.grizz.game.exception.InsufficientPermissionException;
import org.grizz.game.model.Player;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.script.ScriptRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminRightsServiceTest {
    private final String AUTHORIZATION_CHECK_SCRIPT_ID = "authorization-check";
    private final Player PLAYER = dummyPlayer();
    private final Script AUTHORIZATION_CHECK_SCRIPT = dummyScript(AUTHORIZATION_CHECK_SCRIPT_ID);

    @Mock
    private ScriptRunner scriptRunner;

    @Mock
    private ScriptRepo scriptRepo;

    @InjectMocks
    private AdminRightsService adminRightsService = new AdminRightsService();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldThrowExceptionWhenScriptReturnsFalse() throws Exception {
        when(scriptRepo.get(AUTHORIZATION_CHECK_SCRIPT_ID)).thenReturn(AUTHORIZATION_CHECK_SCRIPT);
        when(scriptRunner.execute(AUTHORIZATION_CHECK_SCRIPT, PLAYER, null, Boolean.class)).thenReturn(Boolean.FALSE);
        exception.expect(InsufficientPermissionException.class);
        exception.expectMessage("player.is.not.an.admin");

        adminRightsService.checkAdminRights(PLAYER);
    }

    @Test
    public void shouldPassWhenScriptReturnsTrue() throws Exception {
        when(scriptRepo.get(AUTHORIZATION_CHECK_SCRIPT_ID)).thenReturn(AUTHORIZATION_CHECK_SCRIPT);
        when(scriptRunner.execute(AUTHORIZATION_CHECK_SCRIPT, PLAYER, null, Boolean.class)).thenReturn(Boolean.TRUE);

        adminRightsService.checkAdminRights(PLAYER);
    }

    private Script dummyScript(String id) {
        return Script.builder().id(id).build();
    }

    private Player dummyPlayer() {
        return Player.builder().build();
    }
}