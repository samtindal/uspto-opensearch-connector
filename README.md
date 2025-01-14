# USPTO OpenSearch Connector

## Overview

The **USPTO OpenSearch Connector** is a read-only service designed to interface with the USPTO OpenSearch data store hosted on AWS. It simplifies querying and retrieving patent-related data using the AWS OpenSearch SDK, managing endpoint configuration and credential details within the wrapper itself. API permissions are enforced via IAM roles assumed by the calling application.

This client wrapper also handles pre-processing of record details retrieved from USPTO and is written in Java for its strong-typing support. The calling application is recommended to be written in Python, R, or another language for advanced data processing.

The API methods are exposed via **Amazon API Gateway**, with compute provided by **AWS Lambda** for scalability and cost efficiency.

### GitHub Repository

The CDK package for deploying this service is available at:  
[GitHub: samtindal/uspto-opensearch-connector-cdk](https://github.com/samtindal/uspto-opensearch-connector-cdk)

## Features

- **Read-only Access**: Query and retrieve data from OpenSearch without modifying or indexing records.
- **Query Related Records**: Retrieve related record IDs based on a query string.
- **Fetch Record Details**: Retrieve detailed information for records using their unique identifiers.
- **AWS OpenSearch SDK Integration**: Utilizes the AWS SDK for OpenSearch to ensure secure and efficient operations.
- **IAM Role-Based Access**: API permissions are managed by IAM roles assumed by the calling application.
- **API Gateway and Lambda**:
  - **API Gateway**: Hosts the API endpoints for querying and retrieving data.
  - **AWS Lambda**: Executes the Java client wrapper for processing requests and interacting with OpenSearch.

## Components

### Key Responsibilities

1. **Client Wrapper**:
   - Manages endpoint configuration and credential knowledge required for the OpenSearch client.
   - Abstracts the complexities of interacting with the OpenSearch SDK.

2. **Data Retrieval**:
   - Query for related records based on a search term.
   - Fetch detailed information for specific record IDs.

3. **API Gateway Integration**:
   - Exposes two main API methods (`/query` and `/details`) for accessing OpenSearch data:
     - **`/query`**: Accepts a search term and returns matching record IDs.
     - **`/details`**: Accepts a list of record IDs and returns the corresponding record details.

4. **AWS Lambda**:
   - Handles the execution of the client wrapper.
   - Configured with environment variables for OpenSearch endpoint, region, and index name.

### High-Level Methods

- **`getRelatedRecordIds(query: String)`**: Executes a query to fetch related record IDs.
- **`getRecordDetails(recordIds: List<String>)`**: Retrieves details for the specified record IDs.

## Installation

### Maven

Add the following dependencies to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>opensearch</artifactId>
        <version>2.17.112</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7
