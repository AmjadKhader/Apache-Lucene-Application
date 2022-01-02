package com.demos;

import com.demos.lucene.manager.SearcherManager;
import com.demos.lucene.manager.QueryManager;
import com.demos.lucene.constants.Constants;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class MainSearcher {

    private static final int MAX_DOC_NUMBER = 10;

    public static void main(String[] args) throws Exception {
        IndexSearcher searcher = SearcherManager.createSearcher(Constants.INDEX_DIR_STANDARD);

        //set analyzer type
        QueryManager.getInstance().setAnalyzer(Constants.eAnalyzerType.STANDARD);
        QueryManager.getInstance().setSearcher(searcher);

        //Search by ID
        TopDocs searchResult = QueryManager.getInstance().searchIndex(Constants.ID, MAX_DOC_NUMBER, "1");
        log.println("Search by Id Results : " + searchResult.totalHits);

        print(searchResult, searcher);

        //Search by Title
        searchResult = QueryManager.getInstance().searchIndex(Constants.TITLE, MAX_DOC_NUMBER, "asking");
        log.println("Search by Title Results : " + searchResult.totalHits);

        print(searchResult, searcher);

        //Term Query
        searchResult = QueryManager.getInstance().searchIndex(Constants.MESSAGE, MAX_DOC_NUMBER, "cookies");
        log.println("Terms Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Multiple Terms Query
        searchResult = QueryManager.getInstance().searchIndex(Constants.MESSAGE, MAX_DOC_NUMBER, "your father");
        log.println("Multiple Terms Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Wildcard Query
        searchResult = QueryManager.getInstance().searchIndex(Constants.MESSAGE, MAX_DOC_NUMBER, "f*r");
        log.println("Wildcard Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Prefix Query
        searchResult = QueryManager.getInstance().searchIndex(Constants.MESSAGE, MAX_DOC_NUMBER, "cook*");
        log.println("Prefix Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Fuzzy Query
        searchResult = QueryManager.getInstance().searchIndexFuzzy(Constants.MESSAGE, MAX_DOC_NUMBER, "holida", 2);
        log.println("Fuzzy Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Phrase Query
        searchResult = QueryManager.getInstance().searchIndexPhrase(Constants.MESSAGE, MAX_DOC_NUMBER, "awesome cookie");
        log.println("Phrase Results :: " + searchResult.totalHits);

        print(searchResult, searcher);

        //Boolean Query
        Map<String, BooleanClause.Occur> filterMap = new HashMap<>();
        filterMap.put("cook*", BooleanClause.Occur.MUST);
        filterMap.put("holiday*", BooleanClause.Occur.MUST_NOT);
        filterMap.put("f*r", BooleanClause.Occur.MUST_NOT);

        searchResult = QueryManager.getInstance().searchIndexBoolean(Constants.MESSAGE, MAX_DOC_NUMBER, filterMap);
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