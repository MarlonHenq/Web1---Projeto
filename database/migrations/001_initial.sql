-- Schema inicial (espelha Hibernate JOINED inheritance + entidades do domínio)

CREATE TABLE IF NOT EXISTS Usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email VARCHAR(60) NOT NULL UNIQUE,
    senha VARCHAR(60) NOT NULL,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Administrador (
    id BIGINT NOT NULL PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuario(id)
);

CREATE TABLE IF NOT EXISTS Empresa (
    id BIGINT NOT NULL PRIMARY KEY,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    descricao VARCHAR(500) NOT NULL,
    FOREIGN KEY (id) REFERENCES Usuario(id)
);

CREATE TABLE IF NOT EXISTS Desenvolvedor (
    id BIGINT NOT NULL PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(15) NOT NULL,
    sexo CHAR(1) NOT NULL CHECK (sexo IN ('M', 'F', 'O')),
    dataNascimento DATE NOT NULL,
    FOREIGN KEY (id) REFERENCES Usuario(id)
);

CREATE TABLE IF NOT EXISTS Projeto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(2000) NOT NULL,
    stackTecnologica VARCHAR(200) NOT NULL,
    orcamentoEstimado DECIMAL(12,2) NOT NULL,
    prazoEntrega DATE NOT NULL,
    empresa_id BIGINT NOT NULL,
    FOREIGN KEY (empresa_id) REFERENCES Empresa(id)
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
    FOREIGN KEY (desenvolvedor_id) REFERENCES Desenvolvedor(id),
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
