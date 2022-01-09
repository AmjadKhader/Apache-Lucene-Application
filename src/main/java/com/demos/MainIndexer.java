package com.demos;

import com.demos.lucene.constants.Constants;
import com.demos.lucene.entites.MessageDocument;
import com.demos.lucene.factory.AnalyzerFactory;
import com.demos.lucene.manager.IndexManager;

import java.nio.file.FileSystemNotFoundException;

public class MainIndexer {

    public static void main(String[] args) {
        try {

            Constants.eOperation operation = Constants.eOperation.ADD;
            AnalyzerFactory.createAnalyzer(Constants.eAnalyzerType.CUSTOM);

            switch (operation) {
                case ADD:
                    IndexManager.getInstance().createIndex();
                    break;
                case DELETE_DOC:
                    IndexManager.getInstance().deleteDocument(Constants.ID, "1");
                    break;
                case UPDATE_DOC:
                    IndexManager.getInstance().updateDocument(MessageDocument.builder()
                            .documentId("3")
                            .title("amjad")
                            .description("What is your father doing?")
                            .build());
                    break;
                case ADD_DOC:
                    IndexManager.getInstance().addDocument(MessageDocument.builder()
                            .documentId("6")
                            .title("custom")
                            .description("this message is to test cookies and holiday indexes for my father")
                            .build());
                    break;
                default:
                    throw new FileSystemNotFoundException("Wrong input");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}