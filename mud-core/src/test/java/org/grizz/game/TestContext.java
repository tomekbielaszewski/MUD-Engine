package org.grizz.game;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.grizz.game.config.GameConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Created by Grizz on 2015-11-26.
 */
@Configuration
@Import(GameConfig.class)
public class TestContext extends AbstractMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new Fongo(getDatabaseName()).getMongo();
    }
}
