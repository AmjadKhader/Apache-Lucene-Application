package com.demos;

import com.demos.lucene.manager.DocumentManager;
import com.demos.lucene.manager.IndexerManager;
import com.demos.lucene.constants.Constants;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class MainIndexer {

    public static void main(String[] args) throws IOException {
        try (IndexWriter writer = IndexerManager.createIndex(Constants.INDEX_DIR_STANDARD, Constants.eAnalyzerType.STANDARD)) {
            assert writer != null;

            List<Document> documents = new ArrayList<>();
            Document document1 = DocumentManager.createDocument(1,
                    "my father", "My father is coming for the holidays to make cookies");
            Document document2 = DocumentManager.createDocument(2,
                    "cookies", "It takes an hour to make cookie in the holidays");
            Document document3 = DocumentManager.createDocument(3,
                    "asking", "What is your father doing?");
            Document document4 = DocumentManager.createDocument(4,
                    "cooking cookies", "It takes an hour to make cookie");
            Document document5 = DocumentManager.createDocument(5,
                    "my father's cookies", "My father makes awesome cookie");

            documents.add(document1);
            documents.add(document2);
            documents.add(document3);
            documents.add(document4);
            documents.add(document5);

            //Let's clean everything first
            writer.deleteAll();

            writer.addDocuments(documents);
            writer.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}