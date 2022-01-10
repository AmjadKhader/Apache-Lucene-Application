package com.text.search.lucene.entites;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message implements IDocument {
    @JsonProperty("documentId")
    private String documentId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("title")
    private String title;
}
