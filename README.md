# USPTO OpenSearch Client Wrapper

## Overview

This project provides an encapsulated client wrapper for interacting with the USPTO OpenSearch data store. The wrapper abstracts the complexities of directly interacting with OpenSearch, allowing users to query related records and retrieve detailed data in a simple and business-friendly manner.

## Components

### `OpenSearchClient`
- **Purpose**: The primary interface for interacting with the USPTO OpenSearch data store.
- **Responsibilities**:
  - Establish and manage the connection to OpenSearch.
  - Provide methods for querying and inserting data into OpenSearch.
  
#### Methods:
- `connect()`: Establishes the connection to OpenSearch.
- `disconnect()`: Closes the connection to OpenSearch.
- `queryData()`: Executes a query to retrieve data from OpenSearch.
- `insertData()`: Inserts new data into OpenSearch.
- `queryRelatedRecords(query: str)`: Queries for related records based on a query string and returns a list of record IDs.
- `getRecordDetails(record_ids: List[str])`: Accepts a list of record IDs and returns detailed information for each record.

### `DataRetriever`
- **Purpose**: Responsible for retrieving data from OpenSearch based on queries.
- **Responsibilities**:
  - Executes queries to retrieve data and fetch related records by their IDs.
  
#### Methods:
- `executeQuery()`: Runs the query and returns matching results.
- `filterResults()`: Applies additional filtering criteria on retrieved data.
- `queryRelatedRecords(query: str)`: Queries OpenSearch to find related records and returns their IDs.
- `getRecordDetails(record_ids: List[str])`: Fetches details for a list of record IDs.

### `DataIndexer`
- **Purpose**: Responsible for indexing documents in OpenSearch.
- **Responsibilities**:
  - Insert new documents into OpenSearch.
  - Update or delete existing documents.

#### Methods:
- `indexDocument()`: Indexes a new document into OpenSearch.
- `updateDocument()`: Updates an existing document.
- `deleteDocument()`: Deletes a document from OpenSearch.

### `OpenSearchManager`
- **Purpose**: Manages OpenSearch cluster metadata and health.
- **Responsibilities**:
  - Monitor the health of the OpenSearch cluster and individual nodes.
  - Retrieve and update cluster information.

#### Methods:
- `monitorCluster()`: Monitors the status of the OpenSearch cluster.
- `getClusterInfo()`: Retrieves cluster metadata such as node health and index status.
- `updateCluster()`: Updates configurations or settings for the OpenSearch cluster.

### `ErrorHandler`
- **Purpose**: Handles errors and provides retry mechanisms.
- **Responsibilities**:
  - Manages retries for failed OpenSearch operations.
  - Logs errors for debugging and operational monitoring.

#### Methods:
- `handleError()`: Handles errors during data retrieval or insertion.
- `retry()`: Retries failed operations.
- `logError()`: Logs detailed error information for future analysis.

### `Logger`
- **Purpose**: Logs events, information, and errors for debugging and auditing purposes.
- **Responsibilities**:
  - Track system events and operational information.
  - Log errors and provide insights into system performance.

#### Methods:
- `logEvent()`: Logs an event that occurred within the system.
- `logError()`: Logs error details and stack traces.
- `logInfo()`: Logs general information messages for monitoring.

## Usage

To interact with the OpenSearch index, instantiate the `OpenSearchClient` and use its methods to query or insert data.

### Example:

```python
from opensearch_wrapper import OpenSearchClient

# Initialize the client
opensearch_client = OpenSearchClient(index_name="uspto_patents", region="us-east-1")

# Connect to OpenSearch
opensearch_client.connect()

# Query for related records by keyword
query = "patent"
record_ids = opensearch_client.queryRelatedRecords(query)
print(f"Related Record IDs: {record_ids}")

# Fetch details for related records
records = opensearch_client.getRecordDetails(record_ids)
print(f"Record Details: {records}")

# Disconnect
opensearch_client.disconnect()
