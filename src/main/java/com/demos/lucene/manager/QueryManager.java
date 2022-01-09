package com.demos.lucene.manager;

import com.demos.lucene.roles.QueryRole;
import com.demos.lucene.factory.AnalyzerFactory;
import com.demos.lucene.constants.Constants;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.TokenSources;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class QueryManager {

    /**
     * This Singleton class is responsible for creating ##IndexReader to read the index based on the analyzer
     * QueryManager exposes the following functionalities:
     *      - Execute basic queries (prefix, wildcard, term, and multi-term)
     *      - Execute fuzzy queries
     *      - Execute phrase queries
     *      - Execute boolean queries
     **/

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

    public void searchAndPrint(String searchField, int maxDocumentNum, String searchTerm)
            throws IOException, ParseException, InvalidTokenOffsetsException {

        print(searchIndex(searchField, maxDocumentNum, searchTerm), searcher,
                HighlighterManager.getInstance().getHighlighter(searchTerm, searchField));
    }

    public void searchIndexFuzzy(String searchField, int maxDocumentNum, String searchTerm, int degree)
            throws IOException, ParseException, InvalidTokenOffsetsException {
        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(searchField, searchTerm), degree);
        fuzzyQuery.setRewriteMethod(MultiTermQuery.CONSTANT_SCORE_REWRITE);

        print(doSearch(fuzzyQuery, maxDocumentNum), searcher,
                HighlighterManager.getInstance().getHighlighter(searchTerm, searchField));
    }

    public void searchIndexPhrase(String searchField, int maxDocumentNum, String searchTerm)
            throws IOException, ParseException, InvalidTokenOffsetsException {
        List<String> terms = Arrays.asList(searchTerm.split(" "));
        PhraseQuery.Builder builder = new PhraseQuery.Builder();
        terms.forEach((f -> builder.add(new Term(searchField, f))));

        print(doSearch(builder.build(), maxDocumentNum), searcher,
                HighlighterManager.getInstance().getHighlighter(searchTerm, searchField));
    }

    public void searchIndexBoolean(String searchField, int maxDocumentNum, Map<String, QueryRole> filterMap)
            throws IOException, ParseException, InvalidTokenOffsetsException {

        // Create and initialize boolean query
        final String[] highlightedText = {""};
        BooleanQuery.Builder booleanQuery = buildBooleanQuery(filterMap, highlightedText, new QueryParser(searchField, AnalyzerFactory.getAnalyzer()));

        print(doSearch(booleanQuery.build(), maxDocumentNum),
                searcher, HighlighterManager.getInstance().getHighlighter(highlightedText[0], searchField));
    }

    public TopDocs searchIndex(String searchField, int maxDocumentNum, String searchTerm)
            throws IOException, ParseException {
        QueryParser queryParser = new QueryParser(searchField, AnalyzerFactory.getAnalyzer());
        Query query = queryParser.parse(searchTerm);

        return doSearch(query, maxDocumentNum);
    }

    private TopDocs doSearch(Query query, int maxDocumentNum) throws IOException {
        return searcher.search(query, maxDocumentNum);
    }

    private BooleanQuery.Builder buildBooleanQuery(Map<String, QueryRole> filterMap, String[] highlightedText, QueryParser queryParser) {
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        filterMap.forEach((name, queryRole) -> {
            Query query = null;
            try {
                query = queryParser.parse(name);

                // If the rule is MUST OR SHOULD, the text will be highlighted.
                if (queryRole.getOccur().equals(BooleanClause.Occur.MUST)
                        || queryRole.getOccur().equals(BooleanClause.Occur.SHOULD)) {

                    highlightedText[0] += " " + name;
                }

                booleanQuery.add(new BoostQuery(query, (float) queryRole.getBoosting()), queryRole.getOccur());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        return booleanQuery;
    }

    private static void print(TopDocs searchResult, IndexSearcher searcher, Highlighter highlighter)
            throws IOException, InvalidTokenOffsetsException {

        for (ScoreDoc scoreDoc : searchResult.scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);

            log.println(document.get(Constants.ID));
            log.println(document.get(Constants.TITLE));
            log.println(document.get(Constants.MESSAGE));
            log.println("-------------------------------");

            //Create token stream
            TokenStream stream = TokenSources.getAnyTokenStream(SearcherManager.getReader(), scoreDoc.doc, Constants.MESSAGE, AnalyzerFactory.getAnalyzer());

            //Get highlighted text fragments
            String[] frags = highlighter.getBestFragments(stream, document.get(Constants.MESSAGE), document.get(Constants.MESSAGE).length());
            log.println(Arrays.toString(frags));
        }
        log.println("===========================================");
    }
}