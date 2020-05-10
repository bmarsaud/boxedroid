package fr.bmarsaud.boxedroid.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service manipulating INI files as implemented in the Google AVD Manager source code
 */
public class INIService {

    /**
     * Write an INI file according to Google AVD Manager implementation using UTF-8 charset
     * @param file The file to write the INI file to
     * @param values The values to write
     * @throws IOException
     */
    public static void write(File file, Map<String, String> values) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

        for(Map.Entry<String, String> entry : values.entrySet()) {
            writer.write(String.format("%1$s=%2$s\n", entry.getKey(), entry.getValue()));
        }

        writer.close();
    }

    /**
     * Read an INI file according to Google AVD Manager implementation using UTF-8 charset
     * @param file The INI file to read
     * @return The values read
     * @throws IOException
     */
    public static Map<String, String> read(File file) throws IOException {
        Map<String, String> values = new LinkedHashMap<>(); // LinkedHashMap to keep values order
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

        String line;
        while((line = reader.readLine()) != null) {
            String entry[] = line.split("=");
            values.put(entry[0], entry[1]);
        }

        return values;
    }

    /**
     * Edit all values in a INI file.
     * The value is added if not present and replaced if present.
     * @param file The INI file to edit
     * @param values The values to edit
     * @throws IOException
     */
    public static void edit(File file, Map<String, String> values) throws IOException {
        Map<String, String> iniFile = INIService.read(file);
        iniFile.putAll(values);

        INIService.write(file, iniFile);
    }
}
