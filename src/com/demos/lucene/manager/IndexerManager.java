package com.demos.lucene.manager;

import com.demos.lucene.factory.AnalyzerFactory;
import com.demos.lucene.constants.Constants;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class IndexerManager {

    private IndexerManager() {
    }

    public static IndexWriter createIndex(String directoryStr, Constants.eAnalyzerType analyzerType) throws IOException {
        FSDirectory directory = FSDirectory.open(Paths.get(directoryStr));
        try {
            return new IndexWriter(directory, new IndexWriterConfig(AnalyzerFactory.createAnalyzer(analyzerType)));

        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

}