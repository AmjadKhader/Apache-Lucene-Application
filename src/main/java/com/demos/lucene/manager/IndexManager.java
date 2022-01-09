package com.demos.lucene.manager;

import com.demos.lucene.constants.Constants;
import com.demos.lucene.entites.MessageDocument;
import com.demos.lucene.factory.AnalyzerFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.demos.lucene.constants.Constants.*;

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

    public void setAnalyzerType(Constants.eAnalyzerType analyzerType) throws IOException {
        FSDirectory directory = DirectoryManager.getDirectory(analyzerType);
        try {
            indexWriter = new IndexWriter(directory, new IndexWriterConfig(AnalyzerFactory.getAnalyzer()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void createIndex() throws IOException {
        List<Document> documents = new ArrayList<>();

        Document document1 = createDocument(MessageDocument.builder().documentId("1").title("my father").description("My-father is COMING for the holidays to make cookies").build());
        Document document2 = createDocument(MessageDocument.builder().documentId("2").title("cookies").description("It takes an hour to make cookie in the holidays").build());
        Document document3 = createDocument(MessageDocument.builder().documentId("3").title("asking").description("What is your father doing?").build());
        Document document4 = createDocument(MessageDocument.builder().documentId("4").title("cooking cookies").description("It takes an hour to make cookie").build());
        Document document5 = createDocument(MessageDocument.builder().documentId("5").title("my father's cookies").description("My father makes awesome cookie").build());

        documents.add(document1);
        documents.add(document2);
        documents.add(document3);
        documents.add(document4);
        documents.add(document5);

        //Let's clean everything first
        indexWriter.deleteAll();

        indexWriter.addDocuments(documents);

        indexWriter.prepareCommit();
        indexWriter.commit();
    }

    public void deleteDocument(String term, String field) throws IOException {
        indexWriter.deleteDocuments(new Term(term, field));
        indexWriter.commit();
    }

    public void updateDocument(MessageDocument messageDocument) throws IOException {
        Document document = createDocument(messageDocument);

        indexWriter.updateDocument(new Term(ID, document.get(ID)), document);
        indexWriter.commit();
    }

    private Document createDocument(MessageDocument message) {
        Document document = new Document();

        document.add(new TextField(ID, message.getDocumentId(), Field.Store.YES));
        document.add(new TextField(TITLE, message.getTitle(), Field.Store.YES));
        document.add(new TextField(MESSAGE, message.getDescription(), Field.Store.YES)); // Tokenize -> will be analyzed

        return document;
    }

    public void addDocument(MessageDocument newMessage) throws IOException {
        Document document = createDocument(newMessage);

        indexWriter.addDocument(document);
        indexWriter.commit();
    }
}