// Licensed under the MIT License. See LICENSE file for details.

package main.java.com.samtindal.usptoconnector.activities;

import main.java.com.samtindal.usptoconnector.client.OpenSearchClientWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the logic for retrieving detailed record information from the USPTO OpenSearch API.
 */
public class GetRecordDetailsActivity {

    private static final Logger logger = LoggerFactory.getLogger(GetRecordDetailsActivity.class);

    private final OpenSearchClientWrapper openSearchClientWrapper;

    /**
     * Constructor to inject the OpenSearchClientWrapper.
     */
    public GetRecordDetailsActivity(OpenSearchClientWrapper openSearchClientWrapper) {
        this.openSearchClientWrapper = openSearchClientWrapper;
    }

    /**
     * Executes the activity to retrieve detailed information for the given record ID.
     *
     * @param recordId the unique record ID
     * @return the details of the record as a string (JSON or any format)
     */
    public String execute(String recordId) {
        logger.info("Executing GetRecordDetailsActivity with recordId: {}", recordId);

        if (recordId == null || recordId.isBlank()) {
            throw new IllegalArgumentException("Record ID cannot be null or blank.");
        }

        // Use the OpenSearchClientWrapper to fetch details
        return openSearchClientWrapper.fetchDetails(recordId);
    }
}
