package de.team33.testing.io.hydra;

import de.team33.patterns.io.deimos.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

/**
 * Utility for copying files in test scenarios.
 */
public final class FileIO {

    private FileIO() {
    }

    private static Void copy(final InputStream in, final FileTime lastModified, final Path target) throws IOException {
        Files.createDirectories(target.getParent());
        Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        Files.setLastModifiedTime(target, lastModified);
        return null;
    }

    /**
     * Copy a resource file to a given target directory.
     */
    public static void copy(final Class<?> refClass, final String rsrcName, final Path target) {
        Resource.by(refClass, rsrcName)
                .readByteStream(in -> copy(in, FileTime.from(Instant.now()), target));
    }

    /**
     * Copy a source file to a given target directory.
     */
    public static void copy(final Path source, final Path target) {
        Resource.by(source)
                .readByteStream(in -> copy(in, Files.getLastModifiedTime(source), target));
    }
}
