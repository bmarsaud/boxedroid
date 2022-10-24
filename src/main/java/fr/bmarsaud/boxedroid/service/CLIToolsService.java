package fr.bmarsaud.boxedroid.service;

import org.apache.commons.lang3.SystemUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class CLIToolsService {
    private static final String ANDROID_STUDIO_DOWNLOAD_PAGE = "https://developer.android.com/studio";
    private Document downloadPageDocument;

    /**
     * Extract command line tools download URL for the current platform from the android studio download page
     * @return The command line tools download URL
     */
    public String getCommandLineToolsDownloadURL() throws IOException {
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
    public String getCommandLineToolsHTMLTerms() throws IOException {
        Document document = getDocument();
        Element element = document.select(getDownloadModalSelector() + " .sdk-terms").first();
        if(element != null) {
            return element.html();
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
