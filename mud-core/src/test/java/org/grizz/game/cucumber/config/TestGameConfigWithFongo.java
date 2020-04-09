package org.grizz.game.cucumber.config;

import org.grizz.game.config.GameConfig;
import org.grizz.game.cucumber.CucumberSharedData;
import org.grizz.game.service.notifier.Notifier;
import org.grizz.game.service.notifier.ProxyNotifier;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

@Configuration
@Import(GameConfig.class)
public class TestGameConfigWithFongo {
    @Autowired
    private ProxyNotifier notifier;

    @PostConstruct
    public void init() {
        notifier.setNotifier(notifier());
    }

    @Bean
    public Notifier notifier() {
        return Mockito.mock(Notifier.class);
    }

    @Bean
    public CucumberSharedData cucumberSharedData() {
        return new CucumberSharedData();
    }
}
