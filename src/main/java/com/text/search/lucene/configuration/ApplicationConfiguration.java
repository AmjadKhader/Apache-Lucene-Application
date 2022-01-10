package com.text.search.lucene.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class ApplicationConfiguration extends Configuration {

    @JsonProperty("analyzer")
    private AnalyzerConfiguration analyzerConfiguration;

    @JsonProperty("max-document-number")
    private int maxDocumentNumber;

    public AnalyzerConfiguration getAnalyzerConfiguration() {
        return analyzerConfiguration;
    }

    public int getMaxDocumentNumber() {
        return maxDocumentNumber;
    }

}