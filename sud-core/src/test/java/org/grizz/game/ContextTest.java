package org.grizz.game;

import org.grizz.game.config.GameConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Grizz on 2015-10-27.
 */
@ContextConfiguration(classes = {GameConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ContextTest {
    @Test
    public void contextLoads() {
    }
}
