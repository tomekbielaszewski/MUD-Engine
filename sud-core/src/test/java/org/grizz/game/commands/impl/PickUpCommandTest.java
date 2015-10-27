package org.grizz.game.commands.impl;

import com.google.common.collect.Maps;
import org.grizz.game.AbstractTest;
import org.grizz.game.config.GameConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Grizz on 2015-08-11.
 */
@ContextConfiguration(classes = {GameConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PickUpCommandTest extends AbstractTest {
    @Autowired
    private PickUpCommand command;

    @Test
    public void testAccept() throws Exception {
        Map<String, Boolean> commands = Maps.newHashMap();
        commands.put("podnies mlot", Boolean.TRUE);
        commands.put("podnies mlot 3", Boolean.TRUE);
        commands.put("podnies mlot bojowy", Boolean.TRUE);
        commands.put("podnies mlot bojowy 3", Boolean.TRUE);
        commands.put("podnies mlot bojowy 33", Boolean.TRUE);
        commands.put("podnies mlot\n", Boolean.TRUE);
        commands.put("podnies mlot bojowy\n", Boolean.TRUE);
        commands.put("podnies \n", Boolean.TRUE);
        commands.put("podnies mlot 3\n", Boolean.FALSE);
        commands.put("podnies mlot bojowy 3\n", Boolean.FALSE);
        commands.put("podnies ", Boolean.FALSE);
        commands.put("podnies mlot bojowy 3 3", Boolean.FALSE);
        commands.put("podnies mlot bojowy 3 3\n", Boolean.FALSE);
        commands.put("podnies mlot bojowy 3 3 3\n", Boolean.FALSE);
        commands.put("podnies mlot bojowy3\n", Boolean.FALSE);
        commands.put("podnies mlot bojowy 3as\n", Boolean.FALSE);

        commands.put("wez mlot", Boolean.TRUE);
        commands.put("wez mlot 3", Boolean.TRUE);
        commands.put("wez mlot bojowy", Boolean.TRUE);
        commands.put("wez mlot bojowy 3", Boolean.TRUE);
        commands.put("wez mlot bojowy 33", Boolean.TRUE);
        commands.put("wez mlot\n", Boolean.TRUE);
        commands.put("wez mlot bojowy\n", Boolean.TRUE);
        commands.put("wez \n", Boolean.TRUE);
        commands.put("wez mlot 3\n", Boolean.FALSE);
        commands.put("wez mlot bojowy 3\n", Boolean.FALSE);
        commands.put("wez ", Boolean.FALSE);
        commands.put("wez mlot bojowy 3 3", Boolean.FALSE);
        commands.put("wez mlot bojowy 3 3\n", Boolean.FALSE);
        commands.put("wez mlot bojowy 3 3 3\n", Boolean.FALSE);
        commands.put("wez mlot bojowy3\n", Boolean.FALSE);
        commands.put("wez mlot bojowy 3as\n", Boolean.FALSE);

        for (Map.Entry<String, Boolean> commandAndResult : commands.entrySet()) {
            assertThat(commandAndResult.getKey().replaceAll("\n", "[EOL]"),
                    command.accept(commandAndResult.getKey()),
                    is(commandAndResult.getValue().booleanValue()));
        }
    }
}