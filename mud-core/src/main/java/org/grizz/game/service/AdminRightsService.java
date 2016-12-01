package org.grizz.game.service;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.exception.InsufficientPermissionException;
import org.grizz.game.model.Player;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.script.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminRightsService {
    private static final String AUTHORIZATION_CHECK_SCRIPT_ID = "authorization-check";

    @Autowired
    private ScriptRunner scriptRunner;
    @Autowired
    private ScriptRepo scriptRepo;

    public void checkAdminRights(Player player) {
        Script authorizationCheckScript = scriptRepo.get(AUTHORIZATION_CHECK_SCRIPT_ID);
        boolean isAdmin = scriptRunner.execute(authorizationCheckScript, player, null, Boolean.class);

        if(!isAdmin) {
            log.warn("Player {} tried to execute secured command!", player.getName());
            throw new InsufficientPermissionException("player.is.not.an.admin");
        }
    }
}
