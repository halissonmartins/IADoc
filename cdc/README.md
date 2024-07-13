## Como Executar o kafka consumer

- Executar os seguintes comandos no console do container kafka:

* Dentro do container do Kafka executar os seguintes comandos para criar consumidores de teste e visualizar os eventos:
```
cd opt/bitnami/kafka/bin

kafka-topics.sh --bootstrap-server=localhost:9092 --list

kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic from.application.documents --from-beginning
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic from.application.questions --from-beginning
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic from-basic.application.documents --from-beginning

kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic from.document.documents --from-beginning
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic from.question.questions --from-beginning

```

- Comando para conferir se o container do kafka-connect está ON ou se o conector foi adicionado com sucesso:
```
curl -X GET  -H  "Content-Type:application/json" http://localhost:8083/connectors
```

- Comandos para adicionar os conectores do postgres no container do kafka-connect:
```
curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @from_application.json

curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @from_basic_application.json

curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @from_document_documents.json

curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @from_question_questions.json

curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @to_document_documents.json

curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @to_question_documents.json

curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @to_question_questions.json
```

- Script para testar se o CDC está funcionando corretamente:
```

INSERT INTO application.documents
("name", document_type, document_status_code, created_at, processed_at)
VALUES('teste1', 'pdf', 1, current_timestamp, null);

INSERT INTO application.questions
(question, response, question_status_code, document_id, created_at, answered_at)
VALUES('teste1', null, 1, 1, current_timestamp, null);

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
SET response='resposta 21:14', question_status_code=3, answered_at=current_timestamp
WHERE id=1;

```