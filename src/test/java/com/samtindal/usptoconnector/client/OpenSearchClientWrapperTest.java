// Licensed under the MIT License. See LICENSE file for details.

package test.java.com.samtindal.usptoconnector.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the OpenSearchClientWrapper class.
 */
class OpenSearchClientWrapperTests {

    private OpenSearchClientWrapper openSearchClientWrapper;

    @BeforeEach
    void setUp() {
        // Create an instance of OpenSearchClientWrapper.
        // If you need to mock HTTP clients inside the wrapper, you can initialize them here.
        openSearchClientWrapper = new OpenSearchClientWrapper();
    }

    @Test
    void testExecuteQuery_Success() {
        // Arrange
        String query = "patent search";
        // Simulated behavior: the OpenSearchClientWrapper should return mock results.
        // You can replace this with actual mocking if the wrapper uses an HTTP client internally.

        // Act
        var result = openSearchClientWrapper.executeQuery(query);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(3, result.size(), "Result size should match expected value");
        assertTrue(result.contains("12345"), "Result should contain the mock record ID '12345'");
    }

    @Test
    void testExecuteQuery_EmptyQuery() {
        // Arrange
        String query = "";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            openSearchClientWrapper.executeQuery(query);
        });

        assertEquals("Query cannot be empty or null.", exception.getMessage());
    }

    @Test
    void testExecuteQuery_NullQuery() {
        // Arrange
        String query = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            openSearchClientWrapper.executeQuery(query);
        });

        assertEquals("Query cannot be empty or null.", exception.getMessage());
    }

    @Test
    void testFetchDetails_Success() {
        // Arrange
        String recordId = "12345";

        // Act
        String result = openSearchClientWrapper.fetchDetails(recordId);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.contains("12345"), "Result should contain the record ID");
        assertTrue(result.contains("Example Record"), "Result should contain the mock title");
    }

    @Test
    void testFetchDetails_EmptyRecordId() {
        // Arrange
        String recordId = "";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            openSearchClientWrapper.fetchDetails(recordId);
        });

        assertEquals("Record ID cannot be empty or null.", exception.getMessage());
    }

    @Test
    void testFetchDetails_NullRecordId() {
        // Arrange
        String recordId = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            openSearchClientWrapper.fetchDetails(recordId);
        });

        assertEquals("Record ID cannot be empty or null.", exception.getMessage());
    }
}
