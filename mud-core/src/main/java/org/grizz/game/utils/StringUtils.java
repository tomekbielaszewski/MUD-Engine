package org.grizz.game.utils;

public class StringUtils {
    public static String stripAccents(String textWithDiacritics) {
        return org.apache.commons.lang3.StringUtils.stripAccents(textWithDiacritics.trim().toLowerCase())
                .replaceAll("ł", "l")
                .replaceAll("Ł", "L");
    }
}
