package com.demos.lucene.filter;

import org.apache.lucene.analysis.util.CharTokenizer;

public class TextTokenizer extends CharTokenizer {

    @Override
    protected boolean isTokenChar(int i) {
        return false;
    }
}
