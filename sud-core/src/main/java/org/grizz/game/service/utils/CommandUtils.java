package org.grizz.game.service.utils;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Grizz on 2015-08-14.
 */
@Service
public class CommandUtils {
    @Autowired
    private Environment env;

    public List<String> getCommandPatterns(String key) {
        String[] commandPatternsArray = env.getProperty(key)
                .split(";");
        return Lists.newArrayList(commandPatternsArray);
    }

    public String getMatchedPattern(String command, String key) {
        String matchedPattern = getCommandPatterns(key).stream()
                .filter(pattern -> command.matches(pattern))
                .findFirst().get();
        return matchedPattern;
    }

    public boolean isAnyMatching(String command, String key) {
        return getCommandPatterns(key).stream()
                .anyMatch(pattern -> isMatching(command, pattern));
    }

    public boolean isMatching(String command, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(command);
        return matcher.matches();
    }

    public String[] splitCommand(String command, String pattern) {
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

    public boolean hasVariable(String variableName, String command, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(command);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Pattern does not match the command and cannot split it!");
        }
        try {
            matcher.group(variableName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getVariable(String variableName, String command, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(command);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Pattern does not match the command and cannot split it!");
        }
        String group = matcher.group(variableName);

        return group;
    }
}
