package org.grizz.game.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Grizz on 2015-04-21.
 */
public class FileUtils {
    public static List<Path> listFilesInFolder(String filePath) throws IOException, URISyntaxException {
        URL file = new Dummy().getClass().getClassLoader().getResource(filePath);
        return Files.walk(Paths.get(file.toURI()))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
    }

    private static class Dummy {
    }
}
