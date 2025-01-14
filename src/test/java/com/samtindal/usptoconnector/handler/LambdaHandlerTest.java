package test.java.com.samtindal.usptoconnector.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LambdaHandlerTests {

    private LambdaHandler lambdaHandler;
    private Context mockContext;

    @BeforeEach
    void setUp() {
        lambdaHandler = new LambdaHandler();
        mockContext = mock(Context.class);
    }

    @Test
    void testHandleGetRelatedRecordIds_Success() {
        // Arrange
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPath("/getRelatedRecordIds")
                .withQueryStringParameters(Map.of("query", "patent"));

        // Act
        APIGatewayProxyResponseEvent response = lambdaHandler.handleRequest(request, mockContext);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("relatedRecordIds"));
    }

    @Test
    void testHandleGetRelatedRecordIds_MissingQuery() {
        // Arrange
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPath("/getRelatedRecordIds");

        // Act
        APIGatewayProxyResponseEvent response = lambdaHandler.handleRequest(request, mockContext);

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCode());
        assertTrue(response.getBody().contains("Error retrieving related record IDs"));
    }

    @Test
    void testHandleGetRecordDetails_Success() {
        // Arrange
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPath("/getRecordDetails")
                .withQueryStringParameters(Map.of("recordId", "12345"));

        // Act
        APIGatewayProxyResponseEvent response = lambdaHandler.handleRequest(request, mockContext);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("12345")); // Assuming the mock record details return this.
    }

    @Test
    void testHandleGetRecordDetails_MissingRecordId() {
        // Arrange
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPath("/getRecordDetails");

        // Act
        APIGatewayProxyResponseEvent response = lambdaHandler.handleRequest(request, mockContext);

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCode());
        assertTrue(response.getBody().contains("Error retrieving record details"));
    }

    @Test
    void testHandleUnknownEndpoint() {
        // Arrange
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPath("/unknownEndpoint");

        // Act
        APIGatewayProxyResponseEvent response = lambdaHandler.handleRequest(request, mockContext);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
        assertTrue(response.getBody().contains("Endpoint not found"));
    }
}
