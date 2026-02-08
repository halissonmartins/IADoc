# Testes Unitários - IADoc

Este documento descreve os testes unitários criados para os microserviços do projeto IADoc.

## 📋 Visão Geral

Foram criados testes unitários abrangentes para os três microserviços principais:
- **microservice-application**
- **microservice-document-processor**
- **microservice-question-processor**

## 🎯 Objetivo de Cobertura

Meta: **80% de cobertura de código**

## 🧪 Estrutura dos Testes

### microservice-application

#### Controllers
- ✅ `DocumentControllerTest` - Testa endpoints de documentos
- ✅ `FileUploadControllerTest` - Testa upload de arquivos
- ✅ `QuestionControllerTest` - Testa endpoints de perguntas
- ✅ `BaseControllerTest` - Testa controller base

#### Services
- ✅ `DocumentServiceTest` - Testa lógica de negócio de documentos
- ✅ `QuestionServiceTest` - Testa lógica de negócio de perguntas

#### Storage
- ✅ `FileSystemStorageServiceTest` - Testa armazenamento no filesystem
- ✅ `MinioStorageServiceTest` - Testa armazenamento no MinIO

#### Entities & DTOs
- ✅ `EntityTest` - Testa entidades JPA
- ✅ `DtoTest` - Testa objetos de transferência de dados

#### Exception Handling
- ✅ `GlobalExceptionHandlerTest` - Testa tratamento global de exceções

#### Configurations
- ✅ `ConfigTest` - Testa configurações do Spring

### microservice-document-processor

#### Batch Processing
- ✅ `DocumentItemProcessorTest` - Testa processamento de documentos
- ✅ `DocumentInitializeItemProcessorTest` - Testa inicialização de documentos
- ✅ `ListenersTest` - Testa listeners do Spring Batch

#### Storage
- ✅ `StorageServiceTest` - Testa serviços de armazenamento

#### Entities
- ✅ `EntityTest` - Testa entidades JPA

#### Configurations
- ✅ `ConfigTest` - Testa configurações

### microservice-question-processor

#### Batch Processing
- ✅ `QuestionItemProcessorTest` - Testa processamento de perguntas
- ✅ `QuestionInitializeItemProcessorTest` - Testa inicialização de perguntas
- ✅ `ListenersTest` - Testa listeners do Spring Batch

#### Services
- ✅ `ChatServiceTest` - Testa serviço de chat com IA
- ✅ `ChatServiceWithoutDocumentTest` - Testa chat sem documento
- ✅ `AbstractChatServiceTest` - Testa classe abstrata base

#### Entities
- ✅ `EntityTest` - Testa entidades JPA

#### Configurations
- ✅ `ConfigTest` - Testa configurações

## 🚀 Executando os Testes

### Executar todos os testes do projeto

```bash
mvn clean test
```

### Executar testes de um microserviço específico

```bash
# Microservice Application
cd microservice-application
mvn test

# Microservice Document Processor
cd microservice-document-processor
mvn test

# Microservice Question Processor
cd microservice-question-processor
mvn test
```

### Executar uma classe de teste específica

```bash
mvn test -Dtest=DocumentServiceTest
```

### Executar um método de teste específico

```bash
mvn test -Dtest=DocumentServiceTest#testSave_Success
```

## 🔧 Tecnologias Utilizadas

- **JUnit 5** - Framework de testes
- **Mockito** - Framework de mocking
- **Spring Boot Test** - Utilitários de teste do Spring
- **MockMvc** - Testes de controllers REST
- **@TempDir** - Testes de sistema de arquivos

## ✅ Padrões de Teste Aplicados

### 1. Nomenclatura
- Métodos de teste: `test[MethodName]_[Scenario]_[ExpectedResult]`
- Exemplo: `testSave_DocumentAlreadyExists_ShouldThrowException`

### 2. Estrutura AAA (Arrange-Act-Assert)
```java
@Test
void testExample() {
    // Arrange - Preparar dados
    Document document = new Document();
    
    // Act - Executar ação
    Document result = service.save(document);
    
    // Assert - Verificar resultado
    assertNotNull(result);
}
```

### 3. Uso de Mocks
- Mocks para dependências externas
- Verificação de chamadas com `verify()`
- Controle de comportamento com `when()`

### 4. Testes Isolados
- Cada teste é independente
- Setup e teardown adequados
- Sem dependências entre testes

## 📝 Cenários de Teste Cobertos

### Cenários Positivos (Happy Path)
- ✅ Criação bem-sucedida de entidades
- ✅ Busca de registros existentes
- ✅ Processamento correto de arquivos
- ✅ Geração de respostas por IA

### Cenários Negativos (Error Path)
- ✅ Entidade não encontrada
- ✅ Entidade já existe
- ✅ Arquivo vazio ou inválido
- ✅ Falhas de armazenamento

### Casos Especiais (Edge Cases)
- ✅ Valores nulos
- ✅ Listas vazias
- ✅ IDs zero ou negativos
- ✅ Documentos sem perguntas associadas
