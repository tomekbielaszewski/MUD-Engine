package org.grizz.game.command.parsers;

import com.google.common.collect.Lists;
import org.grizz.game.command.Command;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CommandParser implements Command {
    private Environment env;

    public CommandParser(Environment env) {
        this.env = env;
    }

    protected List<String> getCommandPatterns(String key) {
        String commandPatterns = env.getProperty(key);
        if (commandPatterns == null) throw new IllegalStateException("command " + key + " has no mapping!");
        String[] commandPatternsArray = commandPatterns
                .split(";");
        return Lists.newArrayList(commandPatternsArray);
    }

    protected String getMatchedPattern(String command, String key) {
        String matchedPattern = getCommandPatterns(key).stream()
                .filter(pattern -> command.matches(pattern))
                .findFirst().get();
        return matchedPattern;
    }

    protected boolean isAnyMatching(String command, String key) {
        return getCommandPatterns(key).stream()
                .anyMatch(pattern -> isMatching(command, pattern));
    }

    protected boolean isMatching(String command, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(command);
        return matcher.matches();
    }

    protected boolean hasVariable(String variableName, String command, String pattern) {
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

    protected String getVariable(String variableName, String command, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(command);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Pattern does not match the command and cannot split it!");
        }
        String group = matcher.group(variableName);

        return group;
    }

    protected String getVariableOrDefaultValue(String variableName, String defaultValue, String command, String pattern) {
        if (hasVariable(variableName, command, pattern)) {
            return getVariable(variableName, command, pattern);
        } else {
            return defaultValue;
        }
    }

    protected Set<String> getVariableNames(String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Method namedGroups = ReflectionUtils.findMethod(Pattern.class, "namedGroups");
        ReflectionUtils.makeAccessible(namedGroups);
        Object namedGroupsMap = ReflectionUtils.invokeMethod(namedGroups, compiledPattern);
        Set<String> variableNames = ((Map<String, Integer>) namedGroupsMap).keySet();

        return variableNames;
    }
}
