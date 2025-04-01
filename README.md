# USPTO OpenSearch Connector 🔍

## Overview

The **USPTO OpenSearch Connector** is a read-only service designed to interface with the USPTO OpenSearch data store hosted on AWS. It simplifies querying and retrieving patent-related data using the AWS OpenSearch SDK, managing endpoint configuration and credential details within the wrapper itself. API permissions are enforced via IAM roles assumed by the calling application.

This client wrapper also handles pre-processing of record details retrieved from USPTO and is written in Java for its strong-typing support. The calling application is recommended to be written in Python, R, or another language for advanced data processing.

The API methods are exposed via **Amazon API Gateway**, with compute provided by **AWS Lambda** for scalability and cost efficiency. The CDK package for deploying this service is available at:  
[GitHub: samtindal/uspto-opensearch-connector-cdk](https://github.com/samtindal/uspto-opensearch-connector-cdk)

---

## Features ✔️

- **Serverless**: Fully serverless architecture powered by AWS Lambda and API Gateway.
- **Efficient Querying**: Easy interaction with the USPTO OpenSearch API through well-defined methods.
- **Extensible Design**: Modular activity classes for handling specific API methods.
- **Simple Routing**: Lambda handler routes requests to the appropriate activity class based on the method invoked.

---

## Table of Contents 📚

- [Architecture](#architecture)
- [API Endpoints](#api-endpoints)
- [Methods](#methods)

---

## Architecture 🏗️

This application is built around the following components:

1. **Lambda Handler**  
   - The entry point for API Gateway requests.  
   - Routes requests to the appropriate activity class based on the invoked method.

2. **Activity Classes**  
   - `GetRelatedRecordIdsActivity`: Handles retrieving related record IDs using a query string.  
   - `GetRecordDetailsActivity`: Handles fetching record details using a record ID.

3. **Logging**  
   - Uses **SLF4J** for structured and efficient logging within all components.  
   - Logs are sent to **AWS CloudWatch** for centralized storage and monitoring.

4. **API Gateway**  
   - The interface for users to interact with the Lambda function.  
   - Routes HTTP requests to the Lambda Handler.

5. **IAM Role-Based Access**: 
   - API permissions are managed by IAM roles assumed by the calling application.

---

## API Endpoints 🌐

1. **`GET /getRelatedRecordIds`**  
   - **Query Parameters**:  
     - `query` (string): The search query string to retrieve related record IDs.  
   - **Response**:  
     - A list of related record IDs.

2. **`GET /getRecordDetails`**  
   - **Query Parameters**:  
     - `recordId` (string): The ID of the record to fetch details for.  
   - **Response**:  
     - The details of the specified record.

---

## Methods 🛠️

### 1. `getRelatedRecordIds`

- **Description**: Fetches related record IDs based on the provided query string.  
- **Input**:  
  - Query String: `query` (e.g., "patent search term").  
- **Output**:  
  - A JSON object containing an array of related record IDs.  

### 2. `getRecordDetails`

- **Description**: Retrieves the detailed information of a specific record using its record ID.  
- **Input**:  
  - Query Parameter: `recordId` (e.g., "12345").  
- **Output**:  
  - A JSON object with the details of the requested record.

## Class Diagram

```plantuml
@startuml
class LambdaHandler {
    - getRelatedIds: GetRelatedRecordIdsActivity
    - getRecordDetails: GetRecordDetailsActivity
}

class GetRelatedRecordIdsActivity {
    - osClient: OpenSearchClientWrapper
    + execute(query: String): List<String>
}

class GetRecordDetailsActivity {
    - osClient: OpenSearchClientWrapper
    + execute(recordId: String): UsptoRecord
}

class OpenSearchClientWrapper {
    - executeQuery(query: String): List<String>
    - fetchDetails(recordId: String): UsptoRecord
}

LambdaHandler --> GetRelatedRecordIdsActivity
LambdaHandler --> GetRecordDetailsActivity
GetRelatedRecordIdsActivity --> OpenSearchClientWrapper
GetRecordDetailsActivity --> OpenSearchClientWrapper
@enduml

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.