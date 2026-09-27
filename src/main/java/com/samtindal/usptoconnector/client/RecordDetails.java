// Licensed under the MIT License. See LICENSE file for details.

package com.samtindal.usptoconnector.client;

/**
 * Details of a single USPTO record, serialized as the /getRecordDetails response body.
 */
public record RecordDetails(String recordId, String title, String description) {
}
