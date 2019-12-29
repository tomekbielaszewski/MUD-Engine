package org.grizz.game.model.repository;

import org.grizz.game.exception.LocationAlreadyExistException;
import org.grizz.game.exception.NoSuchLocationException;
import org.grizz.game.model.Location;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.db.dao.LocationItemsDao;
import org.grizz.game.model.db.dao.LocationsDao;
import org.grizz.game.model.db.entities.LocationEntity;
import org.grizz.game.model.db.entities.LocationItemsEntity;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class LocationItemsService implements LocationItemsRepository {
    private final LocationsDao locationsDao;
    private final LocationItemsDao locationItemsDao;
    private final ItemRepo itemRepo;

    @Autowired
    public LocationItemsService(LocationsDao locationsDao, LocationItemsDao locationItemsDao, ItemRepo itemRepo) {
        this.locationsDao = locationsDao;
        this.locationItemsDao = locationItemsDao;
        this.itemRepo = itemRepo;
    }

    @Override
    public LocationItems findByLocationId(String locationId) {
        List<LocationItemsEntity> itemsEntities = locationItemsDao.getByLocationId(locationId);
        List<Item> items = toItems(itemsEntities);
        List<Item> mobileItems = getMobileItems(items);
        List<Item> staticItems = getStaticItems(items);
        return LocationItems.builder()
                .locationId(locationId)
                .mobileItems(mobileItems)
                .staticItems(staticItems)
                .build();
    }

    @Override
    public void update(LocationItems items) {
        if (!locationsDao.checkExistence(items.getLocationId()).isPresent()) {
            throw new NoSuchLocationException("no.such.location", items.getLocationId());
        }
        List<LocationItemsEntity> itemsEntities = toEntities(items);
        locationItemsDao.removeAll(items.getLocationId()); //in case of depleting some item stack
        locationItemsDao.insert(itemsEntities);
    }

    @Override
    public LocationItems save(Location location) {
        if (locationsDao.checkExistence(location.getId()).isPresent()) {
            throw new LocationAlreadyExistException("location.already.exist", location.getId());
        }
        LocationEntity locationEntity = toEntity(location);
        locationsDao.insert(locationEntity);
        List<LocationItemsEntity> itemsEntities = toEntities(location.getItems());
        locationItemsDao.insert(itemsEntities);
        return location.getItems();
    }

    private List<Item> toItems(List<LocationItemsEntity> itemsEntities) {
        return itemsEntities.stream()
                .map(e -> IntStream.range(0, e.getAmount())
                        .mapToObj(i -> itemRepo.get(e.getItemId()))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Item> getStaticItems(List<Item> itemsEntities) {
        return itemsEntities.stream()
                .filter(i -> ItemType.STATIC.equals(i.getItemType()))
                .collect(Collectors.toList());
    }

    private List<Item> getMobileItems(List<Item> itemsEntities) {
        return itemsEntities.stream()
                .filter(i -> !ItemType.STATIC.equals(i.getItemType()))
                .collect(Collectors.toList());
    }

    private List<LocationItemsEntity> toEntities(LocationItems items) {
        return Stream.concat(
                items.getMobileItems().stream(),
                items.getStaticItems().stream()
        )
                .collect(Collectors.groupingBy(Item::getId))
                .entrySet()
                .stream()
                .map(entry -> LocationItemsEntity.builder()
                        .locationId(items.getLocationId())
                        .itemId(entry.getKey())
                        .itemName(entry.getValue().get(0).getName()) //I'm grouping by id so all lists are non zero
                        .amount(entry.getValue().size())
                        .build())
                .collect(Collectors.toList());
    }

    private LocationEntity toEntity(Location location) {
        return LocationEntity.builder()
                .locationId(location.getId())
                .name(location.getName())
                .build();
    }
}
