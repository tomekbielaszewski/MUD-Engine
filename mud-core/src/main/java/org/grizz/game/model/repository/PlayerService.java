package org.grizz.game.model.repository;

import com.google.common.collect.Maps;
import org.grizz.game.exception.PlayerDoesNotExistException;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.Player;
import org.grizz.game.model.Stats;
import org.grizz.game.model.db.dao.*;
import org.grizz.game.model.db.entities.*;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Weapon;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PlayerService implements PlayerRepository {
    private final PlayersDao playersDao;
    private final PlayerStatsDao playerStatsDao;
    private final PlayerParamsDao playerParamsDao;
    private final EquipmentsDao equipmentsDao;
    private final BackpackItemsDao backpackItemsDao;
    private final ItemRepo itemRepo;

    private final Jdbi jdbi;

    @Autowired
    public PlayerService(PlayersDao playersDao, PlayerStatsDao playerStatsDao, PlayerParamsDao playerParamsDao,
                         EquipmentsDao equipmentsDao, BackpackItemsDao backpackItemsDao, ItemRepo itemRepo, Jdbi jdbi) {
        this.playersDao = playersDao;
        this.playerStatsDao = playerStatsDao;
        this.playerParamsDao = playerParamsDao;
        this.equipmentsDao = equipmentsDao;
        this.backpackItemsDao = backpackItemsDao;
        this.itemRepo = itemRepo;
        this.jdbi = jdbi;
    }

    @Override
    public Player findByName(final String name) {
        PlayerEntity playerEntity = playersDao.getByName(name);
        PlayerStatsEntity statsEntity = playerStatsDao.getByName(name);
        List<PlayerParamEntity> paramEntities = playerParamsDao.getByName(name);
        EquipmentEntity equipmentEntity = equipmentsDao.getByName(name);
        List<BackpackItemsEntity> itemsEntities = backpackItemsDao.getByName(name);

        Player player = Player.builder()
                .name(playerEntity.getName())
                .currentLocation(playerEntity.getCurrentLocationId())
                .pastLocation(playerEntity.getPastLocationId())
                .lastActivityTimestamp(playerEntity.getLastActivity().getTime())
                .equipment(fromEntity(equipmentEntity, itemsEntities))
                .stats(fromEntity(statsEntity))
                .parameters(Maps.newHashMap())
                .build();
        paramEntities.forEach(param -> player.addParameter(param.getKey(), param.getValue()));
        return player;
    }

    @Override
    public Player findByNameIgnoreCase(String name) {
        return findByName(name);
    }

    @Override
    public List<Player> findByLastActivityTimestampGreaterThan(long timestamp) {
        return playersDao.findByLastActivityTimestampGreaterThan(timestamp)
                .stream()
                .map(this::findByName)
                .collect(Collectors.toList());
    }

    @Override
    public List<Player> findByCurrentLocation(String id) {
        return playersDao.findByCurrentLocation(id)
                .stream()
                .map(this::findByName)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Player player) {
        jdbi.useTransaction(h -> {
            h.attach(PlayersDao.class).upsert(toPlayerEntity(player));
            h.attach(EquipmentsDao.class).upsert(toEquipmentEntity(player.getEquipment(), player.getName()));
            BackpackItemsDao backpackItemsDao = h.attach(BackpackItemsDao.class);
            backpackItemsDao.removeAll(player.getName());
            backpackItemsDao.insert(toBackpackItemsEntities(player.getEquipment().getBackpack(), player.getName()));
            PlayerParamsDao playerParamsDao = h.attach(PlayerParamsDao.class);
            playerParamsDao.removeAll(player.getName());
            playerParamsDao.insert(toParamsEntity(player.getParameters(), player.getName()));
            h.attach(PlayerStatsDao.class).upsert(toPlayerStatsEntity(player.getStats(), player.getName()));
        });
    }

    @Override
    public void insert(Player player) {
        if (playersDao.checkExistence(player.getName()).isPresent()) {
            throw new PlayerDoesNotExistException("player.already.exist", player.getName());
        }
        update(player); //does upsert operation
    }

    private Stats fromEntity(PlayerStatsEntity statsEntity) {
        return Stats.builder()
                .strength(statsEntity.getStrength())
                .dexterity(statsEntity.getDexterity())
                .charisma(statsEntity.getCharisma())
                .endurance(statsEntity.getEndurance())
                .intelligence(statsEntity.getIntelligence())
                .wisdom(statsEntity.getWisdom())
                .build();
    }

    private Equipment fromEntity(EquipmentEntity equipmentEntity, List<BackpackItemsEntity> itemsEntities) {
        List<Item> backpack = itemsEntities.stream()
                .map(entity -> IntStream.range(0, entity.getAmount())
                        .mapToObj(i -> entity.getItemId())
                        .map(itemRepo::get)
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return Equipment.builder()
                .headItem(getNullableArmor(equipmentEntity.getHead()))
                .torsoItem(getNullableArmor(equipmentEntity.getTorso()))
                .handsItem(getNullableArmor(equipmentEntity.getHands()))
                .legsItem(getNullableArmor(equipmentEntity.getLegs()))
                .feetItem(getNullableArmor(equipmentEntity.getFeet()))
                .meleeWeapon(getNullableWeapon(equipmentEntity.getMeleeWeapon()))
                .rangeWeapon(getNullableWeapon(equipmentEntity.getRangedWeapon()))
                .backpack(backpack)
                .build();
    }

    private Armor getNullableArmor(String id) {
        return getNullableItem(id, Armor.class);
    }

    private Weapon getNullableWeapon(String id) {
        return getNullableItem(id, Weapon.class);
    }

    private <T> T getNullableItem(String id, Class<T> clazz) {
        return Optional.ofNullable(id)
                .map(itemRepo::get)
                .map(clazz::cast)
                .orElse(null);
    }

    private PlayerStatsEntity toPlayerStatsEntity(Stats stats, String name) {
        return PlayerStatsEntity.builder()
                .playerName(name)
                .strength(stats.getStrength())
                .dexterity(stats.getDexterity())
                .charisma(stats.getCharisma())
                .endurance(stats.getEndurance())
                .intelligence(stats.getIntelligence())
                .wisdom(stats.getWisdom())
                .build();
    }

    private List<PlayerParamEntity> toParamsEntity(Map<String, Object> parameters, String name) {
        return Optional.ofNullable(parameters)
                .orElse(Maps.newHashMap())
                .entrySet()
                .stream()
                .map(entry -> PlayerParamEntity.builder()
                        .playerName(name)
                        .key(entry.getKey())
                        .value(String.valueOf(entry.getValue()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<BackpackItemsEntity> toBackpackItemsEntities(List<Item> backpack, String name) {
        return backpack.stream()
                .collect(Collectors.groupingBy(Item::getId))
                .entrySet()
                .stream()
                .map(entry -> BackpackItemsEntity.builder()
                        .playerName(name)
                        .itemId(entry.getKey())
                        .itemName(entry.getValue().get(0).getName())
                        .amount(entry.getValue().size())
                        .build())
                .collect(Collectors.toList());
    }

    private EquipmentEntity toEquipmentEntity(Equipment equipment, String name) {
        return EquipmentEntity.builder()
                .playerName(name)
                .head(Optional.ofNullable(equipment).map(Equipment::getHeadItem).map(Item::getId).orElse(null))
                .torso(Optional.ofNullable(equipment).map(Equipment::getTorsoItem).map(Item::getId).orElse(null))
                .hands(Optional.ofNullable(equipment).map(Equipment::getHandsItem).map(Item::getId).orElse(null))
                .legs(Optional.ofNullable(equipment).map(Equipment::getLegsItem).map(Item::getId).orElse(null))
                .feet(Optional.ofNullable(equipment).map(Equipment::getFeetItem).map(Item::getId).orElse(null))
                .meleeWeapon(Optional.ofNullable(equipment).map(Equipment::getMeleeWeapon).map(Item::getId).orElse(null))
                .rangedWeapon(Optional.ofNullable(equipment).map(Equipment::getRangeWeapon).map(Item::getId).orElse(null))
                .build();
    }

    private PlayerEntity toPlayerEntity(Player player) {
        return PlayerEntity.builder()
                .name(player.getName())
                .currentLocationId(player.getCurrentLocation())
                .pastLocationId(player.getPastLocation())
                .lastActivity(Timestamp.from(Instant.ofEpochMilli(player.getLastActivityTimestamp())))
                .build();
    }
}
