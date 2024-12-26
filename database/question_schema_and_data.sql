-- Criação do schema question
CREATE SCHEMA question;

--** Criação da tabela document_status
CREATE TABLE question.document_status (
    code INT PRIMARY KEY,
    description VARCHAR(200) NOT NULL
);

-- Criação da tabela documents
CREATE TABLE question.documents (
    id BIGINT PRIMARY KEY,
    name VARCHAR(400) NOT NULL,
	document_status_code INT NOT NULL,
	FOREIGN KEY (document_status_code) REFERENCES question.document_status(code)
);

--** Criação da tabela question_status
CREATE TABLE question.question_status (
    code INT PRIMARY KEY,
    description VARCHAR(200) NOT NULL
);

-- Criação da tabela documents
CREATE TABLE question.questions (
    id BIGINT PRIMARY KEY,
    question VARCHAR(400) NOT NULL,
	response TEXT,
	question_status_code INT NOT NULL,
	document_id BIGINT,
	created_at TIMESTAMP NOT NULL,
	processing_started_at TIMESTAMP,
	answered_at TIMESTAMP,
	FOREIGN KEY (question_status_code) REFERENCES question.question_status(code),
	FOREIGN KEY (document_id) REFERENCES question.documents(id)
);

BEGIN;

-- Inserindo registros na tabela document_status
INSERT INTO question.document_status (code, description) VALUES
(1, 'Imported'),
(2, 'Processing started'),
(3, 'Successfully processed'),
(4, 'Erro');

-- Inserindo registros na tabela question_status
INSERT INTO question.question_status (code, description) VALUES
(1, 'Created'),
(2, 'Processing started'),
(3, 'Successfully answered'),
(4, 'Erro');

COMMIT;