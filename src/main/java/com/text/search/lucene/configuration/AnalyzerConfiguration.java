package com.text.search.lucene.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AnalyzerConfiguration {

    @NotEmpty
    @JsonProperty("analyzer-type")
    private String analyzerType;

    @JsonProperty("index-path")
    private String indexPath;

    @JsonProperty("stop-words")
    private String stopWords;

    @JsonProperty("synonym")
    private String synonym;
}