// Licensed under the MIT License. See LICENSE file for details.

package com.samtindal.usptoconnector.client;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenSearchClientWrapperTest {

    private final OpenSearchClientWrapper client = new OpenSearchClientWrapper();

    @Test
    void executeQueryReturnsSimulatedRecordIds() {
        assertEquals(List.of("12345", "67890", "11223"), client.executeQuery("patent search"));
    }

    @Test
    void fetchDetailsReturnsSimulatedRecordForRequestedId() {
        RecordDetails record = client.fetchDetails("12345");

        assertEquals("12345", record.recordId());
        assertEquals("Example Record", record.title());
    }
}
