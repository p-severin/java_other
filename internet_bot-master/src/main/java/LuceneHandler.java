import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LuceneHandler {

    private static final String START_URL_WEBSITE = "https://en.wikipedia.org/wiki/Python_(programming_language)";
    private static final int NUMBER_OF_PAGES_TO_DOWNLOAD = 10000;
    private static final String INDEX_DIRECTORY = "C:/Users/patry/Documents/IdeaProjects/webminingIndexingWebPages/index.lucene";
    private static final int NUMBER_OF_THREADS = 30;
    private final Logger logger = Logger.getLogger(getClass().getName());


    private StandardAnalyzer analyzer;
    private FSDirectory index;
    private IndexWriterConfig config;
    private IndexWriter writer;
    private IndexReader reader;
    private IndexSearcher searcher;
    private MoreLikeThis moreLikeThis;
    private boolean indexExists;

    LuceneHandler() throws IOException {
        analyzer = new StandardAnalyzer();
        index = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        config = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(index, config);
        indexExists = DirectoryReader.indexExists(index);
    }

    public void getOrCreateIndexFile() throws IOException, ParseException {
        if (indexExists == false){
            logger.log(Level.INFO, "Index directory does not exist - creating new one.");
            indexWebPages();

        } else{
            logger.log(Level.INFO, "Index directory already exists - proceeding with search queries.");
        }

        logger.log(Level.INFO, "Number of documents added: " + writer.numDocs());

        writer.close();

    }

    public void getReaderFile() throws IOException {
        reader = DirectoryReader.open(index);

        writeIndexUrlsToFile();
    }

    public void performSearchInsideIndex() throws ParseException, IOException {

        String querystr = getSearchQuery();
        searcher = new IndexSearcher(reader);

        Query q = new QueryParser("text", analyzer).parse(querystr);
        int hitsPerPage = 5;
        ScoreDoc[] hits = getQueryResults(q, hitsPerPage);

        moreLikeThis = new MoreLikeThis(reader);
        moreLikeThis.setFieldNames(new String[]{
                "index",
                "title",
                "text"
        });

        moreLikeThis.setAnalyzer(analyzer);
        Query similarDocumentQuery = moreLikeThis.like(hits[0].doc);
        ScoreDoc[] bestSimilarHits = getQueryResults(similarDocumentQuery, 2);


    }

    private ScoreDoc[] getQueryResults(Query q, int hitsPerPage) throws IOException {
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        System.out.println(String.format("Found %d hits :", hits.length));
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(String.format("%-2d. %-100s",i, d.get("title")));
        }
        return hits;
    }

    private String getSearchQuery() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the search query:");
        return scanner.nextLine();
    }

    private void indexWebPages() {

        String startURL = START_URL_WEBSITE;
        try {
            new Downloader(this, NUMBER_OF_PAGES_TO_DOWNLOAD).downloadPage(startURL);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        for (int i = 0; i<NUMBER_OF_THREADS; i++){
            executor.execute(new Downloader(this , NUMBER_OF_PAGES_TO_DOWNLOAD));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finished!");
    }

    private void writeIndexUrlsToFile() {

        Writer fileWriter = null;

        try {
            fileWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("list_of_urls.txt"), "utf-8"));

            for (int i = 0; i<reader.maxDoc(); i++){
                Document document = reader.document(i);
                String title = document.get("title");
                fileWriter.write(title+"\n");
            }
        } catch (IOException ex) {
            // report
        } finally {
            try {fileWriter.close();} catch (Exception ex) {/*ignore*/}
        }
    }

    public void addDocumentToLuceneIndex(String url, String htmlText) throws IOException {
        Document luceneDoc = new Document();
        luceneDoc.add(new StringField("index", url, Field.Store.YES));
        luceneDoc.add(new TextField("title", url, Field.Store.YES));
        luceneDoc.add(new TextField("text", htmlText, Field.Store.YES));
        writer.addDocument(luceneDoc);
    }


}
