// Licensed under the MIT License. See LICENSE file for details.

package com.samtindal.usptoconnector.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samtindal.usptoconnector.activities.GetRecordDetailsActivity;
import com.samtindal.usptoconnector.activities.GetRelatedRecordIdsActivity;
import com.samtindal.usptoconnector.client.OpenSearchClientWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Main Lambda Handler for USPTO OpenSearch Connector.
 * Routes incoming requests to the appropriate activity classes.
 */
public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = LoggerFactory.getLogger(LambdaHandler.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Map<String, String> JSON_HEADERS = Map.of("Content-Type", "application/json");

    private final GetRelatedRecordIdsActivity getRelatedRecordIdsActivity;
    private final GetRecordDetailsActivity getRecordDetailsActivity;

    public LambdaHandler() {
        this(new OpenSearchClientWrapper());
    }

    private LambdaHandler(OpenSearchClientWrapper openSearchClientWrapper) {
        this(new GetRelatedRecordIdsActivity(openSearchClientWrapper),
                new GetRecordDetailsActivity(openSearchClientWrapper));
    }

    LambdaHandler(GetRelatedRecordIdsActivity getRelatedRecordIdsActivity,
                  GetRecordDetailsActivity getRecordDetailsActivity) {
        this.getRelatedRecordIdsActivity = getRelatedRecordIdsActivity;
        this.getRecordDetailsActivity = getRecordDetailsActivity;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String path = request.getPath();
        logger.info("Received request: {}", path);

        try {
            return switch (path) {
                case "/getRelatedRecordIds" -> respond(200, Map.of(
                        "relatedRecordIds", getRelatedRecordIdsActivity.execute(queryParam(request, "query"))));
                case "/getRecordDetails" -> respond(200,
                        getRecordDetailsActivity.execute(queryParam(request, "recordId")));
                case null, default -> respond(404, Map.of("message", "Endpoint not found"));
            };
        } catch (IllegalArgumentException e) {
            logger.warn("Rejected request to {}: {}", path, e.getMessage());
            return respond(400, Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error while processing request to {}", path, e);
            return respond(500, Map.of("message", "Internal server error"));
        }
    }

    private static String queryParam(APIGatewayProxyRequestEvent request, String name) {
        Map<String, String> params = request.getQueryStringParameters();
        return params == null ? null : params.get(name);
    }

    private static APIGatewayProxyResponseEvent respond(int statusCode, Object body) {
        try {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(statusCode)
                    .withHeaders(JSON_HEADERS)
                    .withBody(MAPPER.writeValueAsString(body));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize response body", e);
        }
    }
}
