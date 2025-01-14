package com.uspto.opensearch;

import software.amazon.awssdk.services.opensearch.OpenSearchClient;
import software.amazon.awssdk.services.opensearch.model.*;

import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public static List<String> queryRelatedRecords(OpenSearchClient client, String index, String query) {
        List<String> recordIds = new ArrayList<>();
        try {
            SearchRequest searchRequest = SearchRequest.builder()
                    .index(index)
                    .body("""
                          {
                              "query": {
                                  "match": {
                                      "content": "%s"
                                  }
                              }
                          }
                          """.formatted(query))
                    .build();

            SearchResponse searchResponse = client.search(searchRequest);
            searchResponse.hits().hits().forEach(hit -> recordIds.add(hit.id()));
        } catch (Exception e) {
            ErrorHandler.logError("Error querying related records", e);
            throw new RuntimeException(e);
        }
        return recordIds;
    }

    public static List<String> getRecordDetails(OpenSearchClient client, String index, List<String> recordIds) {
        List<String> records = new ArrayList<>();
        try {
            for (String recordId : recordIds) {
                GetRequest getRequest = GetRequest.builder()
                        .index(index)
                        .id(recordId)
                        .build();
                GetResponse getResponse = client.get(getRequest);
                records.add(getResponse.sourceAsString());
            }
        } catch (Exception e) {
            ErrorHandler.logError("Error fetching record details", e);
            throw new RuntimeException(e);
        }
        return records;
    }
}
