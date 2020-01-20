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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.nodes.Element;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LuceneHandlerImpl implements LuceneHandler {

    private static final int NUMBER_OF_THREADS = 30;
    private static final int NUMBER_OF_PAGES_TO_DOWNLOAD = 20000;
    private static final String INDEX_DIRECTORY = "C:/Users/patry/Documents/IdeaProjects/sentimentAnalysis/index.lucene";
    private final Logger logger = Logger.getLogger(getClass().getName());

    private StandardAnalyzer analyzer;
    private Directory index;
    private IndexWriterConfig config;
    private IndexWriter writer;
    private IndexReader reader;
    private IndexSearcher searcher;
    private MoreLikeThis moreLikeThis;
    private boolean indexExists;
    private List<String> uniqueParagraphs;

    LuceneHandlerImpl() throws IOException {

        analyzer = new StandardAnalyzer();
        index = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        config = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(index, config);
        indexExists = DirectoryReader.indexExists(index);
        uniqueParagraphs = new ArrayList<>(50);

    }


    public void addDocumentToIndex(String URL, Element paragraph) throws IOException {

        Document luceneDoc = new Document();
        luceneDoc.add(new StringField("index", URL, Field.Store.YES));
//        luceneDoc.add(new TextField("title", URL, Field.Store.YES));
        luceneDoc.add(new TextField("text", paragraph.text(), Field.Store.YES));
        writer.addDocument(luceneDoc);

    }

    public void getReaderFile() throws IOException {
        reader = DirectoryReader.open(index);
//        writeIndexUrlsToFile();
//        writeParagraphsToFile();
    }

    public List<String> searchIndexForDocument() throws IOException, ParseException {

        String querystr = getSearchQuery();
        searcher = new IndexSearcher(reader);

        Query q = new QueryParser("text", analyzer).parse(querystr);
        int hitsPerPage = 1500;
        ScoreDoc[] hits = getQueryResults(q, hitsPerPage);




        System.out.println(String.format("Found %d hits :", hits.length));
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);

            String documentText = d.get("text");

//            System.out.println(String.format("%-2d. %-100s",i, documentText));


            if (!uniqueParagraphs.contains(documentText)){
                uniqueParagraphs.add(documentText);
            }
        }

        return uniqueParagraphs;


//        moreLikeThis = new MoreLikeThis(reader);
//        moreLikeThis.setFieldNames(new String[]{
//                "index",
//                "text"
//        });

//        moreLikeThis.setAnalyzer(analyzer);
//        Query similarDocumentQuery = moreLikeThis.like(hits[0].doc);
//        ScoreDoc[] bestSimilarHits = getQueryResults(similarDocumentQuery, 2);

    }

    private ScoreDoc[] getQueryResults(Query q, int hitsPerPage) throws IOException {
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;


        return hits;
    }

    private String getSearchQuery() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the search query:");
        return scanner.nextLine();
    }

    public void getOrCreateIndexFile() throws IOException {

        if (indexExists == false){
            logger.log(Level.INFO, "Index directory does not exist - creating new one.");
            indexWebPages();

        } else{
            logger.log(Level.INFO, "Index directory already exists - proceeding with search queries.");
        }

        logger.log(Level.INFO, "Number of documents added: " + writer.numDocs());

        writer.close();

    }

    @Override
    public void indexWebPages() {

        new DownloaderImpl(this, NUMBER_OF_PAGES_TO_DOWNLOAD).downloadPage(App.URL_FIRST_PAGE);

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        for (int i = 0; i<NUMBER_OF_THREADS; i++){
            executor.execute(new DownloaderImpl(this, NUMBER_OF_PAGES_TO_DOWNLOAD));
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
                String title = document.get("index");
                fileWriter.write(title+"\n");
            }
        } catch (IOException ex) {
            // report
        } finally {
            try {fileWriter.close();} catch (Exception ex) {/*ignore*/}
        }
    }

    private void writeParagraphsToFile(){

        Writer fileWriter = null;

        try {
            fileWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("paragraphs.txt"), "utf-8"));

            for (int i = 0; i<reader.maxDoc(); i++){
                Document document = reader.document(i);
                String title = document.get("text");
                fileWriter.write(title+"\n");
            }
        } catch (IOException ex) {
            // report
        } finally {
            try {fileWriter.close();} catch (Exception ex) {/*ignore*/}
        }
    }
}
