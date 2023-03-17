package program.observer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import fr.bmarsaud.boxedroid.program.Program;
import fr.bmarsaud.boxedroid.program.observer.licence.LicenceObserver;

import static org.mockito.Mockito.*;

public class LicenceObserverTest {
    private static final List<String> LICENCE = Arrays.asList("Terms and Conditions",
            "---------------------------------------",
            "Licence line 1",
            "Licence line 2",
            "---------------------------------------",
            "Please accept licence !"
    );

    @Test
    public void licenceObserving() {
        LicenceObserver licenceObserver = spy(new LicenceObserver() {
            public void onLicenseDisplay(String licenceLine) { }
            public void onLicenceAcceptanceRequest(Program program) { }
        });

        for(String line : LICENCE) {
            licenceObserver.update(null, line);
        }

        verify(licenceObserver, times(1)).onLicenseDisplay(LICENCE.get(0));
        verify(licenceObserver, times(2)).onLicenseDisplay(LICENCE.get(1));
        verify(licenceObserver, times(1)).onLicenseDisplay(LICENCE.get(2));
        verify(licenceObserver, times(1)).onLicenseDisplay(LICENCE.get(3));
        verify(licenceObserver, times(0)).onLicenseDisplay(LICENCE.get(5));
        verify(licenceObserver, times(1)).onLicenceAcceptanceRequest(null);
    }
}
