package org.grizz.game.model.repository;

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
                .build();
        paramEntities.forEach(param -> player.addParameter(param.getKey(), param.getValue()));
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
            h.attach(BackpackItemsDao.class).upsert(toBackpackItemsEntities(player.getEquipment().getBackpack(), player.getName()));
            h.attach(PlayerParamsDao.class).upsert(toParamsEntity(player.getParameters(), player.getName()));
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
                .headItem((Armor) itemRepo.get(equipmentEntity.getHead()))
                .torsoItem((Armor) itemRepo.get(equipmentEntity.getTorso()))
                .handsItem((Armor) itemRepo.get(equipmentEntity.getHands()))
                .legsItem((Armor) itemRepo.get(equipmentEntity.getLegs()))
                .feetItem((Armor) itemRepo.get(equipmentEntity.getFeet()))
                .meleeWeapon((Weapon) itemRepo.get(equipmentEntity.getMeleeWeapon()))
                .rangeWeapon((Weapon) itemRepo.get(equipmentEntity.getRangedWeapon()))
                .backpack(backpack)
                .build();
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
        return parameters.entrySet()
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
                .head(equipment.getHeadItem().getId())
                .torso(equipment.getTorsoItem().getId())
                .hands(equipment.getHandsItem().getId())
                .legs(equipment.getLegsItem().getId())
                .feet(equipment.getFeetItem().getId())
                .meleeWeapon(equipment.getMeleeWeapon().getId())
                .rangedWeapon(equipment.getRangeWeapon().getId())
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
