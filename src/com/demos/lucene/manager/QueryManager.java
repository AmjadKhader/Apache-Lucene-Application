package com.demos.lucene.manager;

import com.demos.lucene.factory.AnalyzerFactory;
import com.demos.lucene.constants.Constants;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QueryManager {

    private Constants.eAnalyzerType analyzer = Constants.eAnalyzerType.STANDARD;
    private static QueryManager instance = null;
    private IndexSearcher searcher = null;

    private QueryManager() {
    }

    public static QueryManager getInstance() {
        if (Objects.isNull(instance)) {
            instance = new QueryManager();
        }
        return instance;
    }

    public void setSearcher(IndexSearcher searcher) {
        this.searcher = searcher;
    }

    public void setAnalyzer(Constants.eAnalyzerType analyzerType) {
        this.analyzer = analyzerType;
    }

    public TopDocs searchIndex(String searchTerm, int maxDocumentNum, String field) throws IOException, ParseException {
        QueryParser queryParser = new QueryParser(searchTerm, AnalyzerFactory.createAnalyzer(analyzer));
        Query query = queryParser.parse(field);

        return searcher.search(query, maxDocumentNum);
    }

    public TopDocs searchIndexFuzzy(String searchTerm, int maxDocumentNum, String field, int degree) throws IOException {
        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(searchTerm, field), degree);
        fuzzyQuery.setRewriteMethod(MultiTermQuery.CONSTANT_SCORE_REWRITE);

        return searcher.search(fuzzyQuery, maxDocumentNum);
    }

    public TopDocs searchIndexPhrase(String searchTerm, int maxDocumentNum, String field) throws IOException {
        List<String> terms = Arrays.asList(field.split(" "));
        PhraseQuery.Builder builder = new PhraseQuery.Builder();
        terms.forEach((f -> builder.add(new Term(searchTerm, f))));

        return searcher.search(builder.build(), maxDocumentNum);
    }

    public TopDocs searchIndexBoolean(String searchTerm, int maxDocumentNum, Map<String, BooleanClause.Occur> filterMap) throws IOException {
        //create terms to search
        QueryParser queryParser = new QueryParser(searchTerm, AnalyzerFactory.createAnalyzer(analyzer));
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        filterMap.forEach((name, occur) -> {
            Query query = null;
            try {
                query = queryParser.parse(name);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            booleanQuery.add(query, occur);
        });

        return searcher.search(booleanQuery.build(), maxDocumentNum);
    }
}