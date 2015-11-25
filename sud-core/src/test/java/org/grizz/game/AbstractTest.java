package org.grizz.game;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Created by Grizz on 2015-10-27.
 */
public abstract class AbstractTest {
    private static final MongodStarter starter = MongodStarter.getDefaultInstance();
    private static MongodExecutable _mongodExe;
    private static MongodProcess _mongod;
    public static MongoClient _mongo;

    @BeforeClass
    public static void setUp() throws Exception {
        if (null == _mongo) {
            _mongodExe = starter.prepare(new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net("127.0.0.1", 27017, Network.localhostIsIPv6()))
                    .build());
            _mongod = _mongodExe.start();

            _mongo = new MongoClient("localhost", 27017);
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        _mongod.stop();
        _mongodExe.stop();
    }
}
