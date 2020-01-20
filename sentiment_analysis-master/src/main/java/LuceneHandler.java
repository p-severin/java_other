import org.apache.lucene.queryparser.classic.ParseException;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

public interface LuceneHandler {

    void addDocumentToIndex(String URL, Element paragraph) throws IOException;
    List<String> searchIndexForDocument() throws IOException, ParseException;
    void getOrCreateIndexFile() throws IOException;
    void indexWebPages();

}
