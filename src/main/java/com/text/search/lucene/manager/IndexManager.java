package com.text.search.lucene.manager;

import com.text.search.lucene.constants.Constants;
import com.text.search.lucene.entites.Message;
import com.text.search.lucene.factory.AnalyzerFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import static com.text.search.lucene.constants.Constants.INDEX_DIR;
import static com.text.search.lucene.constants.Constants.analyzerType;

public class IndexManager {

    /**
     * This Singleton class manages the index.
     * IndexManager is responsible for:
     * - Creating new index.
     * - Add document to index.
     * - Delete document from index.
     * - Update document in index by field.
     **/

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

    public void setupIndexWriter() throws IOException {
        FSDirectory directory = FSDirectory.open(Paths.get(INDEX_DIR));
        try {
            indexWriter = new IndexWriter(directory, new IndexWriterConfig(AnalyzerFactory.createAnalyzer(analyzerType)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void deleteDocument(String term, String field) throws IOException {
        setupIndexWriter(); // open connection

        indexWriter.deleteDocuments(new Term(term, field));
        indexWriter.commit();
        indexWriter.close();
    }

    public void updateDocument(Message messageDocument) throws IOException {
        setupIndexWriter(); // open connection

        Document document = createDocument(messageDocument);

        indexWriter.updateDocument(new Term(Constants.ID, document.get(Constants.ID)), document);
        indexWriter.commit();
        indexWriter.close();
    }

    private Document createDocument(Message message) throws IOException {
        setupIndexWriter(); // open connection

        Document document = new Document();

        document.add(new TextField(Constants.ID, message.getDocumentId(), Field.Store.YES));
        document.add(new TextField(Constants.TITLE, message.getTitle(), Field.Store.YES));
        document.add(new TextField(Constants.DESCRIPTION, message.getDescription(), Field.Store.YES)); // Tokenize -> will be analyzed

        return document;
    }

    public void addDocument(Message newMessage) throws IOException {
        setupIndexWriter(); // open connection

        Document document = createDocument(newMessage);

        indexWriter.addDocument(document);
        indexWriter.commit();
        indexWriter.close();
    }
}