import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public interface Downloader {

    void downloadPage(String URL) throws URISyntaxException;
    void addLinksToGlobalVariable(Elements currentLinks) throws MalformedURLException, UnknownHostException, URISyntaxException;

}


