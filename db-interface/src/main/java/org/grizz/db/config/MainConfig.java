package org.grizz.db.config;

import lombok.extern.slf4j.Slf4j;
import org.grizz.db.processing.CommandProcessingTask;
import org.grizz.db.processing.ResponseCollector;
import org.grizz.game.config.GameConfig;
import org.grizz.game.service.notifier.ProxyNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

@Slf4j
@EnableScheduling
@ComponentScan("org.grizz.db")
@Import(GameConfig.class)
@EnableMongoRepositories("org.grizz.db.model.repository")
@SpringBootApplication
public class MainConfig {
    private static final int SEC = 1000;
    private static final long REFRESH_DELAY = 10 * SEC;

    @Autowired
    private ResponseCollector responseCollector;
    @Autowired
    private CommandProcessingTask commandProcessingTask;
    @Autowired
    private ProxyNotifier notifier;

    @Scheduled(fixedDelay = REFRESH_DELAY)
    public void scheduled() {
        log.info("tick...");
        commandProcessingTask.run();
    }

    @PostConstruct
    public void init() {
        notifier.setNotifier(responseCollector::collect);
    }
}