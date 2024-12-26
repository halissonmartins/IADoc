-- Criação do schema application
CREATE SCHEMA application;

--** Criação da tabela document_status
CREATE TABLE application.document_status (
    code INT PRIMARY KEY,
    description VARCHAR(200) NOT NULL
);

-- Criação da tabela documents
CREATE TABLE application.documents (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(400) NOT NULL,
	document_type VARCHAR(40) NOT NULL,
	document_status_code INT NOT NULL,
	created_at TIMESTAMP NOT NULL,
	processed_at TIMESTAMP,
	FOREIGN KEY (document_status_code) REFERENCES application.document_status(code)
);

--** Criação da tabela question_status
CREATE TABLE application.question_status (
    code INT PRIMARY KEY,
    description VARCHAR(200) NOT NULL
);

-- Criação da tabela questions
CREATE TABLE application.questions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    question VARCHAR(400) NOT NULL,
	response TEXT,
	question_status_code INT NOT NULL,
	document_id BIGINT,
	created_at TIMESTAMP NOT NULL,
	answered_at TIMESTAMP,
	FOREIGN KEY (question_status_code) REFERENCES application.question_status(code),
	FOREIGN KEY (document_id) REFERENCES application.documents(id)
);

BEGIN;

-- Inserindo registros na tabela document_status
INSERT INTO application.document_status (code, description) VALUES
(1, 'Imported'),
(2, 'Processing started'),
(3, 'Successfully processed'),
(4, 'Erro');

-- Inserindo registros na tabela question_status
INSERT INTO application.question_status (code, description) VALUES
(1, 'Created'),
(2, 'Processing started'),
(3, 'Successfully answered'),
(4, 'Erro');

COMMIT;