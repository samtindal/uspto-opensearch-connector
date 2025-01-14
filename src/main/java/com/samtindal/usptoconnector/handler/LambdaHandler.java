package main.java.com.samtindal.usptoconnector.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Lambda Handler for USPTO OpenSearch Connector.
 * Routes incoming requests to the appropriate activity classes.
 */
public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = LoggerFactory.getLogger(LambdaHandler.class);

    // Activity classes
    private final GetRelatedRecordIdsActivity getRelatedRecordIdsActivity;
    private final GetRecordDetailsActivity getRecordDetailsActivity;

    // Constructor
    public LambdaHandler() {
        // Initialize activity classes and dependencies
        OpenSearchClientWrapper openSearchClientWrapper = new OpenSearchClientWrapper();
        this.getRelatedRecordIdsActivity = new GetRelatedRecordIdsActivity(openSearchClientWrapper);
        this.getRecordDetailsActivity = new GetRecordDetailsActivity(openSearchClientWrapper);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        logger.info("Received request: {}", request.getPath());

        // Response object
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            String path = request.getPath();
            switch (path) {
                case "/getRelatedRecordIds":
                    return handleGetRelatedRecordIds(request);
                case "/getRecordDetails":
                    return handleGetRecordDetails(request);
                default:
                    return response
                            .withStatusCode(404)
                            .withBody("{\"message\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            logger.error("Error while processing request", e);
            return response
                    .withStatusCode(500)
                    .withBody("{\"message\": \"Internal server error\"}");
        }
    }

    private APIGatewayProxyResponseEvent handleGetRelatedRecordIds(APIGatewayProxyRequestEvent request) {
        String query = request.getQueryStringParameters().get("query");
        logger.info("Handling getRelatedRecordIds with query: {}", query);

        var response = new APIGatewayProxyResponseEvent();
        try {
            var relatedIds = getRelatedRecordIdsActivity.execute(query);
            return response
                    .withStatusCode(200)
                    .withBody("{\"relatedRecordIds\": " + relatedIds.toString() + "}");
        } catch (Exception e) {
            logger.error("Error in getRelatedRecordIdsActivity", e);
            return response
                    .withStatusCode(500)
                    .withBody("{\"message\": \"Error retrieving related record IDs\"}");
        }
    }

    private APIGatewayProxyResponseEvent handleGetRecordDetails(APIGatewayProxyRequestEvent request) {
        String recordId = request.getQueryStringParameters().get("recordId");
        logger.info("Handling getRecordDetails with recordId: {}", recordId);

        var response = new APIGatewayProxyResponseEvent();
        try {
            var recordDetails = getRecordDetailsActivity.execute(recordId);
            return response
                    .withStatusCode(200)
                    .withBody(recordDetails.toString());
        } catch (Exception e) {
            logger.error("Error in getRecordDetailsActivity", e);
            return response
                    .withStatusCode(500)
                    .withBody("{\"message\": \"Error retrieving record details\"}");
        }
    }
}
