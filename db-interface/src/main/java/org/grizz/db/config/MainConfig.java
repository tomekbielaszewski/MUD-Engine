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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

@Slf4j
@EnableScheduling
@ComponentScan("org.grizz.db")
@Import(GameConfig.class)
@SpringBootApplication
public class MainConfig {
    @Autowired
    private ResponseCollector responseCollector;
    @Autowired
    private CommandProcessingTask commandProcessingTask;
    @Autowired
    private ProxyNotifier notifier;

    @Scheduled(cron = "*/1 * * * * *")
    public void scheduled() {
        commandProcessingTask.run();
    }

    @PostConstruct
    public void init() {
        notifier.setNotifier(responseCollector::collect);
    }
}
