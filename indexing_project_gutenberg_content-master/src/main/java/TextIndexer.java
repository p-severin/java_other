import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextIndexer {

    List<File> queue = new ArrayList<File>();
    private int count = 0;
    final Logger log = Logger.getLogger(getClass().getName());


    public static void main(String[] args) throws IOException, ParseException {


        String unzipDirectoryPath = "D:/Nauka/UnzippedGutenbergProject";
        StandardAnalyzer analyzer = new StandardAnalyzer();
        FSDirectory index = FSDirectory.open(Paths.get("C:/Users/patry/Documents/IdeaProjects/webminingluceneappex6/index.lucene"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        boolean indexExists = DirectoryReader.indexExists(index);
        IndexWriter writer = new IndexWriter(index, config);
        TextIndexer textIndexer = new TextIndexer();

        if (indexExists == false){

            textIndexer.log.log(Level.INFO, "Index directory does not exist - creating new one.");
            textIndexer.indexFileOrDirectory(unzipDirectoryPath, writer);

        } else{
            textIndexer.log.log(Level.INFO, "Index directory already exists - proceeding with search queries.");
        }

        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the search query:");
        String querystr = scanner.nextLine();

        Query q = new QueryParser("title", analyzer).parse(querystr);

        int hitsPerPage = 100;

        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        System.out.println(String.format("Found %d hits :", hits.length));
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(String.format("%-2d. %-15s : %-50s",i, d.get("index"), d.get("title")));
        }

        reader.close();

    }

    private void addFiles(File file) {

        if (!file.exists()) {
            System.out.println(file + " does not exist.");
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                addFiles(f);
            }
        } else {
            String filename = file.getName().toLowerCase();
            if (filename.endsWith(".htm") || filename.endsWith(".html") ||
                    filename.endsWith(".xml") || filename.endsWith(".txt")) {
                queue.add(file);
            }
        }
    }

    public void indexFileOrDirectory(String fileName, IndexWriter writer) throws IOException {


        addFiles(new File(fileName));
        int originalNumDocs = writer.numDocs();
        int allFilesCount = queue.size();
        int resolution = 200;
        int totalSigns = (int) Math.floor(allFilesCount/resolution);
        float percentResult = 0.0f;



        for (File f : queue) {

            createProgressBar(allFilesCount, resolution, totalSigns);

            try{
                String wholeText = readTextFromFile(f);

                String titlePattern = "Title: (.*)\\s";
                String textPattern = "\\*{3}[^\\*]+\\*{3}\\s(.*)";

                Pattern rTitle = Pattern.compile(titlePattern);
                Pattern rText = Pattern.compile(textPattern, Pattern.DOTALL);

                Matcher mTitle = rTitle.matcher(wholeText);
                Matcher mText = rText.matcher(wholeText);

                String luceneTitle = null;
                String luceneText = null;

                if(mTitle.find()){
                    luceneTitle = mTitle.group(1);
                }
                if(mText.find()){
                    luceneText = mText.group(1);
                }

                Document doc = new Document();
                doc.add(new TextField("title", luceneTitle, Field.Store.YES));
                doc.add(new StringField("index", f.getName(), Field.Store.YES));
                doc.add(new TextField("text", luceneText, Field.Store.YES));

                writer.addDocument(doc);
            } catch (Exception e) {
            }

        }

        int newNumDocs = writer.numDocs();
        writer.close();

        System.out.println("");
        System.out.println("************************");
        System.out.println((newNumDocs - originalNumDocs) + " documents added.");
        System.out.println("************************");
        queue.clear();

    }

    private void createProgressBar(float allFilesCount, int resolution, int totalSigns) {
        float percentResult;
        count += 1;
        percentResult = (float)count/ allFilesCount *100;

        int currentNumberOfSigns = (int) Math.floor(count/resolution);
        StringBuilder progressBuilder = new StringBuilder();

        progressBuilder.append('\r');
        progressBuilder.append('<');
        for (int i=0; i<currentNumberOfSigns; i++){
            progressBuilder.append('=');
        }
        for (int i=currentNumberOfSigns; i<totalSigns; i++){
            progressBuilder.append('-');
        }
        progressBuilder.append('>');

        String progressBar = progressBuilder.toString();
        System.out.print(progressBar);
        System.out.print(String.format(" %.2f %%", percentResult));
    }

    private String readTextFromFile(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f.getAbsolutePath()));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while(line != null){
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();

        }
        br.close();
        return sb.toString();
    }

}
