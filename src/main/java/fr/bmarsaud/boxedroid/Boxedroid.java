package fr.bmarsaud.boxedroid;

import com.sun.org.apache.xpath.internal.operations.And;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.bmarsaud.boxedroid.entity.AndroidVersion;
import fr.bmarsaud.boxedroid.entity.exception.SDKException;
import fr.bmarsaud.boxedroid.program.AVDManager;
import fr.bmarsaud.boxedroid.service.AVDService;

public class Boxedroid {
    private String sdkPath;

    public Boxedroid(String sdkPath) {
        this.sdkPath = sdkPath;
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
    }
}
