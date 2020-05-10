package fr.bmarsaud.boxedroid.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
