package com.demos.lucene.filter;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.Locale;

public class TextFilter extends TokenFilter {

    private final CharTermAttribute charTermAttr;

    public TextFilter(TokenStream input) {
        super(input);
        this.charTermAttr = addAttribute(CharTermAttribute.class);
    }

    public void replaceToken(String old) throws IOException {
        if (charTermAttr.toString().toLowerCase(Locale.ROOT).equals("father")) {
            charTermAttr.setEmpty();
            charTermAttr.append(old);
        }
    }

    @Override
    public boolean incrementToken() throws IOException {
        return input.incrementToken();
    }
}
