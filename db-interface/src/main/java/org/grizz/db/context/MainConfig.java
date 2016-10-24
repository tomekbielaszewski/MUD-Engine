package org.grizz.db.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class MainConfig {
    private static final int SEC = 1000;
    private static final long REFRESH_DELAY = 10 * SEC;

    @PostConstruct
    public void init() {
    }

    @Scheduled(fixedDelay = REFRESH_DELAY)
    public void scheduled() {

    }
}
