package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.grizz.game.model.Item;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.ItemStack;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.ResponseExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
@Service
public class ResponseExtractorImpl implements ResponseExtractor {
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public PlayerResponse extract(PlayerContext context) {
        PlayerResponseImpl response = new PlayerResponseImpl();

        extractLocationData(context, response);
        extractPlayerData(context, response);

        return response;
    }

    private void extractLocationData(PlayerContext context, PlayerResponseImpl response) {
        String currentLocationID = context.getCurrentLocation();
        Location location = locationRepo.get(currentLocationID);
        List<String> possibleExits = Lists.newArrayList();

        response.setLocationName(location.getName());
        response.setLocationDescription(location.getDescription());

        if (!StringUtils.isEmpty(location.getNorth())) possibleExits.add("north");
        if (!StringUtils.isEmpty(location.getSouth())) possibleExits.add("south");
        if (!StringUtils.isEmpty(location.getEast())) possibleExits.add("east");
        if (!StringUtils.isEmpty(location.getWest())) possibleExits.add("west");
        if (!StringUtils.isEmpty(location.getUp())) possibleExits.add("up");
        if (!StringUtils.isEmpty(location.getDown())) possibleExits.add("down");

        response.setLocationItems(Lists.newArrayList());

        for (ItemStack item : location.getItems()) {
            response.getLocationItems().add(getItemData(item));
        }

        response.setPossibleExits(possibleExits);
    }

    private String getItemData(ItemStack itemStack) {
        Item item = itemRepo.get(itemStack.getItemId());
        return String.format("%s w ilości %d sztuk", item.getName(), itemStack.getQuantity());
    }

    private void extractPlayerData(PlayerContext context, PlayerResponseImpl response) {
        response.setPlayerEvents(context.getEvents());
    }
}
