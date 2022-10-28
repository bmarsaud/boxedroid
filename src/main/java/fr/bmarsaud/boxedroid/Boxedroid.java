package fr.bmarsaud.boxedroid;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

import fr.bmarsaud.boxedroid.entity.ABI;
import fr.bmarsaud.boxedroid.entity.AndroidVersion;
import fr.bmarsaud.boxedroid.entity.Device;
import fr.bmarsaud.boxedroid.entity.Variant;
import fr.bmarsaud.boxedroid.entity.exception.DeviceNotAvailableException;
import fr.bmarsaud.boxedroid.entity.exception.SDKException;
import fr.bmarsaud.boxedroid.service.AVDService;
import fr.bmarsaud.boxedroid.service.CLIToolsService;
import fr.bmarsaud.boxedroid.service.SDKService;

public class Boxedroid {
    private static String DEFAULT_DEVICE = "pixel_2";
    private static String DEFAULT_ABI = "x86";
    private static String DEFAULT_VARIANT = "google_apis";

    private Logger logger = LoggerFactory.getLogger(Boxedroid.class);
    private String sdkPath;
    private String cmdlineToolsPath;

    public Boxedroid(String sdkPath) {
        this.sdkPath = sdkPath;
    }

    /**
     * Launch an emulator of a given version, abi, variant and on a certain device
     * @param version The Android version the AVD
     * @param abi The ABI of the AVD
     * @param variant The variant of the AVD
     * @param device The device name of the AVD
     */
    public void launchEmulator(AndroidVersion version, ABI abi, Variant variant, String device) {
        String currentDir = null;
        try {
            URI uri = Boxedroid.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            File file = new File(uri);
            if(file.isDirectory()) {
                currentDir = uri.getPath();
            } else {
                currentDir = file.getParent();
            }
        } catch (URISyntaxException e) {
            logger.error("Impossible to retrieve the current directory");
            System.exit(1);
        };

        SDKService sdkService = new SDKService(sdkPath, cmdlineToolsPath);
        AVDService avdService = new AVDService(sdkPath, cmdlineToolsPath, currentDir);

        try {
            sdkService.install(version.getApiLevel(), abi, variant);
            avdService.createAndLaunch(version.getApiLevel(), abi, variant, device);
        } catch(SDKException e) {
            logger.error(e.getMessage());

            if(e instanceof DeviceNotAvailableException) {
                logger.error(
                    "Available devices: " +
                    avdService.getAvailableDevices().stream().map(Device::getCode).collect(Collectors.joining(", "))
                );
            }
            System.exit(1);
        }
    }

    /**
     * Find where cmdline-tools are installed. Install them if not found.
     */
    public void acquireCommandLineTools() {
        CLIToolsService cliToolsService = new CLIToolsService(sdkPath);
        try {
            cmdlineToolsPath = cliToolsService.acquireCommandLineTools();
        } catch (IOException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(
            Option.builder()
                .longOpt("sdk")
                .desc("Android SDK absolute path (required)")
                .hasArg()
                .argName("SDK_PATH")
                .required()
                .build()
        );

        options.addOption(
            Option.builder()
                .longOpt("android")
                .desc("Android Version (required)")
                .hasArg()
                .argName("ANDROID_VERSION")
                .required()
                .build()
        );

        options.addOption(
            Option.builder()
                .longOpt("abi")
                .desc("Emulator ABI")
                .hasArg()
                .argName("ABI")
                .build()
        );


        options.addOption(
            Option.builder()
                .longOpt("variant")
                .desc("Android platform variant")
                .hasArg()
                .argName("VARIANT")
                .build()
        );

        options.addOption(
            Option.builder()
                .longOpt("device")
                .desc("Emulator device")
                .hasArg()
                .argName("DEVICE_NAME")
                .build()
        );

        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch(ParseException e) {
            helpFormatter.printHelp("boxedroid --sdk <SDK_PATH> --android <ANDROID_VERSION> [options]", options);
            System.exit(1);
        }

        String sdkPath = cmd.getOptionValue("sdk");
        AndroidVersion androidVersion = AndroidVersion.fromCode(cmd.getOptionValue("android"));
        ABI abi = ABI.fromId(cmd.getOptionValue("abi",  DEFAULT_ABI));
        Variant variant = Variant.fromId(cmd.getOptionValue("variant", DEFAULT_VARIANT));
        String device = cmd.getOptionValue("device", DEFAULT_DEVICE);

        if(androidVersion == null) {
            System.err.println("Unknown Android version '" + cmd.getOptionValue("android") +"', available versions:");
            for(AndroidVersion version : AndroidVersion.values()) {
                System.err.print("- ");
                for(int i = 0; i < version.getCodes().length; i++) {
                    System.err.print(version.getCodes()[i]);
                    if(i < version.getCodes().length - 1) {
                        System.err.print(", ");
                    }
                }
                System.err.print("\n");
            }
            System.exit(1);
        }

        if(abi == null) {
            System.err.println(
                "Unknown ABI '" + cmd.getOptionValue("abi") + "', available ABIs: " +
                Arrays.asList(ABI.values()).stream().map(ABI::name).map(String::toLowerCase).collect(Collectors.joining(", "))
            );
            System.exit(1);
        }

        if(variant == null) {
            System.err.println(
                "Unknown variant '" + cmd.getOptionValue("variant") + "', available variants: " +
                Arrays.asList(Variant.values()).stream().map(Variant::name).map(String::toLowerCase).collect(Collectors.joining(", "))
            );
            System.exit(1);
        }

        Boxedroid boxedroid = new Boxedroid(sdkPath);
        boxedroid.acquireCommandLineTools();
        boxedroid.launchEmulator(androidVersion, abi, variant, device);
    }
}
