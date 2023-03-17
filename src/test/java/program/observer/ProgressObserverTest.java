package program.observer;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import fr.bmarsaud.boxedroid.program.observer.ProgressObserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProgressObserverTest {
    private static final String PROGRESS_TEXT = "Progressing...";

    @Test
    @DisplayName("No progression")
    public void noProgression() {
        ProgressObserver observer = new ProgressObserver();
        observer.update(null, "non progressing line");

        assertNull(observer.getCurrentStep());
        assertEquals(0, observer.getProgress());
    }

    @Test
    @DisplayName("Successive progressions")
    public void withProgression() {
        ProgressObserver observer = new ProgressObserver();
        observer.update(null, "10% " + PROGRESS_TEXT);

        assertEquals(PROGRESS_TEXT, observer.getCurrentStep());
        assertEquals(10, observer.getProgress());

        observer.update(null, "20% " + PROGRESS_TEXT);

        assertEquals(PROGRESS_TEXT, observer.getCurrentStep());
        assertEquals(20, observer.getProgress());
    }

    @Test
    @DisplayName("With callback specified")
    public void withCallback() {
        Consumer<ProgressObserver> mockedCallback = spy(new Consumer<ProgressObserver>() {
            public void accept(ProgressObserver observer) {
                assertEquals(PROGRESS_TEXT, observer.getCurrentStep());
                assertEquals(10, observer.getProgress());
            }
        });

        ProgressObserver observer = new ProgressObserver(mockedCallback);
        observer.update(null, "10% " + PROGRESS_TEXT);

        verify(mockedCallback).accept(observer);
    }
}
