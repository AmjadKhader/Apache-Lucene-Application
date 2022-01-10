package com.text.search.lucene.manager;

import com.text.search.lucene.configuration.ApplicationConfiguration;
import com.text.search.lucene.constants.Constants;
import com.text.search.lucene.factory.AnalyzerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.text.search.lucene.constants.Constants.*;

public class StartupManager {
    private static StartupManager instance = null;

    private StartupManager() {
    }

    public static StartupManager getInstance() {
        if (Objects.isNull(instance)) {
            instance = new StartupManager();
        }
        return instance;
    }

    public void setAnalyzerConfiguration(ApplicationConfiguration applicationConfiguration) throws IOException {
        INDEX_DIR = applicationConfiguration.getAnalyzerConfiguration().getIndexPath() + applicationConfiguration.getAnalyzerConfiguration().getAnalyzerType();
        STOPWORDS = applicationConfiguration.getAnalyzerConfiguration().getStopWords();
        SYNONYM = applicationConfiguration.getAnalyzerConfiguration().getSynonym();
        MAX_DOC_NUMBER = applicationConfiguration.getMaxDocumentNumber();

        this.setupAnalyzerType(applicationConfiguration.getAnalyzerConfiguration().getAnalyzerType());
    }

    private void setupAnalyzerType(String analyzerType) throws IOException {
        switch (analyzerType.toLowerCase(Locale.ROOT)) {
            case "standard":
                Constants.analyzerType = eAnalyzerType.STANDARD;
                break;
            case "simple":
                Constants.analyzerType = eAnalyzerType.SIMPLE;
                break;
            case "whitespace":
                Constants.analyzerType = eAnalyzerType.WHITE_SPACE;
                break;
            case "custom":
                Constants.analyzerType = eAnalyzerType.CUSTOM;
                break;
            default:
                throw new NoSuchElementException("Choose propre analyzer type");
        }

        AnalyzerFactory.createAnalyzer(Constants.analyzerType);

    }
}
