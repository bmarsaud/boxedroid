package fr.bmarsaud.boxedroid.program;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A abstraction of an external executable program
 */
public class Program {
    private String path;

    public Program(String path) {
        this.path = path;
    }

    /**
     * Execute the program with the given arguments
     * @param args The arguments passed to the program
     * @return The process of the executed program
     */
    public Process execute(String... args) throws IOException {
        List<String> command = new ArrayList<>(Arrays.asList(args));
        command.add(0, path);

        return new ProcessBuilder(command).start();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
