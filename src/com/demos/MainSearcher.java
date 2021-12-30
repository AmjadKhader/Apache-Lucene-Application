package com.demos;

import com.demos.lucene.manager.SearcherManager;
import com.demos.lucene.manager.SearchManager;
import com.demos.lucene.constants.Constants;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class MainSearcher {

    private static final int MAX_DOC_NUMBER = 10;

    public static void main(String[] args) throws Exception {
        IndexSearcher searcher = SearcherManager.createSearcher(Constants.INDEX_DIR_STANDARD);

        //set analyzer type
        SearchManager.getInstance().setAnalyzer(Constants.eAnalyzerType.STANDARD);
        SearchManager.getInstance().setSearcher(searcher);

        //Search by ID
        TopDocs searchResult = SearchManager.getInstance().searchIndex(Constants.ID, MAX_DOC_NUMBER, "1");
        log.println("Search by Id Results : " + searchResult.totalHits);

        print(searchResult, searcher);

        //Search by Title
        searchResult = SearchManager.getInstance().searchIndex(Constants.TITLE, MAX_DOC_NUMBER, "asking");
        log.println("Search by Title Results : " + searchResult.totalHits);

        print(searchResult, searcher);

        //Term Query
        searchResult = SearchManager.getInstance().searchIndex(Constants.MESSAGE, MAX_DOC_NUMBER, "cookies");
        log.println("Terms Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Multiple Terms Query
        searchResult = SearchManager.getInstance().searchIndex(Constants.MESSAGE, MAX_DOC_NUMBER, "your father");
        log.println("Multiple Terms Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Wildcard Query
        searchResult = SearchManager.getInstance().searchIndex(Constants.MESSAGE, MAX_DOC_NUMBER, "f*r");
        log.println("Wildcard Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Prefix Query
        searchResult = SearchManager.getInstance().searchIndex(Constants.MESSAGE, MAX_DOC_NUMBER, "cook*");
        log.println("Prefix Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Fuzzy Query
        searchResult = SearchManager.getInstance().searchIndexFuzzy(Constants.MESSAGE, MAX_DOC_NUMBER, "holida", 2);
        log.println("Fuzzy Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Phrase Query
        searchResult = SearchManager.getInstance().searchIndexPhrase(Constants.MESSAGE, MAX_DOC_NUMBER, "awesome cookie");
        log.println("Phrase Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Boolean Query
        searchResult = SearchManager.getInstance().searchIndexBoolean(Constants.MESSAGE, MAX_DOC_NUMBER,
                "cook*", "holiday*");
        log.println("Boolean Results :: " + searchResult.totalHits);

        print(searchResult, searcher);
    }

    private static void print(TopDocs searchResult, IndexSearcher searcher) throws IOException {
        for (ScoreDoc scoreDoc : searchResult.scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);

            log.println(document.get(Constants.ID));
            log.println(document.get(Constants.TITLE));
            log.println(document.get(Constants.MESSAGE));
        }
        log.println("-------------------------------");
    }
}