# IADoc - Microservices project with document upload, question processing using RAG from documents, and the use of Kafka, Debezium, CDC, Spring Batch, Spring Boot, Minio, Spring AI, Ollama, and Deepseek-r1 for accurate responses.
The project consists of three main microservices: Application (document upload and question registration), Documents (document processing), and Questions (question processing).

The application microservice is responsible for uploading PDF documents and registering questions about them via a REST API.
To replicate data to the document and question microservices, I used the CDC (Change Data Capture) strategy, utilizing Confluent Kafka Connect and the Debezium plugin for PostgreSQL.
To optimize GPU usage, documents and questions are not processed in real time. They are stored and replicated to the respective microservices.
In the document microservice, a JOB was created with Spring Batch to perform Retrieval-Augmented Generation (RAG) on documents and store them as vectors in REDIS.
In the question microservice, another JOB was created with Spring Batch to answer the registered questions.
For more accurate responses, data is retrieved from the VectorDB (REDIS), placed in a template, and Spring AI interacts with the Deepseek-r1 model to obtain responses.

## Tools and frameworks used
- Ollama
- Deepseek-r1
- Kafka
- Confluent Kafka Connect
- Debezium
- PostgreSQL
- Redis
- Minio
- Docker
- Spring Boot
- Spring Cloud
- Spring AI
- Spring Batch

## Data flow between databases with CDC
![Alt text](/asserts/images/FluxoDadosCDC.jpg?raw=true "Diagram representing data flow between databases")

## Programs that need to be installed and started beforehand
- Docker Desktop
- JAVA 25
- MAVEN

## Before starting execution
- Create the environment variable UPLOAD_RECEIVED_DIR, containing the relative directory on your PC for receiving files.
  ```sh
  set UPLOAD_RECEIVED_DIR=D:\YourDirectory\IADoc\data\documents\received\
  ```

- In the pom.xml file at the root of the project, change the user.docker.hub property to your DockerHub username.
- In the docker-compose.yml file at the root of the project, change the microservices image paths to your DockerHub username.

## Steps to run via IDE
- In the terminal, execute the following command:
  ```sh
  docker-compose -f docker-compose-infra.yml up -d
  ```

- If running the project for the first time, wait for the Ollama container to complete the pull of the Deepseek-r1 model.
- Wait 2 minutes for all infrastructure containers to be fully initialized.
- Start the microservices in the following order, ensuring each one fully initializes before starting the next:
  1. Microservice microservice-discovery.
  2. Microservice microservice-api-gateway.
  3. Microservice microservice-application.
  4. Microservice microservice-document-processor.
  5. Microservice microservice-question-processor.

- In the terminal, execute the command to stop all containers.
  ```sh
  docker-compose -f docker-compose-infra.yml down
  ```

## Steps to run all containers via Docker
- At the root of the project, execute the command to generate microservice executables (JARs):
  ```sh
  mvn clean install
  ```

- At the root of the project, execute the command to generate microservice images:
  ```sh
  mvn spring-boot:build-image
  ```

- In the terminal, execute the following command:
  ```sh
  docker-compose -f docker-compose.yml up -d
  ```

- Wait for all containers to initialize.
- In the terminal, execute the command to stop all containers.
  ```sh
  docker-compose -f docker-compose.yml down --volumes
  ```

## Instructions for using the endpoints
- Upload a PDF document:
  ```sh
  curl --location 'http://localhost:8501/upload' --form 'files=@"/somePdfFile.pdf"'
  ```

- Register a question related to the PDF document:
  ```sh
  curl --location 'http://localhost:8501/questions' --header 'Content-Type: application/json' \
  --data '{
      "question": "Your question?",
      "documentId": 1
  }'
  ```

- After processing the document, the question will be answered.
- Retrieve the question:
  ```sh
  curl --location 'http://localhost:8501/questions/1'
  ```

> Note: The project contains a collection with all REST requests for importing into Postman or Insomnia.

> Free book download site: https://www.livrosabertos.abcd.usp.br/portaldelivrosUSP

## References

### CDC
- https://debezium.io/documentation/reference/stable/
- https://developers.redhat.com/articles/2023/07/06/how-use-debezium-smt-groovy-filter-routing-events
- https://youtu.be/0_Fm-xr3LY8?si=v2rjM9mDmOb1icrA
- https://medium.com/trendyol-tech/debezium-with-simple-message-transformation-smt-4f5a80c85358

### Spring AI and VectorDB
- https://youtu.be/4-rG2qsTrAs?si=0LFTj5qkzjGwMFxT
- https://youtu.be/ZoPVGrB8iHU?si=zNmMAC6962DvcsMl
- https://docs.spring.io/spring-ai/reference/index.html

### File upload
- https://spring.io/guides/gs/uploading-files
- https://medium.com/@patelsajal2/how-to-create-a-spring-boot-rest-api-for-multipart-file-uploads-a-comprehensive-guide-b4d95ce3022b

### Spring Batch
- https://spring.io/guides/gs/batch-processing
- https://www.toptal.com/spring/spring-batch-tutorial
- https://medium.com/@rostyslav.ivankiv/introduction-to-spring-batch-a2f39454573f
- https://tucanoo.com/spring-batch-example-building-a-bulk-contact-importer/
- https://stackoverflow.com/questions/29286699/repeating-a-step-x-times-in-spring-batch

### Minio
- https://gurselgazii.medium.com/integrating-minio-with-spring-boot-a-guide-to-simplified-object-storage-525d5a7686cc
- https://medium.com/@kapincev/a-developers-guide-to-integrating-minio-with-angular-and-spring-boot-3d77c13aefc7

### Condition Qualifier
- https://codingnconcepts.com/spring-boot/conditional-annotations-in-spring-boot/

## TODO
- Create the necessary tables for SpringBatch directly via DDL in the PostgreSQL container.
- Implement deployment using Kubernetes.
- Implement integration with Cucumber.
- Implement integration with SpringFlow.

## TODO DEVELOPMENT
- Document the code.
- Write unit tests.
- Modify the DockerCompose file via a template with the version when executing MAVEN.
- Update the Spring AI version.
- Create a condition based on an environment variable in the Bean to store in the directory or Minio.
- In the Docker environment, correctly display the HATEOAS URL considering the API Gateway.
