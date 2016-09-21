package org.grizz.game.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventService {
    @Autowired
    private Environment env;

    public String getEvent(String messageKey, String... params) {
        String unformattedEvent = env.getProperty(messageKey);
        if (unformattedEvent == null) {
            log.error("Key [{}] message missing! Add it to strings.properties!", messageKey);
            return messageKey;
        }
        return String.format(unformattedEvent, params);
    }
}
