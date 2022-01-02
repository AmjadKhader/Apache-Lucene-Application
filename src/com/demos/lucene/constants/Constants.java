package com.demos.lucene.constants;

public class Constants {

    public enum eAnalyzerType {STANDARD, SIMPLE, WHITE_SPACE, CUSTOM}

    public enum eOperation {ADD, ADD_DOC, UPDATE_DOC, DELETE_DOC}

    public static final String INDEX_DIR_STANDARD = "/home/amjad/Desktop/Lucene Data/StandardAnalyzer";
    public static final String INDEX_DIR_SIMPLE = "/home/amjad/Desktop/Lucene Data/SimpleAnalyzer";
    public static final String INDEX_DIR_WHITE = "/home/amjad/Desktop/Lucene Data/WhiteAnalyzer";

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
}
