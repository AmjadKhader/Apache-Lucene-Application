package com.demos.lucene.factory;

import org.apache.lucene.analysis.Analyzer;
import com.demos.lucene.constants.Constants;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class AnalyzerFactory {

    private AnalyzerFactory() {
    }

    public static Analyzer createAnalyzer(Constants.eAnalyzerType analyzerType) {
        switch (analyzerType) {
            case SIMPLE:
                return new SimpleAnalyzer();
            case STANDARD:
                return new StandardAnalyzer();
            case WHITE_SPACE:
                return new WhitespaceAnalyzer();
        }
        return null;
    }
}
