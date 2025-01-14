package com.uspto.opensearch;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.List;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final OpenSearchClientWrapper clientWrapper;

    public LambdaHandler() {
        // Initialize OpenSearch client with environment variables
        String endpoint = System.getenv("DOMAIN_ENDPOINT");
        String region = System.getenv("REGION");
        String index = System.getenv("INDEX_NAME");
        this.clientWrapper = new OpenSearchClientWrapper(endpoint, region, index);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String query = request.getQueryStringParameters().get("query");
        String recordIdsParam = request.getQueryStringParameters().get("record_ids");

        try {
            if (query != null) {
                List<String> recordIds = clientWrapper.getRelatedRecordIds(query);
                return createResponse(200, recordIds.toString());
            } else if (recordIdsParam != null) {
                List<String> recordIds = List.of(recordIdsParam.split(","));
                List<String> records = clientWrapper.getRecordDetails(recordIds);
                return createResponse(200, records.toString());
            } else {
                return createResponse(400, "Invalid request: Provide 'query' or 'record_ids' parameters.");
            }
        } catch (Exception e) {
            ErrorHandler.logError("Error handling request", e);
            return createResponse(500, "Internal server error: " + e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent createResponse(int statusCode, String body) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(body);
    }
}
