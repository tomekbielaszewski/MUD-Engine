package org.grizz.game.service.impl;

import org.grizz.game.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by Grizz on 2015-08-18.
 */
@Component
public class EventServiceImpl implements EventService {
    @Autowired
    private Environment env;

    @Override
    public String getEvent(String messageKey, String... params) {
        String unformattedEvent = env.getProperty(messageKey);
        return String.format(unformattedEvent, params);
    }
}
