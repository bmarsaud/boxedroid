package fr.bmarsaud.boxedroid.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class IOUtils {
    /**
     * Download a file from a source to a destination.
     * @param sourceUrl The source URL to download the file from.
     * @param destination The destination to download the file to.
     */
    public static void downloadFile(String sourceUrl, Path destination) throws IOException {
        URL url = new URL(sourceUrl);
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        try(FileOutputStream fileOutputStream = new FileOutputStream(destination.toFile())) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }

    /**
     * Unzip all files from a zip file to a destination.
     * @param zipFile The zip file to unzip.
     * @param destination The destination where to unzip the archive.
     */
    public static void unzipFile(File zipFile, Path destination) throws IOException {
        try(FileInputStream fileInputStream = new FileInputStream(zipFile);
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream)) {
            for(ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry()) {
                File outputFile = destination.resolve(zipEntry.getName()).toFile();
                if(zipEntry.isDirectory()) {
                    if (!outputFile.isDirectory() && !outputFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + outputFile);
                    }
                } else {
                    File parent = outputFile.getParentFile();
                    if(!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }
                    try(FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                        fileOutputStream.getChannel().transferFrom(Channels.newChannel(zipInputStream), 0, Long.MAX_VALUE);
                    }
                }
            }
        }
    }

    /**
     * Read the first line from the standard input.
     * @return The first line of the standard input, null if an {@link IOException} occurred.
     */
    public static String readFromStandardInput() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            return br.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}
