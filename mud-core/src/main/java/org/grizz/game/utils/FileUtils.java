package org.grizz.game.utils;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Grizz on 2015-04-21.
 */
@Slf4j
public class FileUtils {
    private static FileSystem fs;

    public static List<Path> listFilesInFolder(String filePath) throws IOException, URISyntaxException {
        URL directory = new Dummy().getClass().getClassLoader().getResource(filePath);
        log.info("Accessing directory: " + directory.toString());

        if (directory.toString().contains("!")) { //folder is in jar file
            return listFilesInFolderLocatedInJarFile(directory);
        } else {
            return listFilesInFolder(directory);
        }
    }

    private static List<Path> listFilesInFolderLocatedInJarFile(URL directory) throws IOException {
        if (fs != null) {
            fs.close();
        }
        String[] filePathSplit = directory.toString().split("!");
        fs = FileSystems.newFileSystem(URI.create(filePathSplit[0]), Maps.newHashMap());
        Path directoryPath = fs.getPath(filePathSplit[1]);

        return Files.walk(directoryPath)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
    }

    private static List<Path> listFilesInFolder(URL directory) throws URISyntaxException, IOException {
        Path directoryPath = Paths.get(directory.toURI());

        return Files.walk(directoryPath)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
    }

    public static Path getFilepath(String filePath) throws IOException {
        URL url = new Dummy().getClass().getClassLoader().getResource(filePath);
        if(url == null) {
            throw new IOException("Problem loading file ["+filePath+"]");
        }
        log.debug("Accessing file: " + filePath);
        Path path = null;
        try {
            path = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
        return path;
    }

    private static class Dummy {
    }
}
