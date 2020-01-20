import org.apache.lucene.queryparser.classic.ParseException;

import java.io.*;

public class App {

    public static void main(String[] args) throws IOException, ParseException {

        LuceneHandler luceneHandler = new LuceneHandler();
        luceneHandler.getOrCreateIndexFile();
        luceneHandler.getReaderFile();
        luceneHandler.performSearchInsideIndex();

    }









}
