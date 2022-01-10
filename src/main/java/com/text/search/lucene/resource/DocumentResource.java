package com.text.search.lucene.resource;

import com.text.search.lucene.constants.Constants;
import com.text.search.lucene.dto.RequestDTO;
import com.text.search.lucene.entites.Message;
import com.text.search.lucene.manager.IndexManager;
import com.text.search.lucene.manager.QueryManager;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/messages")
public class DocumentResource {


    @GET
    @Path("/{messages-field}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getDocument(
            @PathParam("messages-field") String messagesField,
            RequestDTO requestDTO) throws IOException, ParseException, InvalidTokenOffsetsException {

        requestDTO.setSearchField(messagesField);
        return QueryManager.getInstance().performSearch(messagesField, requestDTO);
    }

    @POST
    public String addDocument(Message document) throws IOException {
        IndexManager.getInstance().addDocument(document);
        return document.getDocumentId();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Message updateDocument(Message document) throws IOException {
        IndexManager.getInstance().updateDocument(document);
        return document;
    }

    @DELETE
    @Path("/{messages-Id}")
    public int deleteDocument(@PathParam("messages-Id") String documentId) throws IOException {
        IndexManager.getInstance().deleteDocument(Constants.ID, documentId);
        return HttpStatus.OK_200;
    }
}