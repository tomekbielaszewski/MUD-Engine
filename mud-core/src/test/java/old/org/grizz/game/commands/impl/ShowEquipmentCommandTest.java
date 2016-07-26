package old.org.grizz.game.commands.impl;

import old.org.grizz.game.TestContext;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.simple.EquipmentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Grizz on 2015-12-07.
 */
@ContextConfiguration(classes = {TestContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ShowEquipmentCommandTest {
    @Autowired
    @InjectMocks
    private ShowEquipmentCommand command;

    @Mock
    private EquipmentService equipmentService;

    @Mock
    private PlayerContext context;

    @Mock
    private PlayerResponse response;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    //TODO
    @Test
    public void test() {
    }
}