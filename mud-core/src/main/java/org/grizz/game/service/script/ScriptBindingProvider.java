package org.grizz.game.service.script;

import com.google.common.collect.Lists;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ScriptBindingProvider {
    private static final String RESPONSE = "response";
    private static final String PLAYER = "player";
    private static final List<ScriptBinding> SYSTEM_BINDINGS = Lists.newArrayList();

    @Autowired
    private SystemScriptBindingProvider systemScriptBindingsProvider;

    @PostConstruct
    public void initSystemBindings() {
        SYSTEM_BINDINGS.addAll(systemScriptBindingsProvider.provide());
    }

    public List<ScriptBinding> provide(Player player, PlayerResponse response) {
        List<ScriptBinding> bindings = Lists.newArrayList();
        bindings.add(ScriptBinding.builder().name(PLAYER).object(player).build());
        bindings.add(ScriptBinding.builder().name(RESPONSE).object(response).build());
        bindings.addAll(SYSTEM_BINDINGS);

        return bindings;
    }
}
