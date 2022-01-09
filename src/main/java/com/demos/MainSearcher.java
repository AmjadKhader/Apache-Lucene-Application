package com.demos;

import com.demos.lucene.factory.AnalyzerFactory;
import com.demos.lucene.roles.QueryRole;
import com.demos.lucene.constants.Constants;
import com.demos.lucene.manager.QueryManager;
import com.demos.lucene.manager.SearcherManager;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;

import java.util.HashMap;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class MainSearcher {

    private static final int MAX_DOC_NUMBER = 10;

    public static void main(String[] args) throws Exception {

        //set analyzer type
        AnalyzerFactory.createAnalyzer(Constants.eAnalyzerType.CUSTOM);
        IndexSearcher searcher = SearcherManager.createSearcher();

        QueryManager.getInstance().setSearcher(searcher);

        //Search by ID
        log.println("Search by Id Results : ");
        QueryManager.getInstance().searchAndPrint(Constants.ID, MAX_DOC_NUMBER, "1");

        //Search by Title
        log.println("Search by Title Results : ");
        QueryManager.getInstance().searchAndPrint(Constants.TITLE, MAX_DOC_NUMBER, "asking");

        //Term Query
        log.println("Terms Results :: ");
        QueryManager.getInstance().searchAndPrint(Constants.MESSAGE, MAX_DOC_NUMBER, "coming"); // this word is used as an example of StopWords

        //Multiple Terms Query
        log.println("Multiple Terms Results :: ");
        QueryManager.getInstance().searchAndPrint(Constants.MESSAGE, MAX_DOC_NUMBER, "your man"); // father has been used as an example of Synonym

        //Wildcard Query
        log.println("Wildcard Results :: ");
        QueryManager.getInstance().searchAndPrint(Constants.MESSAGE, MAX_DOC_NUMBER, "f*r");

        //Prefix Query
        log.println("Prefix Results :: ");
        QueryManager.getInstance().searchAndPrint(Constants.MESSAGE, MAX_DOC_NUMBER, "cook*");

        //Fuzzy Query
        log.println("Fuzzy Results :: ");
        QueryManager.getInstance().searchIndexFuzzy(Constants.MESSAGE, MAX_DOC_NUMBER, "holida*", 2);

        //Phrase Query
        log.println("Phrase Results :: ");
        QueryManager.getInstance().searchIndexPhrase(Constants.MESSAGE, MAX_DOC_NUMBER, "awesome cookie");

        //Boolean Query
        Map<String, QueryRole> termFilterMap = new HashMap<>();
        termFilterMap.put("hour", QueryRole.builder().boosting(0.6).occur(BooleanClause.Occur.SHOULD).build());
        termFilterMap.put("doing", QueryRole.builder().boosting(0.7).occur(BooleanClause.Occur.SHOULD).build());

        log.println("Boolean Results :: ");
        QueryManager.getInstance().searchIndexBoolean(Constants.MESSAGE, MAX_DOC_NUMBER, termFilterMap);
    }
}