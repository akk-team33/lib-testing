package de.team33.testing.io.hydra;

import de.team33.patterns.io.deimos.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility for unpacking zip files in test scenarios.
 */
public final class ZipIO {

    private static final String CANNOT_UNZIP_RESOURCE = "cannot unzip resource%n" +
                                                        "    resource name   : %s%n" +
                                                        "    referring class : %s%n" +
                                                        "    target path     : %s%n";
    private static final String CANNOT_UNZIP_FILE = "cannot unzip file%n" +
                                                    "    zip file    : %s%n" +
                                                    "    target path : %s%n";

    private ZipIO() {
    }

    private static Void unzip(final InputStream in, final Path targetRoot) throws IOException {
        try (final ZipInputStream zipIn = new ZipInputStream(in)) {
            int counter = 0;
            ZipEntry entry = zipIn.getNextEntry();
            while (null != entry) {
                // Empty directories will be skipped ...
                if (!entry.isDirectory()) {
                    final Path target = targetRoot.resolve(entry.getName());
                    Files.createDirectories(target.getParent());
                    Files.copy(zipIn, target, StandardCopyOption.REPLACE_EXISTING);
                    Files.setLastModifiedTime(target, entry.getLastModifiedTime());
                }
                counter += 1;
                entry = zipIn.getNextEntry();
            }
            if (0 == counter) {
                throw new IOException("zip file is empty or no zip file");
            }
        }
        return null;
    }

    /**
     * Unzip a resource to a given target directory.
     */
    public static void unzip(final Class<?> referringClass, final String resourceName, final Path target) {
        Resource.by(referringClass, resourceName).readByteStream(in -> unzip(in, target));
    }

    /**
     * Unzip a zip file to a given target directory.
     */
    public static void unzip(final Path zipPath, final Path target) {
        Resource.by(zipPath).readByteStream(in -> unzip(in, target));
    }
}
