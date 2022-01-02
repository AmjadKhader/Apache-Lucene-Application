package com.demos;

import com.demos.lucene.manager.IndexManager;
import com.demos.lucene.constants.Constants;

import static com.demos.lucene.constants.Constants.ID;

public class MainIndexer {

    public static void main(String[] args) {
        try {

            Constants.eOperation operation = Constants.eOperation.ADD;
            IndexManager.getInstance().initialize(Constants.INDEX_DIR_STANDARD, Constants.eAnalyzerType.STANDARD);

            switch (operation) {
                case ADD:
                    IndexManager.getInstance().createIndex();
                    break;
                case DELETE_DOC:
                    IndexManager.getInstance().deleteDocument("1");
                    break;
                case UPDATE_DOC:
                    IndexManager.getInstance().updateDocument(ID, "asking", "amjad");
                    break;
                case ADD_DOC:
                    IndexManager.getInstance().addDocument("6", "custom",
                            "this message is to test cookies and holiday indexes for my father");
                    break;
                default:
                    throw new RuntimeException("Wrong input");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}