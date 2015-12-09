package org.grizz.game;

import java.util.Map;
import java.util.function.Predicate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Grizz on 2015-12-03.
 */
public class CommandTestUtils {
    public static void testCommands(Map<String, Boolean> commands, Predicate<String> accept) {
        for (Map.Entry<String, Boolean> commandAndResult : commands.entrySet()) {
            try {
                assertThat(commandAndResult.getKey().replaceAll("\n", "[EOL]"),
                        accept.test(commandAndResult.getKey()),
                        is(commandAndResult.getValue().booleanValue()));
            } catch (AssertionError e) {
                System.err.println(commandAndResult.getKey());
                throw e;
            }
        }
    }
}
