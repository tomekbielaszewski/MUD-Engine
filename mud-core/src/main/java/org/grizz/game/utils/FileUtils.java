package org.grizz.game.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FileUtils {
    @Value("${properties.absolute.path.prefix:}")
    private String pathPrefix;

    public List<Path> listFilesInFolder(String filePath) throws IOException {
        Path directoryPath = getFilepath(filePath);

        return Files.walk(directoryPath)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
    }

    public Path getFilepath(String filePath) {
        String fullPath = pathPrefix + filePath;
        return Paths.get(fullPath);
    }

    public Reader getReader(String filePath) throws IOException {
        return Files.newBufferedReader(getFilepath(filePath), StandardCharsets.UTF_8);
    }
}
