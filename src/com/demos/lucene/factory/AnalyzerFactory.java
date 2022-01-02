package com.demos.lucene.factory;

import org.apache.lucene.analysis.Analyzer;
import com.demos.lucene.constants.Constants;
import org.apache.lucene.analysis.ar.ArabicNormalizationFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

import java.io.IOException;

public class AnalyzerFactory {

    private AnalyzerFactory() {
    }

    public static Analyzer createAnalyzer(Constants.eAnalyzerType analyzerType) throws IOException {
        switch (analyzerType) {
            case SIMPLE:
                return new SimpleAnalyzer();
            case STANDARD:
                return new StandardAnalyzer();
            case WHITE_SPACE:
                return new WhitespaceAnalyzer();
            case CUSTOM:
                return CustomAnalyzer.builder()
                        .withTokenizer(StandardTokenizerFactory.class)
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        .addTokenFilter(ArabicNormalizationFilterFactory.class)
                        .addTokenFilter(EnglishPossessiveFilterFactory.class).build();
        }
        return null;
    }
}
