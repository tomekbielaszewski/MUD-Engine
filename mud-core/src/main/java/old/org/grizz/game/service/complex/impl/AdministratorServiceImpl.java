package old.org.grizz.game.service.complex.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import old.org.grizz.game.exception.CantGiveStaticItemException;
import old.org.grizz.game.exception.InvalidAmountException;
import old.org.grizz.game.model.Location;
import old.org.grizz.game.model.LocationItems;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.model.enums.ItemType;
import old.org.grizz.game.model.impl.PlayerContextImpl;
import old.org.grizz.game.model.impl.PlayerResponseImpl;
import old.org.grizz.game.model.items.Item;
import old.org.grizz.game.model.repository.ItemRepo;
import old.org.grizz.game.model.repository.LocationRepo;
import old.org.grizz.game.model.repository.PlayerRepository;
import old.org.grizz.game.service.complex.AdministratorService;
import old.org.grizz.game.service.complex.MovementService;
import old.org.grizz.game.service.complex.MultiplayerNotificationService;
import old.org.grizz.game.service.complex.ScriptRunnerService;
import old.org.grizz.game.service.simple.EquipmentService;
import old.org.grizz.game.service.simple.EventService;
import old.org.grizz.game.service.simple.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Grizz on 2015-11-30.
 */
@Slf4j
@Service
public class AdministratorServiceImpl implements AdministratorService {
    private static final String AUTHORIZATION_CHECK_SCRIPT_ID = "authorization-check";

    @Autowired
    private ScriptRunnerService scriptRunnerService;

    @Autowired
    private MovementService movementService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepo locationRepo;

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private EventService eventService;

    @Autowired
    private MultiplayerNotificationService multiplayerNotificationService;

    @Override
    public void teleport(String player, String targetLocationId, PlayerContext admin, PlayerResponse adminResponse) {
        if(isAuthorized(admin, adminResponse)) {
            Location targetLocation = locationRepo.get(targetLocationId);

            PlayerContext teleportedPlayer;
            PlayerResponse teleportedPlayerResponse;
            if (admin.getName().equalsIgnoreCase(player)) {
                teleportedPlayer = admin;
                teleportedPlayerResponse = adminResponse;
            } else {
                teleportedPlayer = playerRepo.findByNameIgnoreCase(player);
                teleportedPlayerResponse = new PlayerResponseImpl();
            }

            if (teleportedPlayer == null) {
                String event = eventService.getEvent("admin.command.player.not.found", player);
                adminResponse.getPlayerEvents().add(event);
            } else {
                log.info("Teleporting [{}] to [{}]", player, targetLocationId);
                teleport((PlayerContextImpl) teleportedPlayer, teleportedPlayerResponse, targetLocation, admin, adminResponse);
            }

        } else {
            log.warn("{} is NOT authorized!", admin.getName());
        }
    }

    private void teleport(PlayerContextImpl teleportedPlayer, PlayerResponse teleportedPlayerResponse, Location targetLocation, PlayerContext admin, PlayerResponse adminResponse) {
        movementService.teleport(targetLocation.getId(), teleportedPlayer, teleportedPlayerResponse);
        teleportNotifications(teleportedPlayer, teleportedPlayerResponse, targetLocation, admin, adminResponse);

        if (!teleportedPlayer.getName().equalsIgnoreCase(admin.getName())) {
            playerRepo.save(teleportedPlayer);
            multiplayerNotificationService.send(teleportedPlayer, teleportedPlayerResponse);
        }
    }

    private void teleportNotifications(PlayerContextImpl teleportedPlayer, PlayerResponse teleportedPlayerResponse, Location targetLocation, PlayerContext admin, PlayerResponse adminResponse) {
        String teleportedPlayerNotification = eventService.getEvent("admin.command.player.teleportation.notification", admin.getName(), targetLocation.getName());
        String playersOnSourceLocationEvent = eventService.getEvent("admin.command.player.teleportation.notification.broadcast.out", teleportedPlayer.getName());
        String playersOnTargetLocationEvent = eventService.getEvent("admin.command.player.teleportation.notification.broadcast.in", teleportedPlayer.getName());
        String adminNotification = eventService.getEvent("admin.command.success.teleportation.notification", teleportedPlayer.getName(), targetLocation.getName());

        teleportedPlayerResponse.getPlayerEvents().add(teleportedPlayerNotification);
        adminResponse.getPlayerEvents().add(adminNotification);
        multiplayerNotificationService.broadcast(locationRepo.get(teleportedPlayer.getCurrentLocation()), playersOnTargetLocationEvent, teleportedPlayer);
        multiplayerNotificationService.broadcast(locationRepo.get(teleportedPlayer.getPastLocation()), playersOnSourceLocationEvent, teleportedPlayer);
    }

