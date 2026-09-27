// Licensed under the MIT License. See LICENSE file for details.

package com.samtindal.usptoconnector.activities;

import com.samtindal.usptoconnector.client.OpenSearchClientWrapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class GetRelatedRecordIdsActivityTest {

    private final OpenSearchClientWrapper client = mock(OpenSearchClientWrapper.class);
    private final GetRelatedRecordIdsActivity activity = new GetRelatedRecordIdsActivity(client);

    @Test
    void returnsIdsFromClient() {
        when(client.executeQuery("patent")).thenReturn(List.of("12345", "67890"));

        assertEquals(List.of("12345", "67890"), activity.execute("patent"));
    }

    @Test
    void rejectsNullQueryWithoutCallingClient() {
        assertThrows(IllegalArgumentException.class, () -> activity.execute(null));
        verifyNoInteractions(client);
    }

    @Test
    void rejectsBlankQueryWithoutCallingClient() {
        assertThrows(IllegalArgumentException.class, () -> activity.execute("   "));
        verifyNoInteractions(client);
    }
}
