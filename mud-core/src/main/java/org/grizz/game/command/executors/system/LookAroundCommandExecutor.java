package org.grizz.game.command.executors.system;

import org.apache.commons.lang3.StringUtils;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.script.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LookAroundCommandExecutor {
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ScriptRunner scriptRunner;
    @Autowired
    private ScriptRepo scriptRepo;

    public void lookAround(Player player, PlayerResponse response) {
        notifyAboutCurrentLocation(player, response);
        notifyAboutPlayersOnLocation(player, response);
    }

    private void notifyAboutCurrentLocation(Player player, PlayerResponse response) {
        Location location = locationRepo.get(player.getCurrentLocation());
        response.setCurrentLocation(location);
        runOnShowScript(location, player, response);
    }

    private void runOnShowScript(Location location, Player player, PlayerResponse response) {
        if (StringUtils.isEmpty(location.getOnShowScript())) return;
        Script script = scriptRepo.get(location.getOnShowScript());
        scriptRunner.execute(script, player, response);
    }

    private void notifyAboutPlayersOnLocation(Player player, PlayerResponse response) {
        List<Player> otherPlayersOnLocation = playerRepository.findByCurrentLocation(player.getCurrentLocation());
        List<String> playersNames = otherPlayersOnLocation.stream()
                .map(p -> p.getName())
                .filter(p -> !p.equals(player.getName()))
                .collect(Collectors.toList());
        response.setPlayers(playersNames);
    }
}
