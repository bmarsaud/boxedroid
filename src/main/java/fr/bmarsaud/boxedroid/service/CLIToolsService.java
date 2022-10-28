package fr.bmarsaud.boxedroid.service;

import org.apache.commons.lang3.SystemUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CLIToolsService {
    private static final String ANDROID_STUDIO_DOWNLOAD_PAGE = "https://developer.android.com/studio";

    private final String sdkPath;
    private Document downloadPageDocument;

    public CLIToolsService(String sdkPath) {
        this.sdkPath = sdkPath;
    }

    /**
     * Checks if cmdline-tools are available from the SDK path.
     * If not, download them from the Google website after asking the user to accept the agreements
     * terms.
     * @return The path to the cmdline-tools binaries
     */
    public String acquireCommandLineTools() throws IOException {
        String commandLineToolsPath = resolveCommandLineToolsPath(sdkPath);
        if(commandLineToolsPath == null) {
            System.out.println("Command Line Tools not found in " + sdkPath);
            System.out.println("Do you want to download it from Google website? (y/n)");

            String answer = IOUtils.readFromStandardInput();
            if (!"y".equalsIgnoreCase(answer)) {
               System.exit(1);
            }

            String terms = getCommandLineToolsHTMLTerms();
            if(terms != null) {
                System.out.println(terms.replaceAll("<br \\>", "\n"));
                System.out.println("I have read and agree with the above terms and conditions (y/n)");

                answer = IOUtils.readFromStandardInput();
                if (!"y".equalsIgnoreCase(answer)) {
                    System.exit(1);
                }

                String downloadUrl = getCommandLineToolsDownloadURL();
                Path downloadedFile = Paths.get(System.getProperty("java.io.tmpdir"), "cmdline-tools.zip");
                IOUtils.downloadFile(downloadUrl, downloadedFile);
                IOUtils.unzipFile(downloadedFile.toFile(), Paths.get(sdkPath));

                commandLineToolsPath = resolveCommandLineToolsPath(sdkPath);
            }
        }
        return commandLineToolsPath;
    }

    /**
     * Extract command line tools download URL for the current platform from the android studio download page
     * @return The command line tools download URL
     */
    private String getCommandLineToolsDownloadURL() throws IOException {
        Document document = getDocument();
        Element element = document.select(getDownloadModalSelector() + " a[data-action=download]").first();
        if(element != null) {
            return element.attr("href");
        }
        return null;
    }

    /**
     * Extract HTML licence terms for the current platform from the android studio download page
     * @return The command line tools download URL
     */
    private String getCommandLineToolsHTMLTerms() throws IOException {
        Document document = getDocument();
        Element element = document.select(getDownloadModalSelector() + " .sdk-terms").first();
        if(element != null) {
            return element.html();
        }
        return null;
    }

    /**
     * Find the correct path to cmdline-tools binaries.
     * In some versions of the Android SDK, cmdline-tools binaries are under the `latest` directory.
     * @param sdkPath The path to the Android SDK
     * @return The path to cmdline-tools binaries
     */
    private String resolveCommandLineToolsPath(String sdkPath) {
        List<String> pathsToCheck = Arrays.asList(
                "cmdline-tools/bin",
                "cmdline-tools/latest/bin"
        );
        for(String path : pathsToCheck) {
            if(Paths.get(sdkPath, path, "sdkmanager").toFile().exists()) {
                return path;
            }
        }
        return null;
    }

    /**
     * Retrieve the Android Studio download page document
     * @return The download page document
     */
    private Document getDocument() throws IOException {
        if(downloadPageDocument == null) {
            downloadPageDocument = Jsoup.connect(ANDROID_STUDIO_DOWNLOAD_PAGE).get();
        }
        return downloadPageDocument;
    }

    /**
     * Get command line tools download modal selector from the current platform
     * @return The download modal selector
     */
    private String getDownloadModalSelector() {
        String platform = "";
        if(SystemUtils.IS_OS_LINUX) {
            platform = "linux";
        } else if(SystemUtils.IS_OS_MAC) {
            platform = "mac";
        } else if(SystemUtils.IS_OS_WINDOWS) {
            platform = "win";
        }
        return "#sdk_" + platform + "_download";
    }
}
