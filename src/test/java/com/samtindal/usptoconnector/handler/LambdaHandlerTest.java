// Licensed under the MIT License. See LICENSE file for details.

package com.samtindal.usptoconnector.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samtindal.usptoconnector.activities.GetRecordDetailsActivity;
import com.samtindal.usptoconnector.activities.GetRelatedRecordIdsActivity;
import com.samtindal.usptoconnector.client.OpenSearchClientWrapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LambdaHandlerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Context context = mock(Context.class);
    private final LambdaHandler handler = new LambdaHandler();

    private static APIGatewayProxyRequestEvent get(String path, Map<String, String> queryParams) {
        return new APIGatewayProxyRequestEvent().withPath(path).withQueryStringParameters(queryParams);
    }

    private static JsonNode jsonBody(APIGatewayProxyResponseEvent response) throws Exception {
        assertEquals("application/json", response.getHeaders().get("Content-Type"));
        return MAPPER.readTree(response.getBody());
    }

    @Test
    void relatedRecordIdsReturnsJsonArrayOfStringIds() throws Exception {
        var response = handler.handleRequest(get("/getRelatedRecordIds", Map.of("query", "patent")), context);

        assertEquals(200, response.getStatusCode());
        JsonNode ids = jsonBody(response).get("relatedRecordIds");
        assertTrue(ids.isArray());
        assertTrue(ids.get(0).isTextual());
        assertEquals("12345", ids.get(0).asText());
    }

    @Test
    void relatedRecordIdsWithoutAnyQueryStringIsBadRequest() throws Exception {
        var response = handler.handleRequest(get("/getRelatedRecordIds", null), context);

        assertEquals(400, response.getStatusCode());
        assertTrue(jsonBody(response).get("message").asText().contains("Query"));
    }

    @Test
    void relatedRecordIdsWithBlankQueryIsBadRequest() throws Exception {
        var response = handler.handleRequest(get("/getRelatedRecordIds", Map.of("query", "  ")), context);

        assertEquals(400, response.getStatusCode());
        assertTrue(jsonBody(response).get("message").asText().contains("Query"));
    }

    @Test
    void recordDetailsReturnsRecordAsJsonObject() throws Exception {
        var response = handler.handleRequest(get("/getRecordDetails", Map.of("recordId", "12345")), context);

        assertEquals(200, response.getStatusCode());
        JsonNode record = jsonBody(response);
        assertEquals("12345", record.get("recordId").asText());
        assertEquals("Example Record", record.get("title").asText());
    }

    @Test
    void recordDetailsEscapesQuotesAndBackslashesInRecordId() throws Exception {
        String awkwardId = "12\"34\\5";

        var response = handler.handleRequest(get("/getRecordDetails", Map.of("recordId", awkwardId)), context);

        assertEquals(200, response.getStatusCode());
        assertEquals(awkwardId, jsonBody(response).get("recordId").asText());
    }

    @Test
    void recordDetailsWithoutRecordIdIsBadRequest() throws Exception {
        var response = handler.handleRequest(get("/getRecordDetails", Map.of("query", "patent")), context);

        assertEquals(400, response.getStatusCode());
        assertTrue(jsonBody(response).get("message").asText().contains("Record ID"));
    }

    @Test
    void unknownPathIsNotFound() throws Exception {
        var response = handler.handleRequest(get("/unknownEndpoint", null), context);

        assertEquals(404, response.getStatusCode());
        assertEquals("Endpoint not found", jsonBody(response).get("message").asText());
    }

    @Test
    void unexpectedFailureIsInternalServerErrorWithoutLeakingDetails() throws Exception {
        var failingActivity = mock(GetRelatedRecordIdsActivity.class);
        when(failingActivity.execute("patent"))
                .thenThrow(new RuntimeException("connection refused to internal-host:9200"));
        var handlerWithFailure = new LambdaHandler(
                failingActivity, new GetRecordDetailsActivity(new OpenSearchClientWrapper()));

        var response = handlerWithFailure.handleRequest(
                get("/getRelatedRecordIds", Map.of("query", "patent")), context);

        assertEquals(500, response.getStatusCode());
        assertEquals("Internal server error", jsonBody(response).get("message").asText());
    }
}
