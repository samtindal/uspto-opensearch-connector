// Licensed under the MIT License. See LICENSE file for details.

package test.java.com.samtindal.usptoconnector.activities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetRelatedRecordIdsActivityTests {

    private OpenSearchClientWrapper mockOpenSearchClientWrapper;
    private GetRelatedRecordIdsActivity activity;

    @BeforeEach
    void setUp() {
        mockOpenSearchClientWrapper = mock(OpenSearchClientWrapper.class);
        activity = new GetRelatedRecordIdsActivity(mockOpenSearchClientWrapper);
    }

    @Test
    void testExecute_Success() {
        // Arrange
        String query = "patent";
        List<String> mockResponse = List.of("12345", "67890");
        when(mockOpenSearchClientWrapper.executeQuery(query)).thenReturn(mockResponse);

        // Act
        List<String> result = activity.execute(query);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockOpenSearchClientWrapper, times(1)).executeQuery(query);
    }

    @Test
    void testExecute_NullQuery_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> activity.execute(null));
    }

    @Test
    void testExecute_BlankQuery_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> activity.execute("   "));
    }
}
