package fr.bmarsaud.boxedroid.program;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A abstraction of an external executable program
 */
public class Program {
    private String path;
    private Map<String, String> environment;

    public Program(String path, Map<String, String> environment) {
        this.path = path;
        this.environment = environment;
    }

    public Program(String path) {
        this.path = path;
        this.environment = new HashMap<>();
    }

    /**
     * Execute the program with the given arguments
     * @param args The arguments passed to the program
     * @return The process of the executed program
     */
    public Process execute(String... args) throws IOException {
        List<String> command = new ArrayList<>(Arrays.asList(args));
        command.add(0, path);

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.environment().putAll(environment);

        return pb.start();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    /**
     * Add an environment variable to the next execution of the program
     * @param key The key of the environment variable
     * @param value The value of the environment variable
     * @return The updated program
     */
    public Program addEnvironment(String key, String value) {
        environment.put(key, value);
        return this;
    }
}
