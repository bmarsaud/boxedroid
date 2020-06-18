package program.parser;

import java.util.ArrayList;
import java.util.List;

import fr.bmarsaud.boxedroid.entity.Device;

public class DeviceListParserMock {
    public static String getDeviceListProgramOutput() {
        return "id: 0 or \"tv_1080p\"\n" +
                "    Name: Android TV (1080p)\n" +
                "    OEM : Google\n" +
                "    Tag : android-tv\n" +
                "---------\n" +
                "id: 1 or \"tv_720p\"\n" +
                "    Name: Android TV (720p)\n" +
                "    OEM : Google\n" +
                "    Tag : android-tv\n" +
                "    Unknown line\n" +
                "---------\n" +
                "id: 2 or \"wear_round\"\n" +
                "    Name: Android Wear Round\n" +
                "    OEM : Google\n" +
                "    Tag : android-wear\n";
    }

    public static List<Device> getDeviceList() {
        List<Device> devices = new ArrayList<>();
        devices.add(new Device(0, "tv_1080p", "Android TV (1080p)", "Google", "android-tv"));
        devices.add(new Device(1, "tv_720p", "Android TV (720p)", "Google", "android-tv"));
        devices.add(new Device(2, "wear_round", "Android Wear Round", "Google", "android-wear"));

        return devices;
    }
}
