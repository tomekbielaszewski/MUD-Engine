package org.grizz.game.service.simple.impl;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.service.simple.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by Grizz on 2015-08-18.
 */
@Slf4j
@Component
public class EventServiceImpl implements EventService {
    @Autowired
    private Environment env;

    @Override
    public String getEvent(String messageKey, String... params) {
        String unformattedEvent = env.getProperty(messageKey);
        if (unformattedEvent == null) {
            log.error("Key [{}] message missing! Add it to strings.properties!");
        }
        return String.format(unformattedEvent, params);
    }
}
