package org.grizz.game.command.executors.admin;

import org.grizz.game.exception.CantGiveStaticItemException;
import org.grizz.game.exception.CantOwnStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.exception.PlayerDoesNotExistException;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.AdminRightsService;
import org.grizz.game.service.EquipmentService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.notifier.MultiplayerNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminGiveItemCommandExecutor {
    @Autowired
    private AdminRightsService adminRightsService;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private EventService eventService;
    @Autowired
    private MultiplayerNotificationService notificationService;

    public void give(String playerName, String itemName, int amount, Player admin, PlayerResponse response) {
        adminRightsService.checkAdminRights(admin);
        validateAmount(amount);

        if (isSelfGiving(playerName, admin)) {
            give(itemName, amount, admin, response);
        } else {
            Player player = getPlayer(playerName);
            PlayerResponse playerResponse = new PlayerResponse();

            give(itemName, amount, player, playerResponse);

            savePlayer(player);
            notifyPlayer(player, playerResponse, admin);
            notifyAdmin(itemName, amount, player, response);
        }
    }

    private void savePlayer(Player player) {
        playerRepository.save(player);
    }

    private void validateAmount(int amount) {
        if (amount <= 0) throw new InvalidAmountException("admin.command.cant.give.none.items");
    }

    private void notifyAdmin(String itemName, Integer amount, Player player, PlayerResponse adminResponse) {
        String event = eventService.getEvent("admin.command.items.given.to", player.getName(), itemName, amount.toString());
        adminResponse.getPlayerEvents().add(event);
    }

    private void notifyPlayer(Player player, PlayerResponse playerResponse, Player admin) {
        String event = eventService.getEvent("admin.command.items.received.from", admin.getName());
        playerResponse.getPlayerEvents().add(event);
        notificationService.send(player, playerResponse);
    }

    private Player getPlayer(String playerName) {
        Player player = playerRepository.findByNameIgnoreCase(playerName);
        if (player == null) {
            throw new PlayerDoesNotExistException("admin.command.player.not.found", playerName);
        }
        return player;
    }

    private void give(String itemName, int amount, Player player, PlayerResponse response) {
        try {
            equipmentService.addItems(itemName, amount, player, response);
        } catch (CantOwnStaticItemException e) {
            throw new CantGiveStaticItemException("cant.give.static.item");
        }
    }

    private boolean isSelfGiving(String playerName, Player admin) {
        return playerName.equalsIgnoreCase(admin.getName());
    }
}
