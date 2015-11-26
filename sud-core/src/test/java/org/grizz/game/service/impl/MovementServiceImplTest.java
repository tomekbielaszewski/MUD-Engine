package org.grizz.game.service.impl;

import org.grizz.game.TestContext;
import org.grizz.game.service.complex.MovementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Grizz on 2015-04-26.
 */
@ContextConfiguration(classes = {TestContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class MovementServiceImplTest {
    @Autowired
    private MovementService movementService;

    @Test
    public void test() {

    }
}