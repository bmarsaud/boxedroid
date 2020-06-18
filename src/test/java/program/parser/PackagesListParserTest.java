package program.parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import fr.bmarsaud.boxedroid.entity.packages.AvailablePackage;
import fr.bmarsaud.boxedroid.entity.packages.AvailableUpdates;
import fr.bmarsaud.boxedroid.entity.packages.InstalledPackage;
import fr.bmarsaud.boxedroid.program.observer.AggregateObserver;
import fr.bmarsaud.boxedroid.program.parser.PackagesListParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PackagesListParserTest {
    private static final String PACKAGES_LIST_OUTPUT = PackagesListParserMock.getPackagesListProgramOutput();
    private static final List<InstalledPackage> EXPECTED_INSTALLED = PackagesListParserMock.getInstalledPackages();
    private static final List<AvailablePackage> EXPECTED_AVAILABLE = PackagesListParserMock.getAvailablePackages();
    private static final List<AvailableUpdates> EXPECTED_UPDATE = PackagesListParserMock.getAvailableUpdates();

    private PackagesListParser parser;

    @BeforeAll
    public void init() throws InterruptedException {
        Process process = mock(Process.class);
        when(process.waitFor()).thenReturn(0);

        parser = new PackagesListParser();
        AggregateObserver observer = (AggregateObserver) parser.getObserver();

        for(String line : PACKAGES_LIST_OUTPUT.split("\n")) {
            observer.update(null, line);
        }
        parser.parse(process);
    }

    @Test
    public void getInstalledPackages() {
        assertEquals(EXPECTED_INSTALLED, parser.getInstalledPackages());
    }

    @Test
    public void getAvailablePackages() {
        assertEquals(EXPECTED_AVAILABLE, parser.getAvailablePackages());
    }

    @Test
    public void getAvailableUpdates() {
        assertEquals(EXPECTED_UPDATE,parser.getAvailableUpdates());
    }
}
