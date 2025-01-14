package com.uspto.opensearch;

import software.amazon.awssdk.services.opensearch.OpenSearchClient;
import software.amazon.awssdk.services.opensearch.model.*;

import java.util.List;

public class UsptoOpenSearchConnector {

    private final OpenSearchClient openSearchClient;
    private final String index;

    public UsptoOpenSearchConnector(String endpoint, String region, String index) {
        this.openSearchClient = OpenSearchClient.builder()
                .endpointOverride(java.net.URI.create(endpoint))
                .region(software.amazon.awssdk.regions.Region.of(region))
                .build();
        this.index = index;
    }

    public List<String> getRelatedRecordIds(String query) {
        return DataRetriever.queryRelatedRecords(openSearchClient, index, query);
    }

    public List<String> getRecordDetails(List<String> recordIds) {
        return DataRetriever.getRecordDetails(openSearchClient, index, recordIds);
    }
}
