package org.grizz.game;

import org.grizz.game.service.utils.CommandUtils;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Grizz on 2015-12-07.
 */
public class StringPatternsTest {
    private CommandUtils command = new CommandUtils();

    @Test
    public void testNickPatterns() {
        //At least 4 chars
        //No whitespaces
        //Digits and chars allowed
        //Special chars allowed: -_

        String pattern = "prefix ([\\w-]{4,}) suffix";

        assertThat(command.isMatching("prefix Grizz suffix", pattern), is(true));
        assertThat(command.isMatching("prefix grizz suffix", pattern), is(true));
        assertThat(command.isMatching("prefix abc123 suffix", pattern), is(true));
        assertThat(command.isMatching("prefix 123abc suffix", pattern), is(true));
        assertThat(command.isMatching("prefix _abc123_ suffix", pattern), is(true));
        assertThat(command.isMatching("prefix _123abc_ suffix", pattern), is(true));
        assertThat(command.isMatching("prefix abcd suffix", pattern), is(true));
        assertThat(command.isMatching("prefix ab_d suffix", pattern), is(true));
        assertThat(command.isMatching("prefix ab-d suffix", pattern), is(true));
        assertThat(command.isMatching("prefix -abc suffix", pattern), is(true));
        assertThat(command.isMatching("prefix abc- suffix", pattern), is(true));

        assertThat(command.isMatching("prefix grizz lol suffix", pattern), is(false));
        assertThat(command.isMatching("prefix 123 abc suffix", pattern), is(false));
        assertThat(command.isMatching("prefix 1 suffix", pattern), is(false));
        assertThat(command.isMatching("prefix _ abc 123 _ suffix", pattern), is(false));
        assertThat(command.isMatching("prefix _ 123 abc _ suffix", pattern), is(false));
        assertThat(command.isMatching("prefix abc suffix", pattern), is(false));
        assertThat(command.isMatching("prefix Grizz lol suffix", pattern), is(false));
        assertThat(command.isMatching("prefix  suffix", pattern), is(false));
    }
}
