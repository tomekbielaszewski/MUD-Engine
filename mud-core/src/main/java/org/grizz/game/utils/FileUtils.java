package org.grizz.game.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FileUtils {
    public List<Path> listFilesInFolder(String filePath) throws IOException {
        Path directoryPath = Paths.get(filePath);

        return Files.walk(directoryPath)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
    }

    public Path getFilepath(String filePath) {
        return Paths.get(filePath);
    }
}
