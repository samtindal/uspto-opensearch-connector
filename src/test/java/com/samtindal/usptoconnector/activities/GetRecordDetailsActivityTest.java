package test.java.com.samtindal.usptoconnector.activities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetRecordDetailsActivityTests {

    private OpenSearchClientWrapper mockOpenSearchClientWrapper;
    private GetRecordDetailsActivity activity;

    @BeforeEach
    void setUp() {
        mockOpenSearchClientWrapper = mock(OpenSearchClientWrapper.class);
        activity = new GetRecordDetailsActivity(mockOpenSearchClientWrapper);
    }

    @Test
    void testExecute_Success() {
        // Arrange
        String recordId = "12345";
        String mockResponse = "{\"recordId\": \"12345\", \"title\": \"Example Record\"}";
        when(mockOpenSearchClientWrapper.fetchDetails(recordId)).thenReturn(mockResponse);

        // Act
        String result = activity.execute(recordId);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("12345"));
        verify(mockOpenSearchClientWrapper, times(1)).fetchDetails(recordId);
    }

    @Test
    void testExecute_NullRecordId_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> activity.execute(null));
    }

    @Test
    void testExecute_BlankRecordId_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> activity.execute("   "));
    }
}
