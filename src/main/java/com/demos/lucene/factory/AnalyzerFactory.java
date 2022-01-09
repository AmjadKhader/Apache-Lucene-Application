package com.demos.lucene.factory;

import com.demos.lucene.constants.Constants;
import com.demos.lucene.manager.IndexManager;
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
import java.util.Objects;

import static com.demos.lucene.constants.Constants.*;

public class AnalyzerFactory {

    /**
     * This Factory class is responsible for creating and retrieving Analyzer
     * AnalyzerFactory exposes the following functionalities:
     *      - Creates an analyzer based on the type ( typical factory class)
     *      - Retrieve analyzer type.
     **/

    private static Analyzer analyzer = null;
    private static eAnalyzerType analyzerType = eAnalyzerType.STANDARD;

    private AnalyzerFactory() {
    }

    public static Analyzer getAnalyzer() throws IOException {
        if (Objects.isNull(analyzer)) {
            analyzer = createAnalyzer(Constants.eAnalyzerType.STANDARD); // the default analyzer.
            analyzerType = eAnalyzerType.STANDARD;
            IndexManager.getInstance().setAnalyzerType(eAnalyzerType.STANDARD);
        }

        return analyzer;
    }

    public static eAnalyzerType getAnalyzerType() {
        return analyzerType;
    }

    public static Analyzer createAnalyzer(Constants.eAnalyzerType newAnalyzerType) throws IOException {
        analyzerType = newAnalyzerType;

        switch (analyzerType) {
            case SIMPLE:
                analyzer = new SimpleAnalyzer();
                IndexManager.getInstance().setAnalyzerType(eAnalyzerType.SIMPLE);
                break;

            case STANDARD:
                analyzer = new StandardAnalyzer();
                IndexManager.getInstance().setAnalyzerType(eAnalyzerType.STANDARD);
                break;

            case WHITE_SPACE:
                analyzer = new WhitespaceAnalyzer();
                IndexManager.getInstance().setAnalyzerType(eAnalyzerType.WHITE_SPACE);
                break;

            case CUSTOM:
                analyzer = CustomAnalyzer.builder(Paths.get("/home/amjad/Desktop/Lucene Data"))
                        .withTokenizer(StandardTokenizerFactory.class)
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        .addTokenFilter(StopFilterFactory.class, "ignoreCase", "true", "words", STOPWORDS) // Remove words
                        .addTokenFilter(SynonymGraphFilterFactory.class, "ignoreCase", "true", "synonyms", SYNONYM, "expand", "false") // replace words.
                        .build();

                IndexManager.getInstance().setAnalyzerType(Constants.eAnalyzerType.CUSTOM);
                break;
        }
        return analyzer;
    }

}
