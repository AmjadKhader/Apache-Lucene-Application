package com.demos.lucene.manager;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.nio.file.Paths;

public class SearcherManager {

    private SearcherManager() {
    }

    public static IndexSearcher createSearcher(String indexDir) throws IOException {
        // todo: make sure that indexDir exists
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        IndexReader reader = DirectoryReader.open(dir);
        return new IndexSearcher(reader);
    }
}