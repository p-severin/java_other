import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.List;

public class App {

    public static final String URL_FIRST_PAGE_BASE = "https://www.cnet.com";
    public static final String URL_FIRST_PAGE_APPEND = "/forums/laptops";
    public static final String URL_FIRST_PAGE = URL_FIRST_PAGE_BASE + URL_FIRST_PAGE_APPEND;
    public static String firstPageIP;

    public static void main(String[] args) throws IOException, ParseException, UnirestException {

        LuceneHandlerImpl luceneHandler = new LuceneHandlerImpl();

        InetAddress websiteIPFirstPage = InetAddress.getByName(new URL(URL_FIRST_PAGE).getHost());
        firstPageIP = websiteIPFirstPage.getHostAddress();

        luceneHandler.getOrCreateIndexFile();
        luceneHandler.getReaderFile();
        List<String> searchResults = luceneHandler.searchIndexForDocument();

        ApiHandler apiHandler = new ApiHandler(searchResults);
        apiHandler.sendRequestForSentimentAnalysis();








    }
}
