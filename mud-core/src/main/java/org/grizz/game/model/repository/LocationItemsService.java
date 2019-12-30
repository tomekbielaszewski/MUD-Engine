package org.grizz.game.model.repository;

import org.grizz.game.model.LocationItems;
import org.grizz.game.model.db.dao.LocationItemsDao;
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
    private final LocationItemsDao locationItemsDao;
    private final ItemRepo itemRepo;

    @Autowired
    public LocationItemsService(LocationItemsDao locationItemsDao, ItemRepo itemRepo) {
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
    public LocationItems upsert(LocationItems items) {
        List<LocationItemsEntity> itemsEntities = toEntities(items);
        locationItemsDao.removeAll(items.getLocationId()); //in case of depleting some item stack
        locationItemsDao.insert(itemsEntities);
        return items;
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
}
