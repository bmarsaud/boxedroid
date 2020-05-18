package fr.bmarsaud.boxedroid.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IOUtils {
    /**
     * Returns a {@link String} containing the content of a given {@link InputStream}
     * @param is The input stream to parse
     * @return The parsed string
     */
    public static String streamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String output = "";
        String line;
        try {
            while((line = reader.readLine()) != null) {
                output += line + "\n";
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    /**
     * Remove all the spaces at the beginning and the end of a string
     * @param str The string to remove the spaces to
     * @return The string without surrounding spaces
     */
    public static String removeSurroundingSpaces(String str) {
        if(str == null || str.length() == 0) {
            return str;
        }

        while(str.length() > 0 && str.charAt(0) == ' ') {
            str = str.substring(1);
        }

        while(str.length() > 0 && str.charAt(str.length() - 1) == ' ') {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    /**
     * Get all files in a directory
     * @param dirPath The directory to returns the files
     * @param recursively True the files are found recursively, False if we want only the first level
     * @return The list of files found
     */
    public static List<String> getFilesInDir(String dirPath, boolean recursively) {
        List<String> files = new ArrayList<>();
        Path rootPath = Paths.get(dirPath);

        try {
            Stream<Path> fileStream;

            if(recursively) {
                fileStream = Files.walk(rootPath);
            } else {
                fileStream = Files.walk(rootPath, 1);
            }

            files = fileStream.map(path -> path.toAbsolutePath().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        files.removeIf(filePath -> filePath.equals(rootPath.toAbsolutePath().toString()));
        return files;
    }
}
