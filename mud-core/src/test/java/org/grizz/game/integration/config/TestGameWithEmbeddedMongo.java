package org.grizz.game.integration.config;

import org.grizz.game.config.GameConfig;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        GameConfig.class,
        MongoAutoConfiguration.class
})
public class TestGameWithEmbeddedMongo {
}
