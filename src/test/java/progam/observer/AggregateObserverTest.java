package progam.observer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import fr.bmarsaud.boxedroid.program.observer.AggregateObserver;

@DisplayName("Aggregate observer")
public class AggregateObserverTest {
    public static final List<String> OUTPUT = Arrays.asList(
            "LINE 1",
            "LINE 2",
            "LINE 3"
    );

    @Test
    @DisplayName("Aggregation behavior")
    public void getOutput() {
        AggregateObserver aggregateObserver = new AggregateObserver();
        for(String line : OUTPUT) {
            aggregateObserver.update(null, line);
        }

       assertEquals(OUTPUT, aggregateObserver.getOutput());
    }

    @Test
    @DisplayName("Already started aggregation")
    public void constructor() {
        AggregateObserver  aggregateObserver = new AggregateObserver(OUTPUT);
        assertEquals(OUTPUT, aggregateObserver.getOutput());
    }

}
