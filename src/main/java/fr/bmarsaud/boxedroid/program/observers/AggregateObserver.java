package fr.bmarsaud.boxedroid.program.observers;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Program observer aggregating the program output to a string list
 */
public class AggregateObserver implements Observer {
    private List<String> output;

    public AggregateObserver(List<String> output) {
        this.output = output;
    }

    public AggregateObserver() {
        output = new ArrayList<>();
    }

    @Override
    public void update(Observable program, Object obj) {
        String line = (String) obj;
        this.output.add(line);
    }

    public List<String> getOutput() {
        return output;
    }
}
