package com.demos.lucene.manager;

import com.demos.lucene.factory.AnalyzerFactory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class SearcherManager {

    /**
     * This Singleton class is responsible for creating and ##IndexReader to read the index based on the analyzer
     **/

    private static IndexReader reader = null;

    private SearcherManager() {
    }

    public static IndexSearcher createSearcher() throws IOException {
        String directoryPath = DirectoryManager.getDirectory(AnalyzerFactory.getAnalyzerType()).getDirectory().toString();

        File dir = new File(directoryPath);
        if (dir.exists()) {

            reader = DirectoryReader.open(FSDirectory.open(Paths.get(directoryPath)));
            return new IndexSearcher(reader);

        } else {
            throw new IOException("Input directory is not found");
        }
    }

    public static IndexReader getReader() {
        return reader;
    }
}