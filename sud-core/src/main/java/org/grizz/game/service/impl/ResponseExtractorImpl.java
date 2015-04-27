package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
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

        response.setPossibleExits(possibleExits);
    }

    private void extractPlayerData(PlayerContext context, PlayerResponseImpl response) {
        response.setPlayerEvents(context.getEvents());
    }
}
