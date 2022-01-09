package com.demos.lucene.manager;

import com.demos.lucene.factory.AnalyzerFactory;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.*;

import java.io.IOException;
import java.util.Objects;

public class HighlighterManager {

    /**
     * This Singleton class is responsible for creating and retrieving Highlighter for given word.
     **/

    private static HighlighterManager instance;

    private HighlighterManager() {
    }

    public static HighlighterManager getInstance() {
        if (Objects.isNull(instance)) {
            instance = new HighlighterManager();
        }
        return instance;
    }

    public Highlighter getHighlighter(String wordToHighlight, String searchField)
            throws ParseException, IOException {

        Query query = new QueryParser(searchField, AnalyzerFactory.getAnalyzer()).parse(wordToHighlight);
        QueryScorer scorer = new QueryScorer(query);

        //used to markup highlighted terms found in the best sections of a text
        Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter(), scorer);

        //It breaks text up into same-size texts but does not split up spans
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 10);

        //set fragmented to highlighter
        highlighter.setTextFragmenter(fragmenter);

        return highlighter;
    }
}