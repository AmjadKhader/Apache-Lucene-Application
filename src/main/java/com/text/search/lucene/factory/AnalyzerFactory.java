package com.text.search.lucene.factory;

import com.text.search.lucene.constants.Constants;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.synonym.SynonymGraphFilterFactory;

import java.io.IOException;
import java.nio.file.Paths;

public class AnalyzerFactory {

    /**
     * This Factory class is responsible for creating and retrieving Analyzer
     * AnalyzerFactory exposes the following functionalities:
     *      - Creates an analyzer based on the type ( typical factory class)
     *      - Retrieve analyzer type.
     **/

    private static Analyzer analyzer = null;

    private AnalyzerFactory() {
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
                analyzer = CustomAnalyzer.builder(Paths.get("/home/amjad/Desktop/Lucene Data"))
                        .withTokenizer(StandardTokenizerFactory.class)
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        .addTokenFilter(StopFilterFactory.class, "ignoreCase", "true", "words", Constants.STOPWORDS) // Remove words
                        .addTokenFilter(SynonymGraphFilterFactory.class, "ignoreCase", "true", "synonyms", Constants.SYNONYM, "expand", "false") // replace words.
                        .build();
                break;
        }
        return analyzer;
    }

    public static Analyzer getAnalyzer() {
        return analyzer;
    }
}
