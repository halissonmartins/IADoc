# IADoc
Projeto consiste em 3 microsserviços principais: Aplicação (upload de documentos e cadastro de perguntas), Documentos (processamento dos documentos) e Perguntas (processamento das perguntas).

O microserviço de aplicação é responsável por fazer o upload de documentos PDF e o cadastro de perguntas sobre os respectivos através de API REST.
Para fazer a replicação dos dados para os microsserviços de documentos e perguntas utilizei a estratégia CDC (change data capture), onde foi utilizado Confluent Kafka Connect e o plugin do Debezium para o PostgreSQL.
Para racionalizar o uso da GPU os documentos e perguntas não são processados em tempo real. Eles são armazenados e replicados para os respectivos microsserviços.
No microserviço de documentos foi criado um JOB com Spring Batch para fazer a geração argumentada de recuperação (Retrieval-Augmented Generation, RAG) nos documentos e REDIS no armazenamento em forma de vetor.
No microserviço de perguntas foi criado outro JOB com Spring Batch para responder às perguntas cadastradas. 
Os dados para respostas mais precisas são recuperados do VectorDB (REDIS), colocados em template e o Spring AI realiza o chat com o modelo Llama3 para obter as respostas.

# Fluxo de dados entre as bases com o CDC
![Alt text](/asserts/images/FluxoDadosCDC.jpg?raw=true "Diagrama representando o fluxo dos dados entre as bases")

# Programas que precisam ser instalados e iniciados previamente
- Docker Desktop
- Ollama

# Antes de iniciar a execução
- Crie dois diretórios diferentes (ex: /recebidos e /processados) em uma pasta temporária.
	```sh
	mkdir \tmp\recebidos
 	mkdir \tmp\processados
	```

- Criar as variáveis de ambiente UPLOAD_RECEIVED_DIR e PROCESSED_DIR, com os diretórios para recepção(/recebidos) e guarda(/processados) dos arquivos processados.
	```sh
	set UPLOAD_RECEIVED_DIR=C:\tmp\recebidos\
 	set PROCESSED_DIR=C:\tmp\processados\
	```
- Faça o download do modelo llama3
	```sh
	ollama pull llama3
	```

# Passos para execução
- No terminal execute o seguinte comando: docker compose up -d
- Aguarde um momento para o kafka-connect inicializar completamente
- No terminal acesse a pasta do projeto e execute os seguintes comandos para adicionar os conectores do postgres no container do kafka-connect:
	```sh
	curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @./cdc/from_application.json
	
	curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @./cdc/from_basic_application.json
	
	curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @./cdc/from_document_documents.json
	
	curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @./cdc/from_question_questions.json
	
	curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @./cdc/to_document_documents.json
	
	curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @./cdc/to_question_documents.json
	
	curl -X POST  -H  "Content-Type:application/json" http://localhost:8083/connectors -d @./cdc/to_question_questions.json
	```

- Iniciar o microserviço de aplicação
- Iniciar o microserviço de documentos
- Iniciar o microserviço de perguntas
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

- No terminal execute o comando para finalizar todos os containers: docker-compose down

> Nota: Dentro do projeto tem a coleção com todas as requisições REST para importar no Postman ou Insomia.

> Site para download de livros gratuitos: https://www.livrosabertos.abcd.usp.br/portaldelivrosUSP

# Referências

## CDC
- https://debezium.io/documentation/reference/stable/
- https://developers.redhat.com/articles/2023/07/06/how-use-debezium-smt-groovy-filter-routing-events
- https://youtu.be/0_Fm-xr3LY8?si=v2rjM9mDmOb1icrA
- https://medium.com/trendyol-tech/debezium-with-simple-message-transformation-smt-4f5a80c85358

## Spring AI e VectorDB
- https://youtu.be/4-rG2qsTrAs?si=0LFTj5qkzjGwMFxT
- https://youtu.be/ZoPVGrB8iHU?si=zNmMAC6962DvcsMl
- https://docs.spring.io/spring-ai/reference/index.html


## Upload de arquivos
- https://spring.io/guides/gs/uploading-files
- https://medium.com/@patelsajal2/how-to-create-a-spring-boot-rest-api-for-multipart-file-uploads-a-comprehensive-guide-b4d95ce3022b

## Spring Batch
- https://spring.io/guides/gs/batch-processing
- https://www.toptal.com/spring/spring-batch-tutorial
- https://medium.com/@rostyslav.ivankiv/introduction-to-spring-batch-a2f39454573f
- https://tucanoo.com/spring-batch-example-building-a-bulk-contact-importer/
- https://stackoverflow.com/questions/29286699/repeating-a-step-x-times-in-spring-batch

## Minio
- https://gurselgazii.medium.com/integrating-minio-with-spring-boot-a-guide-to-simplified-object-storage-525d5a7686cc
- https://medium.com/@kapincev/a-developers-guide-to-integrating-minio-with-angular-and-spring-boot-3d77c13aefc7

# TODO
- Implementar o deploy usando Kubernetes
- Implementar integração com o Cucumber
- Implementar integração com o SpringFlow

# TODO DESENVOLVIMENTO
- Escrever os testes unitários
- Alterar o arquivo DockerCompose com a versão ao executar profile o plugin do MAVEN
- Finalizar o README
- Atualizar a versão do Spring AI
- Configura o ChatService corretamente devido a atualização