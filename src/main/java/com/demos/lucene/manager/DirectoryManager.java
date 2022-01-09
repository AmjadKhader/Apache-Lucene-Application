package com.demos.lucene.manager;

import com.demos.lucene.constants.Constants;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class DirectoryManager {

    /**
     * This Singleton class is responsible for creating and retrieving Directory based on the analyzer
     * directory cannot be changed after it has been set.
    **/

    private DirectoryManager() {
    }

    private static FSDirectory directory = null;

    public static FSDirectory getDirectory(Constants.eAnalyzerType analyzerType) throws IOException {

        if (!Objects.isNull(directory)) {
            return directory; // The directory cannot be changed after it has been set..
        }

        switch (analyzerType) {
            case CUSTOM:
                directory = FSDirectory.open(Paths.get(Constants.INDEX_DIR_CUSTOM));
                break;
            case STANDARD:
                directory = FSDirectory.open(Paths.get(Constants.INDEX_DIR_STANDARD));
                break;
            case SIMPLE:
                directory = FSDirectory.open(Paths.get(Constants.INDEX_DIR_SIMPLE));
                break;
            case WHITE_SPACE:
                directory = FSDirectory.open(Paths.get(Constants.INDEX_DIR_WHITE));
                break;
        }
        return directory;
    }
}
