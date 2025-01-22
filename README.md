# IADoc - Projeto de microsserviços com upload de documentos, processamento de perguntas e uso de Kafka, CDC, Spring Batch, Spring Boot, Minio, Spring AI, RAG e Llama3 para otimização e respostas precisas.
Projeto consiste em 3 microsserviços principais: Aplicação (upload de documentos e cadastro de perguntas), Documentos (processamento dos documentos) e Perguntas (processamento das perguntas).

O microserviço de aplicação é responsável por fazer o upload de documentos PDF e o cadastro de perguntas sobre os respectivos através de API REST.
Para fazer a replicação dos dados para os microsserviços de documentos e perguntas utilizei a estratégia CDC (change data capture), onde foi utilizado Confluent Kafka Connect e o plugin do Debezium para o PostgreSQL.
Para racionalizar o uso da GPU os documentos e perguntas não são processados em tempo real. Eles são armazenados e replicados para os respectivos microsserviços.
No microserviço de documentos foi criado um JOB com Spring Batch para fazer a geração argumentada de recuperação (Retrieval-Augmented Generation, RAG) nos documentos e REDIS no armazenamento em forma de vetor.
No microserviço de perguntas foi criado outro JOB com Spring Batch para responder às perguntas cadastradas. 
Os dados para respostas mais precisas são recuperados do VectorDB (REDIS), colocados em template e o Spring AI realiza o chat com o modelo Llama3 para obter as respostas.

## Ferramentas e frameworks utilizados
- Ollama
- Llama3
- Kafka
- Confluent Kafka Connect
- Debezium
- Postgres SQL
- Redis
- Minio
- Docker
- Spring Boot
- Spring Cloud
- Spring AI
- Spring Batch

## Fluxo de dados entre as bases com o CDC
![Alt text](/asserts/images/FluxoDadosCDC.jpg?raw=true "Diagrama representando o fluxo dos dados entre as bases")

## Programas que precisam ser instalados e iniciados previamente
- Docker Desktop
- JAVA 21
- MAVEN

## Antes de iniciar a execução
- Criar a variável de ambiente UPLOAD_RECEIVED_DIR, contendo o diretório relativo seu PC para recepção(/recebidos) dos arquivos.
	```sh
	set UPLOAD_RECEIVED_DIR=D:\SeuDiretorio\IADoc\data\documents\received\
	```

- No arquivo pom.xml na raiz do projeto alterar a propriedade user.docker.hub colocando seu usuário no DockerHub.
- No arquivo docker-compose.yml na raiz do projeto alterar o caminho das imagens dos microserviços colocando seu usuário no DockerHub.

## Passos para execução via IDE
- No terminal execute o seguinte comando:
	```sh
	docker-compose -f docker-compose-infra.yml up -d
	```

- Iniciar o microserviço microservice-discovery.
- Iniciar o microserviço microservice-api-gateway.
- Iniciar o microserviço microservice-application.
- Iniciar o microserviço microservice-document-processor.
- Iniciar o microserviço microservice-question-processor.
- No terminal execute o comando para finalizar todos os containers. 
	```sh
	docker-compose -f docker-compose-infra.yml down
	```

## Passos para execução de todos os containers via Docker
- Na raiz do projeto execute o comando para gerar os executáveis(JAR) dos microserviços:
	```sh
	mvn clean install
	```
- Na raiz do projeto execute o comando para gerar as imagens dos microserviços:
	```sh
	mvn spring-boot:build-image
	```

- No terminal execute o seguinte comando:
	```sh
	docker-compose -f docker-compose.yml up -d
	```

- Aguardar todos os containers inicializarem.
- No terminal execute o comando para finalizar todos os containers. 
	```sh
	docker-compose -f docker-compose.yml down
	```

## Instruções para utilizar os endpoints
- Fazer o upload de um documento PDF:
	```sh
	curl --location 'http://localhost:8501/upload' --form 'files=@"/algumArquivoPdf.pdf"'
	```

- Cadastrar uma pergunta relacionada ao documento PDF:
	```sh
	curl --location 'http://localhost:8501/questions' --header 'Content-Type: application/json' \
	--data '{
		"question": "Sua pergunta?",
		"documentId": 1
	}'
	``` 

- Após o processamento do documento, a pergunta será respondida
- Consultar a pergunta
	```sh
	curl --location 'http://localhost:8501/questions/1'
	```

> Nota: Dentro do projeto tem a coleção com todas as requisições REST para importar no Postman ou Insomia.

> Site para download de livros gratuitos: https://www.livrosabertos.abcd.usp.br/portaldelivrosUSP

## Referências

### CDC
- https://debezium.io/documentation/reference/stable/
- https://developers.redhat.com/articles/2023/07/06/how-use-debezium-smt-groovy-filter-routing-events
- https://youtu.be/0_Fm-xr3LY8?si=v2rjM9mDmOb1icrA
- https://medium.com/trendyol-tech/debezium-with-simple-message-transformation-smt-4f5a80c85358

### Spring AI e VectorDB
- https://youtu.be/4-rG2qsTrAs?si=0LFTj5qkzjGwMFxT
- https://youtu.be/ZoPVGrB8iHU?si=zNmMAC6962DvcsMl
- https://docs.spring.io/spring-ai/reference/index.html


### Upload de arquivos
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
- Criar as tabelas nescessárias para o SpringBatch diretamente via script.
- Implementar o deploy usando Kubernetes
- Implementar integração com o Cucumber
- Implementar integração com o SpringFlow

## TODO DESENVOLVIMENTO
- Escrever os testes unitários
- Alterar o arquivo DockerCompose através de um template com a versão ao executar o MAVEN
- Atualizar a versão do Spring AI
- Criar uma condição baseada em variável de ambiente no Bean para gravar no diretório ou Minio.
- No ambiente docker apresentar corretamente a URL do hateoas considerando o API Gateway.