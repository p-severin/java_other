import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloaderImpl implements Downloader, Runnable {

    private LuceneHandlerImpl luceneHandler;
    private static AtomicInteger atomicInteger = new AtomicInteger(1);
    private static Set<String> uniqueSites = new HashSet<>();
    private static BlockingQueue<String> siteLinks = new LinkedBlockingDeque<>();
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final int numberOfPagesToDownload;

    public DownloaderImpl(LuceneHandlerImpl luceneHandler, int numberOfPagesToDownload){
        this.luceneHandler = luceneHandler;
        this.numberOfPagesToDownload = numberOfPagesToDownload;
    }

    public void downloadPage(String URL){

        Elements currentLinks = null;
        Elements paragraphs = null;

        try{

            Document doc = Jsoup.connect(URL).timeout(10*1000).get();
//            String htmlText = Jsoup.parse(doc.toString()).select("body").text().toLowerCase();
            currentLinks = doc.select("a[href]");
            paragraphs = doc.select("p");
            addLinksToGlobalVariable(currentLinks);

            for (Element paragraph : paragraphs){
                luceneHandler.addDocumentToIndex(URL, paragraph);
            }


            atomicInteger.incrementAndGet();


        } catch (UnsupportedMimeTypeException e){
            e.printStackTrace();
        } catch (HttpStatusException e){
            e.printStackTrace();
        } catch (SocketTimeoutException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addLinksToGlobalVariable(Elements currentLinks) throws MalformedURLException, UnknownHostException{

        for (Element element : currentLinks){
            String link = element.attr("href");

            link = App.URL_FIRST_PAGE_BASE + link;

            if (link.startsWith(App.URL_FIRST_PAGE_BASE + "/forums")){
                InetAddress websiteIPFirstPage = InetAddress.getByName(new URL(link).getHost());
                String urlSiteIP = websiteIPFirstPage.getHostAddress();

                if (!uniqueSites.contains(link) && urlSiteIP.equals(App.firstPageIP)){
                    siteLinks.add(link);
                    uniqueSites.add(link);
                }
            }
        }


    }

    public void run() {
        while (!siteLinks.isEmpty() && atomicInteger.intValue() < numberOfPagesToDownload) {
            String url = siteLinks.poll();
            downloadPage(url);
            System.out.println(atomicInteger.intValue());
        }
    }
}
