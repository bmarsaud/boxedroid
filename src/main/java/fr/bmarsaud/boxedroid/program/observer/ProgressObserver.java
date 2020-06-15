package fr.bmarsaud.boxedroid.program.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Program observer collecting progress bar details
 */
public class ProgressObserver implements Observer {
    private static String PROGRESS_REGEX = "([0-9]*)% ([^\\n]*)";

    private Pattern pattern;
    private Consumer<ProgressObserver>[] callbacks;

    private String currentStep;
    private int progress;

    public ProgressObserver(Consumer<ProgressObserver>... callbacks) {
        this.callbacks = callbacks;
        pattern = Pattern.compile(PROGRESS_REGEX);

    }

    @Override
    public void update(Observable program, Object obj) {
        String line = (String) obj;
        Matcher matcher = pattern.matcher(line);

        if(matcher.find()) {
            progress = Integer.parseInt(matcher.group(1));
            currentStep = matcher.group(2);

            for(Consumer callback : callbacks) {
                callback.accept(this);
            }
        }
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public int getProgress() {
        return progress;
    }
}
