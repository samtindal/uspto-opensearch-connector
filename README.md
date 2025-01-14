# USPTO OpenSearch Connector

## Overview

The **USPTO OpenSearch Connector** is a read-only service designed to interface with the USPTO OpenSearch data store hosted on AWS. It simplifies querying and retrieving patent-related data using the AWS OpenSearch SDK, managing endpoint configuration and credential details within the wrapper itself. API permissions are enforced via IAM roles assumed by the calling application.

## Features

- **Read-only Access**: Query and retrieve data from OpenSearch without modifying or indexing records.
- **Query Related Records**: Retrieve related record IDs based on a query string.
- **Fetch Record Details**: Retrieve detailed information for records using their unique identifiers.
- **AWS OpenSearch SDK Integration**: Utilizes the AWS SDK for OpenSearch to ensure secure and efficient operations.
- **IAM Role-based Access**: API permissions are managed by IAM roles assumed by the calling application.

## Components

### Key Responsibilities

1. **Client Wrapper**:
   - Manages endpoint configuration and credential knowledge required for the OpenSearch client.
   - Abstracts the complexities of interacting with the OpenSearch SDK.

2. **Data Retrieval**:
   - Query for related records based on a search term.
   - Fetch detailed information for specific record IDs.

### High-Level Methods

- **`getRelatedRecordIds(query: String)`**: Executes a query to fetch related record IDs.
- **`getRecordDetails(recordIds: List<String>)`**: Retrieves details for the specified record IDs.

## Prerequisites

Before using the **USPTO OpenSearch Connector**, ensure the following:

1. **AWS Credentials**:
   - The calling application must assume an IAM role with permissions to access the OpenSearch domain and execute the necessary API calls.

2. **OpenSearch Domain**:
   - The endpoint of the OpenSearch domain you want to connect to (e.g., `https://search-uspto-patents-domain.us-east-1.es.amazonaws.com`).

3. **Java Development Environment**:
   - JDK 17 or higher.
   - Maven or Gradle for dependency management.

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
        <version>1.7.32</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.6</version>
    </dependency>
</dependencies>
