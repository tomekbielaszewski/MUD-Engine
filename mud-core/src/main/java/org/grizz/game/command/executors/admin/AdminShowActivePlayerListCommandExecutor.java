package org.grizz.game.command.executors.admin;

import com.google.common.collect.Lists;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.AdminRightsService;
import org.grizz.game.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminShowActivePlayerListCommandExecutor {
    private static final int MINUTES = 1000 * 60;

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private AdminRightsService adminRightsService;

    public void showPlayerList(long lastMinutes, Player admin, PlayerResponse response) {
        adminRightsService.checkAdminRights(admin);

        long timestampSomeMinutesAgo = System.currentTimeMillis() - (lastMinutes * MINUTES);
        List<Player> activePlayers = playerRepository.findByLastActivityTimestampGreaterThan(timestampSomeMinutesAgo);

        printPlayerList(activePlayers, response);
    }

    private void printPlayerList(List<Player> activePlayers, PlayerResponse response) {
        String playerListTitle = eventService.getEvent("admin.command.player.list.title");
        List<String> playerList = Lists.newArrayList();

        activePlayers.forEach(player -> {
            String playerName = player.getName();
            String playerLocationId = player.getCurrentLocation();
            String playerLocation = locationRepo.get(playerLocationId).getName();
            String playerActivityMinutesAgo = calcMinutesAgo(player.getLastActivityTimestamp()).toString();
            String playerDescription = eventService.getEvent("admin.command.player.list.row", playerName, playerActivityMinutesAgo, playerLocation, playerLocationId);
            playerList.add(playerDescription);
        });

        response.getPlayerEvents().add(playerListTitle);
        response.getPlayerEvents().addAll(playerList);
    }

    private Long calcMinutesAgo(long lastActivityTimestamp) {
        return (System.currentTimeMillis() - lastActivityTimestamp) / MINUTES;
    }
}
