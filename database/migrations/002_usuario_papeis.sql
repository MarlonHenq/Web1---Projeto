-- Migration: unifica Administrador, Empresa e Desenvolvedor em Usuario com papel

DROP TABLE IF EXISTS Anexo;
DROP TABLE IF EXISTS Proposta;
DROP TABLE IF EXISTS Projeto;
DROP TABLE IF EXISTS Desenvolvedor;
DROP TABLE IF EXISTS Empresa;
DROP TABLE IF EXISTS Administrador;
DROP TABLE IF EXISTS Usuario;

CREATE TABLE IF NOT EXISTS Usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email VARCHAR(60) NOT NULL UNIQUE,
    senha VARCHAR(60) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    papel VARCHAR(20) NOT NULL CHECK (papel IN ('ADMIN', 'EMPRESA', 'DESENVOLVEDOR')),
    cnpj VARCHAR(18) UNIQUE,
    descricao VARCHAR(500),
    cpf VARCHAR(14) UNIQUE,
    telefone VARCHAR(15),
    sexo CHAR(1) CHECK (sexo IN ('M', 'F', 'O')),
    dataNascimento DATE
);

CREATE TABLE IF NOT EXISTS Projeto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(2000) NOT NULL,
    stackTecnologica VARCHAR(200) NOT NULL,
    orcamentoEstimado DECIMAL(12,2) NOT NULL,
    prazoEntrega DATE NOT NULL,
    empresa_id BIGINT NOT NULL,
    FOREIGN KEY (empresa_id) REFERENCES Usuario(id)
);

CREATE TABLE IF NOT EXISTS Proposta (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    valor DECIMAL(12,2) NOT NULL,
    prazoEstimado DATE NOT NULL,
    justificativa VARCHAR(2000) NOT NULL,
    dataProposta DATE NOT NULL,
    status VARCHAR(15) NOT NULL CHECK (status IN ('ABERTO', 'NAO_ACEITO', 'ACEITO')),
    desenvolvedor_id BIGINT NOT NULL,
    projeto_id BIGINT NOT NULL,
    FOREIGN KEY (desenvolvedor_id) REFERENCES Usuario(id),
    FOREIGN KEY (projeto_id) REFERENCES Projeto(id),
    UNIQUE (desenvolvedor_id, projeto_id)
);

CREATE TABLE IF NOT EXISTS Anexo (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nomeArquivo VARCHAR(200) NOT NULL,
    caminho VARCHAR(500) NOT NULL,
    projeto_id BIGINT NOT NULL,
    FOREIGN KEY (projeto_id) REFERENCES Projeto(id)
);
