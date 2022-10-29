package fr.bmarsaud.boxedroid.service;

import org.apache.commons.lang3.SystemUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CmdlineToolsService {
    private static final String ANDROID_STUDIO_DOWNLOAD_PAGE = "https://developer.android.com/studio";

    private final Logger logger = LoggerFactory.getLogger(CmdlineToolsService.class);
    private final String sdkPath;
    private Document downloadPageDocument;

    public CmdlineToolsService(String sdkPath) {
        this.sdkPath = sdkPath;
    }

    /**
     * Checks if cmdline-tools are available from the SDK path.
     * If not, download them from the Google website after asking the user to accept the agreements
     * terms.
     * @return The path to the cmdline-tools binaries
     */
    public String acquireCmdlineTools() throws IOException {
        String cmdlineToolsPath = resolveCmdlineToolsPath(sdkPath);
        if(cmdlineToolsPath == null) {
            logger.info("cmdline-tools not found in " + sdkPath);
            logger.info("Do you want to download it from Google website? (y/n)");

            String answer = IOUtils.readFromStandardInput();
            if (!"y".equalsIgnoreCase(answer)) {
               System.exit(1);
            }

            String terms = getCmdlineToolsHTMLTerms();
            if(terms != null) {
                logger.info(terms.replaceAll("<br \\>", "\n"));
                logger.info("I have read and agree with the above terms and conditions (y/n)");

                answer = IOUtils.readFromStandardInput();
                if (!"y".equalsIgnoreCase(answer)) {
                    System.exit(1);
                }

                String downloadUrl = getCmdlineToolsDownloadURL();
                Path downloadedFile = Paths.get(System.getProperty("java.io.tmpdir"), "cmdline-tools.zip");
                IOUtils.downloadFile(downloadUrl, downloadedFile);
                IOUtils.unzipFile(downloadedFile.toFile(), Paths.get(sdkPath));

                cmdlineToolsPath = resolveCmdlineToolsPath(sdkPath);
            }
        }
        return cmdlineToolsPath;
    }

    /**
     * Extract cmdline-tools download URL for the current platform from the android studio download page
     * @return The cmdline-tools download URL
     */
    private String getCmdlineToolsDownloadURL() throws IOException {
        Document document = getDocument();
        Element element = document.select(getDownloadModalSelector() + " a[data-action=download]").first();
        if(element != null) {
            return element.attr("href");
        }
        return null;
    }

    /**
     * Extract HTML licence terms for the current platform from the android studio download page
     * @return The cmdline-tools download URL
     */
    private String getCmdlineToolsHTMLTerms() throws IOException {
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
    private String resolveCmdlineToolsPath(String sdkPath) {
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
     * Get cmdline-tools download modal selector from the current platform
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
