package com.text.search.lucene.roles;

import lombok.Builder;
import lombok.Data;
import org.apache.lucene.search.BooleanClause;

@Data
@Builder
public class QueryRole {
    private BooleanClause.Occur occur = BooleanClause.Occur.MUST;
    private double boosting = 0.5;
}
