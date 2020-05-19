package fr.bmarsaud.boxedroid.entity;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import fr.bmarsaud.boxedroid.service.INIService;

/**
 * A created AVD
 */
public class AVD {
    public static final String SYS_IMAGE_INI_KEY = "image.sysdir.1";
    private static final String DEVICE_NAME_KEY = "hw.device.name";
    private static final String SYS_IMAGE_PATH_DIR = "system-images";

    private static final String CONFIG_PATH = "config.ini";

    private String name;
    private String path;
    private Map<String, String> iniConfig;

    public AVD(String name, String path) {
        this.name = name;
        this.path = path;
    }

    /**
     * Load the AVD INI configuration from disk
     * @throws IOException
     */
    public void loadConfig() throws IOException {
        iniConfig = INIService.read(new File(path, CONFIG_PATH));
    }

    /**
     * Save the AVD INI configuration to disk
     * @throws IOException
     */
    public void save() throws IOException {
        INIService.write(new File(path, CONFIG_PATH), iniConfig);
    }

    public APILevel getAPILevel() {
        String sysImage = iniConfig.get(SYS_IMAGE_INI_KEY);
        String[] dirs = sysImage.split(SYS_IMAGE_PATH_DIR)[1].split(File.separator);

        return APILevel.fromName(dirs[1]);
    }

    public ABI getABI() {
        String sysImage = iniConfig.get(SYS_IMAGE_INI_KEY);
        String[] dirs = sysImage.split(SYS_IMAGE_PATH_DIR)[1].split(File.separator);

        return ABI.fromId(dirs[3]);
    }

    public Variant getVariant() {
        String sysImage = iniConfig.get(SYS_IMAGE_INI_KEY);
        String[] dirs = sysImage.split(SYS_IMAGE_PATH_DIR)[1].split(File.separator);

        return Variant.fromId(dirs[2]);
    }

    public String getDeviceName() {
        return iniConfig.get(DEVICE_NAME_KEY);
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    /**
     * Edit the AVD INI configuration
     * @param key The key to set the value to
     * @param value The value
     */
    public void editConfig(String key, String value) {
        iniConfig.put(key, value);
    }


    /**
     * Return a configuration value from its key
     * @param key The configuration key
     * @return The configuration value
     */
    public String getConfigValue(String key) {
        return iniConfig.get(key);
    }
}
