import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Downloader implements Runnable{

    private static BlockingQueue<String> siteLinks = new LinkedBlockingDeque<>();
    private static AtomicInteger atomicInteger = new AtomicInteger(1);
    private static Set<String> uniqueSites = new HashSet<>();
    private static LuceneHandler luceneHandler;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final int numberOfPagesToDownload;

    public Downloader(LuceneHandler luceneHandler, int numberOfPagesToDownload) {

        this.luceneHandler = luceneHandler;
        this.numberOfPagesToDownload = numberOfPagesToDownload;

    }

    public void downloadPage(String url) throws IOException {

        Elements currentLinks = null;

        try{

            Document doc = Jsoup.connect(url).get();
            String htmlText = Jsoup.parse(doc.toString()).select("body").text().toLowerCase();
            currentLinks = doc.select("a[href]");
            addLinksToGlobalVariable(currentLinks);
            luceneHandler.addDocumentToLuceneIndex(url, htmlText);
            atomicInteger.incrementAndGet();


        } catch (UnsupportedMimeTypeException e){
            logger.log(Level.WARNING, "Cannot download: " + url);
        } catch (HttpStatusException e){
            logger.log(Level.WARNING, "Cannot download: " + url);
        } catch (SocketTimeoutException e){
            logger.log(Level.WARNING, "Cannot download: " + url);
        }
    }

    @Override
    public void run() {
        while (atomicInteger.intValue() < numberOfPagesToDownload) {
            try {
                String url = siteLinks.poll();
                downloadPage(url);
                System.out.println(atomicInteger.intValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addLinksToGlobalVariable(Elements currentLinks) {

        for (Element element : currentLinks){
            String urlSite = element.attr("href");

            if (urlSite.startsWith("http") && !uniqueSites.contains(urlSite)) {
                siteLinks.add(urlSite);
                uniqueSites.add(urlSite);
            }
        }
    }
}
