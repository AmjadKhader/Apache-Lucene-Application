package com.demos.lucene.entites;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDocument {
    private String documentId;
    private String description;
    private String title;
}
