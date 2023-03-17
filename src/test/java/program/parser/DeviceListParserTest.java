package program.parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import fr.bmarsaud.boxedroid.entity.Device;
import fr.bmarsaud.boxedroid.program.parser.DeviceListParser;

public class DeviceListParserTest {
    public static final String DEVICE_LIST_OUTPUT = DeviceListParserMock.getDeviceListProgramOutput();
    public static final List<Device> EXPECTED_DEVICES = DeviceListParserMock.getDeviceList();

    @Test
    @DisplayName("Parsing real device list")
    public void parse() throws InterruptedException{
        Process process = mock(Process.class);
        when(process.waitFor()).thenReturn(0);

        DeviceListParser parser = new DeviceListParser();
        for(String line : DEVICE_LIST_OUTPUT.split("\n")) {
            parser.getObserver().update(null, line);
        }

        parser.parse(process);
        assertEquals(EXPECTED_DEVICES, parser.getDevices());
    }

}
