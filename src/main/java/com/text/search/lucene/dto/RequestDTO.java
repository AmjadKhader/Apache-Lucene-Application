package com.text.search.lucene.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    @JsonProperty("inner-queries")
    List<RequestDTO> requestDTOList;

    @Setter
    @JsonProperty("term")
    private String term;

    @JsonProperty("query-type")
    private String queryType;

    @JsonProperty("fuzzy-degree")
    private int fuzzyDegree;

    @JsonProperty("boost")
    private double boost = 0.5;

    @JsonProperty("occur")
    private String occur;

    @Setter
    private String searchField;
}
