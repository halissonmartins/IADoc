-- Criação do schema document
CREATE SCHEMA document;

--** Criação da tabela document_status
CREATE TABLE document.document_status (
    code INT PRIMARY KEY,
    description VARCHAR(200) NOT NULL
);

-- Criação da tabela documents
CREATE TABLE document.documents (
    id BIGINT PRIMARY KEY,
    name VARCHAR(400) NOT NULL,
	document_type VARCHAR(40) NOT NULL,
	document_status_code INT NOT NULL,
	created_at TIMESTAMP NOT NULL,
	processing_started_at TIMESTAMP,
	processed_at TIMESTAMP,
	FOREIGN KEY (document_status_code) REFERENCES document.document_status(code)
);

BEGIN;

-- Inserindo registros na tabela document_status
INSERT INTO document.document_status (code, description) VALUES
(1, 'Imported'),
(2, 'Processing started'),
(3, 'Successfully processed'),
(4, 'Erro');


COMMIT;