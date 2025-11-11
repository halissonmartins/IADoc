## How to Run the Kafka Consumer

- Execute the following commands in the Kafka container console:

**Inside the Kafka container, run these commands to create test consumers and view events:**

```
    cd opt/bitnami/kafka/bin

    kafka-topics.sh --bootstrap-server=localhost:9092 --list

    kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic from.application.documents --from-beginning
    kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic from.application.questions --from-beginning
    kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic from-basic.application.documents --from-beginning

    kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic from.document.documents --from-beginning
    kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic from.question.questions --from-beginning
```

- Command to check if the `kafka-connect` container is running or if the connector was successfully added:

```
    curl -X GET  -H  "Content-Type:application/json" http://localhost:8083/connectors
```

- Commands to add the PostgreSQL connectors to the `kafka-connect` container:

```
    curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @from_application.json

    curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @from_basic_application.json

    curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @from_document_documents.json

    curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @from_question_questions.json

    curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @to_application_documents.json

    curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @to_application_questions.json

    curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @to_document_documents.json

    curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @to_question_documents.json

    curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @to_question_questions.json
```

- Script to test if CDC (Change Data Capture) is working correctly:

```
    INSERT INTO application.documents
    ("name", document_type, document_status_code, created_at, processed_at)
    VALUES('test1', 'pdf', 1, current_timestamp, null);

    INSERT INTO application.questions
    (question, response, question_status_code, document_id, created_at, answered_at)
    VALUES('test1', null, 1, 1, current_timestamp, null);

    -----------------------------

    UPDATE "document".documents
    SET document_status_code=2, processing_started_at=current_timestamp
    WHERE id=1;

    UPDATE "document".documents
    SET document_status_code=3, processed_at=current_timestamp
    WHERE id=1;

    -----------------------------

    UPDATE question.questions
    SET question_status_code=2, processing_started_at=current_timestamp
    WHERE id=1;

    UPDATE question.questions
    SET response='response 21:14', question_status_code=3, answered_at=current_timestamp
    WHERE id=1;

```