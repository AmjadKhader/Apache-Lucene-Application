package com.demos.lucene.manager;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class SearcherManager {

    private SearcherManager() {
    }

    public static IndexSearcher createSearcher(String indexDir) throws IOException {
        File dir = new File(indexDir);
        if (dir.exists()) {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
            return new IndexSearcher(reader);
        } else throw new IOException("Input directory is not found");
    }
}