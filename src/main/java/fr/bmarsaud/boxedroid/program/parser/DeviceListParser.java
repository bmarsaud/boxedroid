package fr.bmarsaud.boxedroid.program.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import fr.bmarsaud.boxedroid.entity.Device;
import fr.bmarsaud.boxedroid.program.observer.AggregateObserver;
import fr.bmarsaud.boxedroid.util.IOUtils;

/**
 * Parse the output of the AVD Manager list device command to a list of devices
 */
public class DeviceListParser {
    private static final String DEVICE_SEPARATOR = "---------";
    private static final String ID_TAG = "id:";
    private static final String CODE_TAG = " or ";
    private static final String NAME_TAG = "Name:";
    private static final String OEM_TAG = "OEM :";
    private static final String TAG_TAG = "Tag :";

    private Observer observer;
    private List<String> programOutput;

    private List<Device> devices;

    public DeviceListParser() {
        this.devices = new ArrayList<>();
        this.programOutput = new ArrayList<>();
        this.observer = new AggregateObserver(this.programOutput);
    }

    /**
     * Parse the output of the given process
     * @param process The AVDManager device list process
     * @throws InterruptedException
     */
    public void parse(Process process) throws InterruptedException {
        process.waitFor();

        Device currentDevice = new Device();
        for(String line : programOutput) {
            if(line.contains(DEVICE_SEPARATOR)) {
                devices.add(currentDevice);
                currentDevice = new Device();
                continue;
            }

            if(line.contains(ID_TAG)) {
                line = line.replace(ID_TAG, "");
                String[] idPair = line.split(CODE_TAG);
                String id = idPair[0].trim();
                String code = idPair[1].replaceAll("\"", "");

                currentDevice.setId(Integer.parseInt(id));
                currentDevice.setCode(code);
            } else if(line.contains(NAME_TAG)) {
                String name = line.split(NAME_TAG)[1].trim();
                currentDevice.setName(name);
            } else if(line.contains(OEM_TAG)) {
                String oem = line.split(OEM_TAG)[1].trim();
                currentDevice.setOem(oem);
            } else if(line.contains(TAG_TAG)) {
                String tag = line.split(TAG_TAG)[1].trim();
                currentDevice.setTag(tag);
            }
        }

        devices.add(currentDevice);
    }

    /**
     * Returns the observer aggregating the program output
     * @return The observer
     */
    public Observer getObserver() {
        return observer;
    }

    /**
     * Returns the parsed list of available devices.
     * Will be empty if {@link DeviceListParser#parse(Process)} was not called before
     * @return The available devices list
     */
    public List<Device> getDevices() {
        return devices;
    }
}
