// Licensed under the MIT License. See LICENSE file for details.

package main.java.com.samtindal.usptoconnector.activities;

import main.java.com.samtindal.usptoconnector.client.OpenSearchClientWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Handles the logic for retrieving related record IDs from the USPTO OpenSearch API.
 */
public class GetRelatedRecordIdsActivity {

    private static final Logger logger = LoggerFactory.getLogger(GetRelatedRecordIdsActivity.class);

    private final OpenSearchClientWrapper openSearchClientWrapper;

    /**
     * Constructor to inject the OpenSearchClientWrapper.
     */
    public GetRelatedRecordIdsActivity(OpenSearchClientWrapper openSearchClientWrapper) {
        this.openSearchClientWrapper = openSearchClientWrapper;
    }

    /**
     * Executes the activity to retrieve related record IDs for the given query.
     *
     * @param query the search query string
     * @return a list of related record IDs
     */
    public List<String> execute(String query) {
        logger.info("Executing GetRelatedRecordIdsActivity with query: {}", query);

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query cannot be null or blank.");
        }

        // Use the OpenSearchClientWrapper to execute the query
        return openSearchClientWrapper.executeQuery(query);
    }
}
