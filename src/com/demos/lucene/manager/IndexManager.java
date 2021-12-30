package com.demos.lucene.manager;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriterConfig;
import com.demos.lucene.factory.AnalyzerFactory;
import org.apache.lucene.search.IndexSearcher;
import com.demos.lucene.constants.Constants;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.demos.lucene.constants.Constants.*;

public class IndexManager {

    private static IndexManager instance = null;
    private IndexWriter indexWriter = null;

    private IndexManager() {
    }

    public static IndexManager getInstance() {
        if (Objects.isNull(instance)) {
            instance = new IndexManager();
        }
        return instance;
    }

    public void initialize(String directoryStr, Constants.eAnalyzerType analyzerType) throws IOException {
        FSDirectory directory = FSDirectory.open(Paths.get(directoryStr)); // this should be open.
        try {
            indexWriter = new IndexWriter(directory, new IndexWriterConfig(AnalyzerFactory.createAnalyzer(analyzerType)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void createIndex() throws IOException {
        List<Document> documents = new ArrayList<>();
        Document document1 = createDocument(1, "my father", "My father is coming for the holidays to make cookies");
        Document document2 = createDocument(2, "cookies", "It takes an hour to make cookie in the holidays");
        Document document3 = createDocument(3, "asking", "What is your father doing?");
        Document document4 = createDocument(4, "cooking cookies", "It takes an hour to make cookie");
        Document document5 = createDocument(5, "my father's cookies", "My father makes awesome cookie");

        documents.add(document1);
        documents.add(document2);
        documents.add(document3);
        documents.add(document4);
        documents.add(document5);

        //Let's clean everything first
        indexWriter.deleteAll();

        indexWriter.addDocuments(documents);
        indexWriter.commit(); // try with resource will close the writer.
    }

    public void updateDocument() throws Exception {
        Document document = null;
        IndexSearcher searcher = SearcherManager.createSearcher(Constants.INDEX_DIR_STANDARD);
        QueryManager.getInstance().setSearcher(searcher);

        for (ScoreDoc scoreDoc : QueryManager.getInstance().searchIndex(Constants.TITLE, 1, "asking").scoreDocs) {
            document = searcher.doc(scoreDoc.doc);
        }

        //todo: debug it more.
        indexWriter.updateDocument(new Term(Constants.TITLE, "Amjad"), document);
        indexWriter.commit(); // try with resource will close the writer.
    }

    private Document createDocument(Integer id, String title, String message) {
        Document document = new Document();

        document.add(new StringField(ID, id.toString(), Field.Store.YES));
        document.add(new StringField(TITLE, title, Field.Store.YES));
        document.add(new TextField(MESSAGE, message, Field.Store.YES)); // Tokenize -> will be analyzed

        return document;
    }

}