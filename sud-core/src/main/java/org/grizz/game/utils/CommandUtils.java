package org.grizz.game.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Grizz on 2015-08-14.
 */
public class CommandUtils {
    public static boolean isMatching(String command, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(command);
        return matcher.matches();
    }

    public static String[] splitCommand(String command, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(command);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Pattern does not match the command and cannot split it!");
        }

        int numberOfCapturingGroups = matcher.groupCount();
        String[] commandSplit = new String[numberOfCapturingGroups];

        for (int i = 0; i < commandSplit.length; i++) {
            commandSplit[i] = matcher.group(i + 1); //groups are indexed from 1
        }

        return commandSplit;
    }
}
