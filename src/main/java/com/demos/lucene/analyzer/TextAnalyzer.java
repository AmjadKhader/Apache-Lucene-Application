package com.demos.lucene.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;

public class TextAnalyzer extends Analyzer {

//    @Override
//    protected TokenStreamComponents createComponents(String field) {
//        final Tokenizer source = new WhitespaceTokenizer();
//        TokenStream tokenStream = source;
//        Pattern p = Pattern.compile("[^A-Za-z0-9]");
//
//        boolean replaceAll = Boolean.TRUE;
//        tokenStream = new PatternReplaceFilter(tokenStream, p, " ", replaceAll);
//        return new TokenStreamComponents(source, tokenStream);
//    }
//

    @Override
    protected TokenStreamComponents createComponents(String s) {
        return new TokenStreamComponents(new WhitespaceTokenizer());
    }

}


