// Licensed under the MIT License. See LICENSE file for details.

package com.samtindal.usptoconnector.activities;

import com.samtindal.usptoconnector.client.OpenSearchClientWrapper;
import com.samtindal.usptoconnector.client.RecordDetails;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class GetRecordDetailsActivityTest {

    private final OpenSearchClientWrapper client = mock(OpenSearchClientWrapper.class);
    private final GetRecordDetailsActivity activity = new GetRecordDetailsActivity(client);

    @Test
    void returnsRecordFromClient() {
        var record = new RecordDetails("12345", "Example Record", "Details of the record.");
        when(client.fetchDetails("12345")).thenReturn(record);

        assertEquals(record, activity.execute("12345"));
    }

    @Test
    void rejectsNullRecordIdWithoutCallingClient() {
        assertThrows(IllegalArgumentException.class, () -> activity.execute(null));
        verifyNoInteractions(client);
    }

    @Test
    void rejectsBlankRecordIdWithoutCallingClient() {
        assertThrows(IllegalArgumentException.class, () -> activity.execute("   "));
        verifyNoInteractions(client);
    }
}
