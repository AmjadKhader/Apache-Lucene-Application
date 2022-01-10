package com.text.search.lucene.manager;

import com.text.search.lucene.constants.Constants;
import com.text.search.lucene.dto.RequestDTO;
import com.text.search.lucene.entites.Message;
import com.text.search.lucene.factory.AnalyzerFactory;
import com.text.search.lucene.roles.QueryRole;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static com.text.search.lucene.constants.Constants.*;

public class QueryManager {

    /**
     * This Singleton class is responsible for creating ##IndexReader to read the index based on the analyzer
     * QueryManager exposes the following functionalities:
     * - Execute basic queries (prefix, wildcard, term, and multi-term)
     * - Execute fuzzy queries
     * - Execute phrase queries
     * - Execute boolean queries
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

    public List<Message> performSearch(String searchField, RequestDTO requestDTO) throws IOException, ParseException, InvalidTokenOffsetsException {
        setupIndexSearcher();

        switch (requestDTO.getQueryType()) {
            case "term":
                return streamValue(requestDTO, searchIndex(searchField, requestDTO.getTerm()));
            case "boolean":
                return streamValue(requestDTO, searchIndexBoolean(searchField, requestDTO));
            case "phrase":
                return streamValue(requestDTO, searchIndexPhrase(searchField, requestDTO.getTerm()));
            case "fuzzy":
                return streamValue(requestDTO, searchIndexFuzzy(searchField, requestDTO.getTerm(), requestDTO.getFuzzyDegree()));
            case "range":
            default:
                return new ArrayList<>();
        }
    }

    public void setupIndexSearcher() throws IOException {
        String directoryPath = FSDirectory.open(Paths.get(INDEX_DIR)).getDirectory().toString();

        File dir = new File(directoryPath);
        if (dir.exists()) {
            this.searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(directoryPath))));

        } else {
            throw new IOException("Input directory is not found");
        }
    }

    // -------------------------- Private -----------------------------------
    private List<Message> streamValue(RequestDTO requestDTO, ScoreDoc[] scoreDocs)
            throws IOException, ParseException, InvalidTokenOffsetsException {
        List<Message> returnedDoc = new ArrayList<>();

        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);

            Highlighter highlighter = HighlighterManager.getInstance().getHighlighter(requestDTO.getTerm(), requestDTO.getSearchField());

            //Create token stream
            TokenStream stream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), scoreDoc.doc, Constants.DESCRIPTION, AnalyzerFactory.getAnalyzer());

            //Get highlighted text fragments
            String[] highlighterBestFragments = highlighter.getBestFragments(stream, document.get(requestDTO.getSearchField()), document.get(requestDTO.getSearchField()).length());

            switch (requestDTO.getSearchField()) {
                case ID:
                    returnedDoc.add(Message.builder()
                            .documentId(highlighterBestFragments[0])
                            .title(document.get(TITLE))
                            .description(document.get(Constants.DESCRIPTION))
                            .build());
                    break;
                case TITLE:
                    returnedDoc.add(Message.builder()
                            .documentId(document.get(ID))
                            .title(highlighterBestFragments[0])
                            .description(document.get(Constants.DESCRIPTION))
                            .build());
                    break;
                case DESCRIPTION:
                    returnedDoc.add(Message.builder()
                            .documentId(document.get(ID))
                            .title(document.get(TITLE))
                            .description(highlighterBestFragments[0])
                            .build());
                    break;
                default:
            }
        }

        return returnedDoc;
    }

    private ScoreDoc[] searchIndexFuzzy(String searchField, String searchTerm, int degree)
            throws IOException {

        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(searchField, searchTerm), degree);
        fuzzyQuery.setRewriteMethod(MultiTermQuery.CONSTANT_SCORE_REWRITE);

        return doSearch(fuzzyQuery);
    }

    private ScoreDoc[] searchIndexPhrase(String searchField, String searchTerm)
            throws IOException {
        List<String> terms = Arrays.asList(searchTerm.split(" "));
        PhraseQuery.Builder builder = new PhraseQuery.Builder();
        terms.forEach((f -> builder.add(new Term(searchField, f))));

        return doSearch(builder.build());
    }

    private ScoreDoc[] searchIndexBoolean(String searchField, RequestDTO requestDTO) throws IOException {

        Map<String, QueryRole> filterMap = new HashMap<>();
        requestDTO.getRequestDTOList().forEach(query -> filterMap.put(query.getTerm(), QueryRole.builder().boosting(query.getBoost()).occur(convertToOccur(query.getOccur())).build()));

        // Create and initialize boolean query
        final String[] highlightedText = {""};
        BooleanQuery.Builder booleanQuery = buildBooleanQuery(filterMap, highlightedText, new QueryParser(searchField, AnalyzerFactory.getAnalyzer()));
        requestDTO.setTerm(highlightedText[0]);

        return doSearch(booleanQuery.build());
    }

    private BooleanClause.Occur convertToOccur(String occur) {
        switch (occur.toLowerCase(Locale.ROOT)) {
            case "must":
                return BooleanClause.Occur.MUST;
            case "must-not":
                return BooleanClause.Occur.MUST_NOT;
            case "should":
            default:
                return BooleanClause.Occur.SHOULD;
        }
    }

    private ScoreDoc[] searchIndex(String searchField, String searchTerm)
            throws IOException, ParseException {
        QueryParser queryParser = new QueryParser(searchField, AnalyzerFactory.getAnalyzer());
        Query query = queryParser.parse(searchTerm);

        return doSearch(query);
    }

    private ScoreDoc[] doSearch(Query query) throws IOException {
        return searcher.search(query, Constants.MAX_DOC_NUMBER).scoreDocs;
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
}