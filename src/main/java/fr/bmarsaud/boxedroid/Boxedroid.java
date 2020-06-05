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

import java.net.URISyntaxException;

import fr.bmarsaud.boxedroid.entity.ABI;
import fr.bmarsaud.boxedroid.entity.AndroidVersion;
import fr.bmarsaud.boxedroid.entity.Variant;
import fr.bmarsaud.boxedroid.entity.exception.SDKException;
import fr.bmarsaud.boxedroid.service.AVDService;
import fr.bmarsaud.boxedroid.service.SDKService;

public class Boxedroid {
    private static String DEFAULT_DEVICE = "pixel_2";
    private static ABI DEFAULT_ABI = ABI.X86;
    private static Variant DEFAULT_VARIANT = Variant.GOOGLE_APIS;

    private Logger logger = LoggerFactory.getLogger(Boxedroid.class);
    private String sdkPath;

    public Boxedroid(String sdkPath) {
        this.sdkPath = sdkPath;
    }

    /**
     * Create and launch an emulator of a given version
     * @param version The Android version of the emulator
     */
    public void launchEmulator(AndroidVersion version) {
        String currentDir = null;
        try {
            currentDir = Boxedroid.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            logger.error("Impossible to retrieve the current directory");
        };

        SDKService sdkService = new SDKService(sdkPath);
        AVDService avdService = new AVDService(sdkPath, currentDir);

        try {
            sdkService.install(version.getApiLevel(), DEFAULT_ABI, DEFAULT_VARIANT);
            avdService.createAndLaunch(version.getApiLevel(), DEFAULT_ABI, DEFAULT_VARIANT, DEFAULT_DEVICE);
        } catch(SDKException e) {
            logger.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(
            Option.builder()
                .longOpt("sdk")
                .desc("Android SDK absolute path")
                .hasArg()
                .argName("SDK_PATH")
                .required()
                .build()
        );

        options.addOption(
            Option.builder()
                .longOpt("android")
                .desc("Android Version")
                .hasArg()
                .argName("ANDROID_VERSION")
                .required()
                .build()
        );

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch(ParseException e) {
            formatter.printHelp("boxedroid [options]", options);
            System.exit(1);
        }

        String sdkPath = cmd.getOptionValue("sdk");
        AndroidVersion androidVersion = AndroidVersion.fromCode(cmd.getOptionValue("android"));

        if(androidVersion == null) {
            System.err.println("Unknown android version '" + cmd.getOptionValue("android") +"', available versions :");
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
        }

        Boxedroid boxedroid = new Boxedroid(sdkPath);
        boxedroid.launchEmulator(androidVersion);
    }
}
