package com.demos;

import com.demos.lucene.manager.IndexManager;
import com.demos.lucene.constants.Constants;

import java.io.IOException;

public class MainIndexer {

    public static void main(String[] args) throws IOException {
        try {

            Constants.eOperation operation = Constants.eOperation.UPDATE_DOC;
            IndexManager.getInstance().initialize(Constants.INDEX_DIR_STANDARD, Constants.eAnalyzerType.STANDARD);

            switch (operation) {
                case ADD:
                    IndexManager.getInstance().createIndex();
                    break;
                case MERGE:
                    break;
                case ADD_DOC:
                    break;
                case DELETE_DOC:
                    break;
                case UPDATE_DOC:
                    IndexManager.getInstance().updateDocument();
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}