    @Override
    public void give(String player, String itemName, int amount, PlayerContext admin, PlayerResponse adminResponse) {
        if(isAuthorized(admin, adminResponse)) {
            if (amount < 1) {
                throw new InvalidAmountException("admin.command.cant.give.none.items");
            }

            Item item = itemRepo.getByName(itemName);
            if (ItemType.STATIC.equals(item.getItemType())) {
                throw new CantGiveStaticItemException("admin.command.cant.give.static.item");
            }

            PlayerContext receiver;
            PlayerResponse receiverResponse;
            if (admin.getName().equalsIgnoreCase(player)) {
                receiver = admin;
                receiverResponse = adminResponse;
            } else {
                receiver = playerRepo.findByNameIgnoreCase(player);
                receiverResponse = new PlayerResponseImpl();
            }

            if (receiver == null) {
                String event = eventService.getEvent("admin.command.player.not.found", player);
                adminResponse.getPlayerEvents().add(event);
            } else {
                log.info("{} gave [{}]x{} to [{}]", admin.getName(), itemName, amount, player);
                give((PlayerContextImpl) receiver, (PlayerResponseImpl) receiverResponse, amount, item, admin, adminResponse);
            }
        }
    }

    private void give(PlayerContextImpl receiver, PlayerResponseImpl receiverResponse, int amount, Item item, PlayerContext admin, PlayerResponse adminResponse) {
        List<Item> items = Lists.newArrayList();
        for (int i = 0; i < amount; i++) {
            items.add(item);
        }

        equipmentService.addItems(items, receiver, receiverResponse);
        giveNotifications(receiver, receiverResponse, admin, adminResponse);

        if (!receiver.getName().equalsIgnoreCase(admin.getName())) {
            playerRepo.save(receiver);
        }
    }

    private void giveNotifications(PlayerContextImpl receiver, PlayerResponseImpl receiverResponse, PlayerContext admin, PlayerResponse adminResponse) {
        if (!receiver.getName().equalsIgnoreCase(admin.getName())) {
            String itemsReceivedEvent = eventService.getEvent("admin.command.items.received.from", admin.getName());
            String itemsGivenEvent = eventService.getEvent("admin.command.items.given.to", receiver.getName());

            receiverResponse.getPlayerEvents().add(itemsReceivedEvent);
            adminResponse.getPlayerEvents().add(itemsGivenEvent);

            multiplayerNotificationService.send(receiver, receiverResponse);
        }
    }

    @Override
    public void put(String itemName, int amount, PlayerContext admin, PlayerResponse adminResponse) {
        if(isAuthorized(admin, adminResponse)) {
            if (amount < 1) {
                throw new InvalidAmountException("admin.command.cant.put.none.items");
            }

            Item item = itemRepo.getByName(itemName);

            Location targetLocation = locationRepo.get(admin.getCurrentLocation());

            log.info("{} put [{}]x{} on [{}]", admin.getName(), itemName, amount, targetLocation.getName());
            put(item, amount, targetLocation, admin, adminResponse);
        }
    }

    private void put(Item item, int amount, Location targetLocation, PlayerContext admin, PlayerResponse adminResponse) {
        List<Item> items = Lists.newArrayList();
        for (int i = 0; i < amount; i++) {
            items.add(item);
        }

        if (ItemType.STATIC.equals(item.getItemType())) {
            LocationItems locationItems = targetLocation.getItems();
            locationItems.getStaticItems().addAll(items);
            locationService.saveLocationState(targetLocation);
        } else {
            locationService.addItems(targetLocation, items);
        }

        putNotifications(item.getName(), amount, targetLocation, admin, adminResponse);
    }

    private void putNotifications(String itemName, int amount, Location targetLocation, PlayerContext admin, PlayerResponse adminResponse) {
        String itemPutLocationNotification = eventService.getEvent("admin.command.item.put.notification.broadcast");
        String adminNotification = eventService.getEvent("admin.command.item.put.notification", String.valueOf(amount), itemName);

        multiplayerNotificationService.broadcast(targetLocation, itemPutLocationNotification, admin);
        adminResponse.getPlayerEvents().add(adminNotification);
    }

    @Override
    public void showPlayerList(PlayerContext admin, PlayerResponse adminResponse) {
        if (isAuthorized(admin, adminResponse)) {
            List<PlayerContextImpl> allPlayers = playerRepo.findAll();

            String playerListTitle = eventService.getEvent("admin.command.player.list.title");
            List<String> playerList = Lists.newArrayList();

            for (PlayerContextImpl player : allPlayers) {
                String playerName = player.getName();
                String playerLocationId = player.getCurrentLocation();
                String playerLocation = locationRepo.get(playerLocationId).getName();
                String playerDescription = eventService.getEvent("admin.command.player.list.row", playerName, playerLocation, playerLocationId);
                playerList.add(playerDescription);
            }

            adminResponse.getPlayerEvents().add(playerListTitle);
            adminResponse.getPlayerEvents().addAll(playerList);
        }
    }

    private boolean isAuthorized(PlayerContext player, PlayerResponse response) {
        boolean isAuthorized = (boolean) scriptRunnerService.execute(AUTHORIZATION_CHECK_SCRIPT_ID, player, response);
        if(!isAuthorized) {
            log.warn("Player {} tried to execute secured command!", player.getName());
        }
        return isAuthorized;
    }
}
