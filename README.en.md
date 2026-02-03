# IADoc - Microservices project with document upload, question processing using RAG from documents, and the use of Kafka, Debezium, CDC, Spring Batch, Spring Boot, Minio, Spring AI, Ollama, and Llama 3 for accurate responses.

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Kafka](https://img.shields.io/badge/Kafka-Latest-black.svg)](https://kafka.apache.org/)
[![Redis](https://img.shields.io/badge/Redis-VectorDB-red.svg)](https://redis.io/)

> Microservices system for PDF document upload, intelligent processing via RAG (Retrieval-Augmented Generation), and automated question answering using generative AI.

## 📋 Table of Contents

- [About the Project](#-about-the-project)
- [Architecture](#-architecture)
- [Key Features](#-key-features)
- [Technologies Used](#-technologies-used)
- [Prerequisites](#-prerequisites)
- [Installation and Setup](#-installation-and-setup)
- [Running the Application](#-running-the-application)
- [API Usage](#-api-usage)
- [Project Structure](#-project-structure)
- [Data Flow](#-data-flow)
- [Roadmap](#-roadmap)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact and Support](#-contact-and-support)
- [References](#-references)

## 🎯 About the Project

**IADoc** is a microservices platform developed to intelligently process PDF documents, allowing users to ask questions about document content and receive accurate AI-generated answers.

### Problem it Solves

- **Information Extraction**: Facilitates extraction of relevant information from large volumes of PDF documents
- **Intelligent Queries**: Enables natural language questions about document content
- **Asynchronous Processing**: Optimizes GPU resource usage through batch processing
- **Scalability**: Microservices architecture allows individual component scaling

### How it Works

1. **Document Upload**: User uploads PDFs via REST API
2. **Data Replication**: System uses CDC (Change Data Capture) to replicate data between microservices
3. **RAG Processing**: Documents are processed and vectorized using RAG techniques
4. **Vector Storage**: Vectors are stored in Redis for efficient retrieval
5. **Question & Answer**: User submits questions that are answered using context retrieved from VectorDB

## 🏗️ Architecture

### Microservices Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        API Gateway (8501)                        │
│                     (Spring Cloud Gateway)                       │
└────────────┬────────────────────────────────────┬────────────────┘
             │                                    │
    ┌────────▼────────┐                  ┌────────▼────────┐
    │   Discovery     │                  │   Application   │
    │    (Eureka)     │◄─────────────────┤  Microservice   │
    │     (8761)      │                  │     (8080)      │
    └─────────────────┘                  └────────┬────────┘
                                                  │
                                         ┌────────▼────────┐
                                         │   PostgreSQL    │
                                         │   (Documents)   │
                                         └────────┬────────┘
                                                  │
                                         ┌────────▼────────┐
                                         │  Debezium CDC   │
                                         │  Kafka Connect  │
                                         └────────┬────────┘
                                                  │
                          ┌───────────────────────┼───────────────────────┐
                          │                       │                       │
                  ┌───────▼────────┐      ┌───────▼────────┐      ┌──────▼──────┐
                  │  Kafka Topic   │      │  Kafka Topic   │      │   MinIO     │
                  │  (Documents)   │      │  (Questions)   │      │  (Storage)  │
                  └───────┬────────┘      └───────┬────────┘      └─────────────┘
                          │                       │
              ┌───────────▼───────────┐  ┌────────▼────────────┐
              │    Document           │  │    Question         │
              │    Processor          │  │    Processor        │
              │  (Spring Batch)       │  │  (Spring Batch)     │
              │  (8081)               │  │  (8082)             │
              └───────────┬───────────┘  └────────┬────────────┘
                          │                       │
                  ┌───────▼────────┐      ┌───────▼────────┐
                  │  Redis Vector  │      │  Ollama LLM    │
                  │      DB        │◄─────┤  (Llama 3)     │
                  └────────────────┘      └────────────────┘
```

### Main Components

#### 1. **Microservice Application** (Port 8080)
- **Responsibility**: Upload management and question registration
- **Endpoints**: REST API for PDF upload and questions CRUD
- **Database**: PostgreSQL (stores document and question metadata)
- **Storage**: MinIO (stores PDF files)

#### 2. **Microservice Document Processor** (Port 8081)
- **Responsibility**: Document processing using RAG
- **Technology**: Spring Batch (scheduled jobs)
- **Processing**:
  - Text extraction from PDFs
  - Chunking (splitting into smaller pieces)
  - Embedding (vectorization with Llama 3)
  - Storage in Redis VectorDB
- **Consumes**: Kafka document topic

#### 3. **Microservice Question Processor** (Port 8082)
- **Responsibility**: Answer registered questions
- **Technology**: Spring Batch (scheduled jobs)
- **Processing**:
  - Relevant context retrieval from VectorDB
  - Prompt construction with template
  - Answer generation via Llama 3 (Ollama)
  - Answer persistence in database
- **Consumes**: Kafka questions topic

#### 4. **Microservice Discovery** (Port 8761)
- **Responsibility**: Service Discovery
- **Technology**: Eureka Server
- **Function**: Microservices registration and discovery

#### 5. **Microservice API Gateway** (Port 8501)
- **Responsibility**: Single entry gateway
- **Technology**: Spring Cloud Gateway
- **Function**: Routing, load balancing and security

### Infrastructure

#### Change Data Capture (CDC)
```
PostgreSQL → Debezium Connector → Kafka Connect → Kafka Topics
```

- **Debezium**: Captures PostgreSQL changes in real-time
- **Kafka Connect**: Orchestrates Debezium connectors
- **Kafka Topics**: Communication channels between microservices

#### Vector Database
- **Redis**: Vector embeddings storage
- **Similarity Search**: Efficient retrieval of relevant context

#### Object Storage
- **MinIO**: S3-compatible storage for PDF files

## ✨ Key Features

### 1. Document Upload
- ✅ Multiple PDF file upload simultaneously
- ✅ Secure storage in MinIO
- ✅ Metadata persisted in PostgreSQL
- ✅ Automatic replication via CDC

### 2. Document Processing (RAG)
- ✅ Automatic text extraction
- ✅ Intelligent content chunking
- ✅ Embedding generation with Llama 3
- ✅ Vector storage in Redis
- ✅ Batch processing with Spring Batch

### 3. Question and Answer System
- ✅ Question registration linked to documents
- ✅ Relevant context retrieval (RAG)
- ✅ Answer generation with Llama 3
- ✅ Question and answer history

### 4. Observability
- ✅ Service Discovery with Eureka
- ✅ Centralized API Gateway
- ✅ Structured logging

## 🛠️ Technologies Used

### Backend
- **Java 25**: Programming language
- **Spring Boot 3.x**: Main framework
- **Spring Cloud**: Microservices (Gateway, Eureka)
- **Spring AI**: LLM integration
- **Spring Batch**: Batch processing
- **Maven**: Dependency management

### Artificial Intelligence
- **Ollama**: LLM model runtime
- **Llama 3**: Model for embeddings
- **Llama 3**: Model for answer generation

### Data and Messaging
- **PostgreSQL**: Relational database
- **Redis**: Vector Database
- **Apache Kafka**: Message Broker
- **Confluent Kafka Connect**: CDC platform
- **Debezium**: PostgreSQL CDC connector

### Storage and Containerization
- **MinIO**: Object Storage (S3-compatible)
- **Docker**: Containerization
- **Docker Compose**: Local orchestration

## 📋 Prerequisites

Before starting, make sure you have installed:

- **Docker Desktop** (with at least 8GB RAM allocated)
- **Java 25** ([Download here](https://openjdk.org/))
- **Maven 3.9+** ([Download here](https://maven.apache.org/))
- **Git** ([Download here](https://git-scm.com/))

### System Requirements
- **RAM**: Minimum 16GB (recommended 32GB)
- **Disk**: Minimum 20GB free
- **GPU**: Optional, but recommended for faster processing

## 🚀 Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/halissonmartins/IADoc.git
cd IADoc
```

### 2. Configure Environment Variables

#### Windows
```cmd
set UPLOAD_RECEIVED_DIR=D:\IADoc\data\documents\received\
```

#### Linux/Mac
```bash
export UPLOAD_RECEIVED_DIR=/home/user/IADoc/data/documents/received/
```

> ⚠️ **Important**: Create the directory before running:
> ```bash
> mkdir -p data/documents/received
> ```

### 3. Configure Docker Hub (Optional)

If deploying images:

**In `pom.xml` file (project root)**:
```xml
<properties>
    <user.docker.hub>YOUR_DOCKERHUB_USERNAME</user.docker.hub>
</properties>
```

**In `docker-compose.yml` file**:
```yaml
image: YOUR_DOCKERHUB_USERNAME/microservice-application:latest
```

## 🎮 Running the Application

### Option 1: Running via IDE (Development)

#### Step 1: Start Infrastructure

```bash
docker-compose -f docker-compose-infra.yml up -d
```

Wait 2-3 minutes for all containers to fully initialize. You can check status with:

```bash
docker-compose -f docker-compose-infra.yml ps
```

#### Step 2: Verify Ollama Model Download

On first run, Ollama needs to download Llama 3 model (~4GB). Check progress:

```bash
docker logs -f ollama
```

Wait until you see a message indicating the model has been loaded.

#### Step 3: Start Microservices (in order)

1. **microservice-discovery** (Eureka)
   - Wait until you see: `Started Eureka Server`
   - Access: http://localhost:8761

2. **microservice-api-gateway**
   - Wait until you see: `Netty started on port 8501`

3. **microservice-application**
   - Wait until you see: `Started ApplicationMicroserviceApplication`

4. **microservice-document-processor**
   - Wait until you see: `Started DocumentProcessorMicroserviceApplication`

5. **microservice-question-processor**
   - Wait until you see: `Started QuestionProcessorMicroserviceApplication`

#### Step 4: Stop Infrastructure

```bash
docker-compose -f docker-compose-infra.yml down --volumes
```

### Option 2: Full Docker Execution

#### Step 1: Compile the Project

```bash
mvn clean install
```

#### Step 2: Generate Docker Images

```bash
mvn spring-boot:build-image
```

> ℹ️ This command may take several minutes on first run.

#### Step 3: Start All Containers

```bash
docker-compose up -d
```

#### Step 4: Check Status

```bash
docker-compose ps
```

All services should have "Up" status.

#### Step 5: View Logs

```bash
# Logs from all services
docker-compose logs -f

# Logs from a specific service
docker-compose logs -f microservice-application
```

#### Step 6: Stop All Containers

```bash
docker-compose down --volumes
```

## 📡 API Usage

### Import Collection in Postman/Insomnia

The project includes a ready-made collection:
```
IA Documents.postman_collection.json
```

### Available Endpoints

#### 1. Document Upload

**Endpoint**: `POST /upload`  
**Port**: `8501` (via Gateway)

```bash
curl --location 'http://localhost:8501/upload' \
--form 'files=@"/path/to/document.pdf"'
```

**Success Response**:
```json
{
  "documentId": 1,
  "filename": "document.pdf",
  "status": "UPLOADED",
  "uploadDate": "2026-02-02T10:30:00"
}
```

#### 2. Register Question

**Endpoint**: `POST /questions`  
**Port**: `8501` (via Gateway)

```bash
curl --location 'http://localhost:8501/questions' \
--header 'Content-Type: application/json' \
--data '{
    "question": "What is the main topic of the document?",
    "documentId": 1
}'
```

**Success Response**:
```json
{
  "questionId": 1,
  "question": "What is the main topic of the document?",
  "documentId": 1,
  "status": "PENDING",
  "createdAt": "2026-02-02T10:35:00"
}
```

#### 3. Query Question and Answer

**Endpoint**: `GET /questions/{id}`  
**Port**: `8501` (via Gateway)

```bash
curl --location 'http://localhost:8501/questions/1'
```

**Success Response** (after processing):
```json
{
  "questionId": 1,
  "question": "What is the main topic of the document?",
  "answer": "The document mainly addresses...",
  "documentId": 1,
  "status": "ANSWERED",
  "createdAt": "2026-02-02T10:35:00",
  "answeredAt": "2026-02-02T10:40:00"
}
```

#### 4. List All Questions for a Document

**Endpoint**: `GET /documents/{id}/questions`  
**Port**: `8501` (via Gateway)

```bash
curl --location 'http://localhost:8501/documents/1/questions'
```

### Complete Usage Flow

1. **PDF Upload**: Upload a document
2. **Wait for Processing**: Document job processes in batch (configurable)
3. **Register Questions**: Register one or more questions about the document
4. **Wait for Answers**: Question job processes and generates answers
5. **Query Results**: Retrieve answers via API

### Processing Times (Estimated)

- **Upload**: < 1 second
- **Document Processing**: 2-5 minutes (depending on size)
- **Answer Generation**: 30-60 seconds per question

## 📁 Project Structure

```
IADoc/
├── .github/
│   └── workflows/              # GitHub Actions (CI/CD)
├── asserts/
│   └── images/                 # Documentation images and diagrams
├── cdc/                        # Debezium CDC configurations
│   └── connectors/             # Kafka connectors
├── data/
│   └── documents/
│       └── received/           # Directory for received PDFs
├── database/
│   ├── init-scripts/           # PostgreSQL initialization scripts
│   └── migrations/             # Database migrations
├── iadoc-library/              # Shared library between microservices
│   ├── src/
│   └── pom.xml
├── microservice-api-gateway/   # Entry gateway
│   ├── src/
│   └── pom.xml
├── microservice-application/   # Application service
│   ├── src/
│   └── pom.xml
├── microservice-discovery/     # Eureka Server
│   ├── src/
│   └── pom.xml
├── microservice-document-processor/  # Document processor
│   ├── src/
│   └── pom.xml
├── microservice-question-processor/  # Question processor
│   ├── src/
│   └── pom.xml
├── docker-compose-infra.yml    # Infrastructure (Kafka, DB, etc)
├── docker-compose.yml          # All services
├── pom.xml                     # Main POM (parent)
├── README.md                   # Portuguese README
├── README.en.md                # This file
└── LICENSE                     # GPL-3.0 License
```

### Module Descriptions

| Module | Description | Port |
|--------|-------------|------|
| **iadoc-library** | Shared library with DTOs, utils and common configurations | N/A |
| **microservice-discovery** | Eureka Server for service discovery | 8761 |
| **microservice-api-gateway** | Spring Cloud Gateway for routing | 8501 |
| **microservice-application** | REST API for upload and CRUD | 8080 |
| **microservice-document-processor** | Batch jobs to process documents | 8081 |
| **microservice-question-processor** | Batch jobs to process questions | 8082 |

## 🔄 Data Flow

### 1. Document Upload Flow

```
Client → API Gateway → Application Service → PostgreSQL
                                           ↓
                                        MinIO
                                           ↓
                                   Debezium (CDC)
                                           ↓
                                     Kafka Topic
                                           ↓
                             Document Processor Service
                                           ↓
                                   Spring Batch Job
                                           ↓
                             Text Extraction (PDF)
                                           ↓
                                  Text Chunking
                                           ↓
                             Embedding (Llama 3 / Ollama)
                                           ↓
                                   Redis Vector DB
```

### 2. Question and Answer Flow

```
Client → API Gateway → Application Service → PostgreSQL
                                                  ↓
                                          Debezium (CDC)
                                                  ↓
                                            Kafka Topic
                                                  ↓
                                  Question Processor Service
                                                  ↓
                                         Spring Batch Job
                                                  ↓
                             Context Search (Redis VectorDB)
                                                  ↓
                                   Prompt Construction
                                                  ↓
										LLM (Llama 3 / Ollama)
                                                  ↓
                                    Generated Answer
                                                  ↓
                                    PostgreSQL (Update)
                                                  ↓
                                  Client (via GET Query)
```

### 3. CDC Diagram (Change Data Capture)

![CDC Flow](asserts/images/FluxoDadosCDC.jpg)

**CDC Flow Explanation**:

1. **Write-Ahead Log (WAL)**: PostgreSQL records all changes in WAL
2. **Debezium Connector**: Reads WAL and converts to Kafka events
3. **Kafka Topics**: Publishes change events (INSERT, UPDATE, DELETE)
4. **Consumers**: Microservices consume events and process

**CDC Advantages**:
- ✅ Low latency in replication
- ✅ Zero impact on main application
- ✅ Event order guarantee
- ✅ Complete change history

## 🗺️ Roadmap

### ✅ Completed
- [x] Microservices architecture
- [x] PDF document upload
- [x] CDC with Debezium and Kafka
- [x] RAG processing with Spring AI
- [x] Vector DB with Redis
- [x] Jobs with Spring Batch
- [x] API Gateway and Service Discovery

### 🚧 In Development
- [ ] Unit and integration tests
- [ ] Code documentation (JavaDoc)
- [ ] Automatic Spring Batch table creation via DDL

### 📅 Planned
- [ ] Kubernetes deployment
- [ ] Cucumber integration (BDD)
- [ ] Spring Flow integration
- [ ] Monitoring with Prometheus and Grafana
- [ ] Authentication and authorization (OAuth2/JWT)
- [ ] Web interface (Frontend)
- [ ] Support for other formats (DOCX, TXT, etc)
- [ ] Streaming answer API (WebSocket)
- [ ] Distributed cache (Hazelcast)
- [ ] Multi-tenant support

### 💡 Future Ideas
- [ ] Domain-specific model fine-tuning
- [ ] Multi-language support
- [ ] Sentiment analysis in documents
- [ ] Automatic summary generation
- [ ] Export answers to PDF/DOCX

## 🤝 Contributing

Contributions are very welcome! To contribute:

1. **Fork the Project**
2. **Create a Branch** for your feature (`git checkout -b feature/MyFeature`)
3. **Commit your changes** (`git commit -m 'feat: Add MyFeature'`)
4. **Push to the Branch** (`git push origin feature/MyFeature`)
5. **Open a Pull Request**

### Commit Convention

We follow [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation
- `style:` Code formatting
- `refactor:` Refactoring
- `test:` Tests
- `chore:` Maintenance tasks

### Code of Conduct

This project adopts a Code of Conduct. By participating, you agree to follow its guidelines.

## 📄 License

This project is licensed under the **GNU General Public License v3.0** - see the [LICENSE](LICENSE) file for details.

**License Summary**:
- ✅ Commercial use allowed
- ✅ Modification allowed
- ✅ Distribution allowed
- ⚠️ Must maintain same license
- ⚠️ Must disclose source code
- ⚠️ Must include license notice

## 📞 Contact and Support

### Author
**Halisson Martins**

- GitHub: [@halissonmartins](https://github.com/halissonmartins)
- LinkedIn: [linkedin.com/in/halissonmartins](https://linkedin.com/in/halissonmartins) *(adjust if needed)*
- Email: *(add if you wish)*

### Report Issues

Found a bug or have a suggestion? [Open an issue](https://github.com/halissonmartins/IADoc/issues/new).

### Useful Resources

- 📚 [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/index.html)
- 📚 [Debezium Documentation](https://debezium.io/documentation/reference/stable/)
- 📚 [Ollama Documentation](https://ollama.ai/docs)
- 📚 [Spring Batch Guide](https://spring.io/guides/gs/batch-processing)

## 📚 References

### Change Data Capture (CDC)
- [Debezium Documentation](https://debezium.io/documentation/reference/stable/)
- [Using Debezium SMT for Groovy Filter Routing Events](https://developers.redhat.com/articles/2023/07/06/how-use-debezium-smt-groovy-filter-routing-events)
- [Debezium Tutorial - YouTube](https://youtu.be/0_Fm-xr3LY8?si=v2rjM9mDmOb1icrA)
- [Debezium with SMT - Medium](https://medium.com/trendyol-tech/debezium-with-simple-message-transformation-smt-4f5a80c85358)

### Spring AI and Vector Database
- [Spring AI with Ollama - YouTube](https://youtu.be/4-rG2qsTrAs?si=0LFTj5qkzjGwMFxT)
- [RAG with Spring AI - YouTube](https://youtu.be/ZoPVGrB8iHU?si=zNmMAC6962DvcsMl)
- [Spring AI Reference Documentation](https://docs.spring.io/spring-ai/reference/index.html)

### File Upload
- [Spring File Upload Guide](https://spring.io/guides/gs/uploading-files)
- [Multipart File Upload REST API - Medium](https://medium.com/@patelsajal2/how-to-create-a-spring-boot-rest-api-for-multipart-file-uploads-a-comprehensive-guide-b4d95ce3022b)

### Spring Batch
- [Spring Batch Processing Guide](https://spring.io/guides/gs/batch-processing)
- [Spring Batch Tutorial - Toptal](https://www.toptal.com/spring/spring-batch-tutorial)
- [Introduction to Spring Batch - Medium](https://medium.com/@rostyslav.ivankiv/introduction-to-spring-batch-a2f39454573f)
- [Spring Batch Contact Importer](https://tucanoo.com/spring-batch-example-building-a-bulk-contact-importer/)
- [Repeating Steps in Spring Batch - Stack Overflow](https://stackoverflow.com/questions/29286699/repeating-a-step-x-times-in-spring-batch)

### MinIO Object Storage
- [Integrating MinIO with Spring Boot - Medium](https://gurselgazii.medium.com/integrating-minio-with-spring-boot-a-guide-to-simplified-object-storage-525d5a7686cc)
- [MinIO with Angular and Spring Boot - Medium](https://medium.com/@kapincev/a-developers-guide-to-integrating-minio-with-angular-and-spring-boot-3d77c13aefc7)

### Conditional Annotations
- [Conditional Annotations in Spring Boot](https://codingnconcepts.com/spring-boot/conditional-annotations-in-spring-boot/)

### Free Books for Testing
- [Open Books USP](https://www.livrosabertos.abcd.usp.br/portaldelivrosUSP)

---

<div align="center">

**⭐ If this project was useful to you, consider giving it a star!**

[![Star on GitHub](https://img.shields.io/github/stars/halissonmartins/IADoc.svg?style=social)](https://github.com/halissonmartins/IADoc)

</div>
