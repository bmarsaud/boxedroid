package fr.bmarsaud.boxedroid;

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

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch(ParseException e) {
            formatter.printHelp("boxedroid [options]", options);
            System.exit(1);
        }

        Boxedroid boxedroid = new Boxedroid(cmd.getOptionValue("sdk"));
    }
}
