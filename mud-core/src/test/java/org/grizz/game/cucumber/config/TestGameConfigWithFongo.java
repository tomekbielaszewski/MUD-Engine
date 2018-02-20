package org.grizz.game.cucumber.config;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.grizz.game.config.GameConfig;
import org.grizz.game.service.notifier.Notifier;
import org.grizz.game.service.notifier.ProxyNotifier;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import javax.annotation.PostConstruct;

@Configuration
@Import(GameConfig.class)
public class TestGameConfigWithFongo extends AbstractMongoConfiguration {
    @Autowired
    private ProxyNotifier notifier;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @PostConstruct
    public void init() {
        notifier.setNotifier(notifier());
    }

    @Bean
    public Notifier notifier() {
        return Mockito.mock(Notifier.class);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new Fongo("Fongo").getMongo();
    }
}