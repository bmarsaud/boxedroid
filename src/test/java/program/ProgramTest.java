package program;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.bmarsaud.boxedroid.program.Program;
import fr.bmarsaud.boxedroid.program.observer.AggregateObserver;

import static org.mockito.Mockito.*;

public class ProgramTest {
    private static final String EXPECTED_OUTPUT = "HelloWorld";

    @Test
    public void execute() throws IOException, InterruptedException {
        Program program = new Program("echo");

        AggregateObserver observer = spy(new AggregateObserver());
        program.onInfo(observer);

        Process process = program.execute(EXPECTED_OUTPUT);
        process.waitFor();

        verify(observer).update(program, EXPECTED_OUTPUT);

        program.unInfo(observer);
        process = program.execute(EXPECTED_OUTPUT);
        process.waitFor();

        verifyNoMoreInteractions(observer);
    }

    // Will fail on Windows
    @Test
    public void withEnvironment() throws IOException, InterruptedException {
        Map<String, String> env = new HashMap<>();
        env.put("ENV_KEY", EXPECTED_OUTPUT);

        Program program = new Program("/bin/bash", env);
        program.addEnvironment("ENV_KEY2", EXPECTED_OUTPUT);

        AggregateObserver observer = spy(new AggregateObserver());
        program.onInfo(observer);

        Process process = program.execute("-c", "echo $ENV_KEY $ENV_KEY2");
        process.waitFor();

        verify(observer, timeout(1000)).update(program, EXPECTED_OUTPUT + " " + EXPECTED_OUTPUT);
    }

    @Test
    public void sendInput() throws IOException, InterruptedException {
        Program program = new Program("/bin/bash");

        AggregateObserver observer = spy(new AggregateObserver());
        program.onInfo(observer);

        Process process = program.execute("-c", "read input; echo $input");
        program.sendInput(EXPECTED_OUTPUT);
        process.waitFor();

        verify(observer, timeout(1000)).update(program, EXPECTED_OUTPUT);
    }

}
