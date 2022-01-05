package com.demos.lucene;

import lombok.Builder;
import lombok.Data;
import org.apache.lucene.search.BooleanClause;

@Data
@Builder
public class QueryFilters {
    private BooleanClause.Occur occur = BooleanClause.Occur.MUST;
    private double boosting = 0.5;
}
