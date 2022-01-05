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
import java.util.Objects;

public class AnalyzerFactory {

    private static Analyzer analyzer = null;

    private AnalyzerFactory() {
    }

    public static Analyzer getAnalyzer() throws IOException {
        if (Objects.isNull(analyzer)) {
            analyzer = createAnalyzer(Constants.eAnalyzerType.STANDARD); // the default analyzer.
        }

        return analyzer;
    }

    public static Analyzer createAnalyzer(Constants.eAnalyzerType analyzerType) throws IOException {
        switch (analyzerType) {
            case SIMPLE:
                analyzer = new SimpleAnalyzer();
                break;
            case STANDARD:
                analyzer = new StandardAnalyzer();
                break;
            case WHITE_SPACE:
                analyzer = new WhitespaceAnalyzer();
                break;
            case CUSTOM:
                analyzer = CustomAnalyzer.builder()
                        .withTokenizer(StandardTokenizerFactory.class)
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        .addTokenFilter(ArabicNormalizationFilterFactory.class)
                        .addTokenFilter(EnglishPossessiveFilterFactory.class).build();
        }
        return analyzer;
    }
}
