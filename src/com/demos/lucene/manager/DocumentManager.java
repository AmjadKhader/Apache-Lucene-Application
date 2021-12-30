package com.demos.lucene.manager;

import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import static com.demos.lucene.constants.Constants.ID;
import static com.demos.lucene.constants.Constants.TITLE;
import static com.demos.lucene.constants.Constants.MESSAGE;

public class DocumentManager {

    private DocumentManager() {
    }

    public static Document createDocument(Integer id, String title, String message) {
        Document document = new Document();

        document.add(new StringField(ID, id.toString(), Field.Store.YES));
        document.add(new StringField(TITLE, title, Field.Store.YES));
        document.add(new TextField(MESSAGE, message, Field.Store.YES)); // Tokenize -> will be analyzed

        return document;
    }
}
