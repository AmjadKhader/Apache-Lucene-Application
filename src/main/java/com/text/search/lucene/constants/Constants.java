package com.text.search.lucene.constants;

public class Constants {

    public enum eAnalyzerType {STANDARD, SIMPLE, WHITE_SPACE, CUSTOM}

    public static eAnalyzerType analyzerType;

    public static int MAX_DOC_NUMBER;
    public static String INDEX_DIR;
    public static String STOPWORDS;
    public static String SYNONYM;

    public static final String ID = "documentId";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";

}