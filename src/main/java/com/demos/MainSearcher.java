package com.demos;

import com.demos.lucene.constants.Constants;
import com.demos.lucene.factory.AnalyzerFactory;
import com.demos.lucene.manager.QueryManager;
import com.demos.lucene.manager.SearcherManager;
import com.demos.lucene.roles.QueryRole;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;

import java.util.HashMap;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class MainSearcher {

    public static void main(String[] args) throws Exception {

        //set analyzer type
        AnalyzerFactory.createAnalyzer(Constants.eAnalyzerType.CUSTOM);
        IndexSearcher searcher = SearcherManager.createSearcher();

        QueryManager.getInstance().setSearcher(searcher);

        //Search by ID
        log.println("Search by Id Results : ");
        QueryManager.getInstance().searchAndPrint(Constants.ID, "1",false);

        //Search by Title
        log.println("Search by Title Results : ");
        QueryManager.getInstance().searchAndPrint(Constants.TITLE, "asking",false);

        //Term Query
        log.println("Terms Results :: ");
        QueryManager.getInstance().searchAndPrint(Constants.MESSAGE, "coming",true); // this word is used as an example of StopWords

        //Multiple Terms Query
        log.println("Multiple Terms Results :: ");
        QueryManager.getInstance().searchAndPrint(Constants.MESSAGE, "your man",true); // father has been used as an example of Synonym

        //Wildcard Query
        log.println("Wildcard Results :: ");
        QueryManager.getInstance().searchAndPrint(Constants.MESSAGE, "f*r",true);

        //Prefix Query
        log.println("Prefix Results :: ");
        QueryManager.getInstance().searchAndPrint(Constants.MESSAGE, "cook*",true);

        //Fuzzy Query
        log.println("Fuzzy Results :: ");
        QueryManager.getInstance().searchIndexFuzzy(Constants.MESSAGE, "holida*", 2);

        //Phrase Query
        log.println("Phrase Results :: ");
        QueryManager.getInstance().searchIndexPhrase(Constants.MESSAGE, "awesome cookie");

        //Boolean Query
        Map<String, QueryRole> termFilterMap = new HashMap<>();
        termFilterMap.put("hour", QueryRole.builder().boosting(0.6).occur(BooleanClause.Occur.SHOULD).build());
        termFilterMap.put("doing", QueryRole.builder().boosting(0.7).occur(BooleanClause.Occur.SHOULD).build());

        log.println("Boolean Results :: ");
        QueryManager.getInstance().searchIndexBoolean(Constants.MESSAGE, termFilterMap);
    }
}