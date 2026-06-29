# FreelancersJPA

Sistema para Contratação de Desenvolvedores (Freelancers) — Web1 UFSCar (T6 + T7).

Produção: [web1.marlonhenq.dev](https://web1.marlonhenq.dev)

## Roteiro de execução

### Pré-requisitos

| Ferramenta | Versão |
|------------|--------|
| Java | 17+ |
| Maven | 3.x |
| Lua + lsqlite3 | 5.4 (somente produção / migrações manuais) |

### SGBD e banco de dados

| Ambiente | SGBD | Arquivo do banco |
|----------|------|------------------|
| Desenvolvimento local | SQLite 3 | `./Freelancers.db` (criado automaticamente) |
| Produção | SQLite 3 | `/var/www/freelancers/data/Freelancers.db` |

Em desenvolvimento, o Hibernate recria o schema a cada execução (`spring.jpa.hibernate.ddl-auto=create-drop`).
Em produção, o schema é gerenciado pelas migrações Lua (`ddl-auto=none`).

### Scripts SQL e migrações

| Arquivo | Descrição |
|---------|-----------|
| `database/migrations/001_initial.sql` | Schema legado (herança JOINED — obsoleto) |
| `database/migrations/002_usuario_papeis.sql` | Schema atual (tabela única `Usuario` com papéis) |

**Executar migrações manualmente:**

```bash
lua scripts/migrate.lua --db ./Freelancers.db
# produção:
lua scripts/migrate.lua --db /var/www/freelancers/data/Freelancers.db
```

### Como executar localmente

```bash
make run
```

Acesse `http://localhost:8080`

Outros comandos:

```bash
make build
make test
make clean
```

### Usuários populados (seed de demonstração)

> **Aviso de segurança (MVP acadêmico):** credenciais fictícias, **somente ambiente local**.
> Criadas automaticamente pelo seed (`@Profile("!prod")`) ao rodar `make run`.
> **Não use em produção.** Sobrescreva via variáveis `DEMO_*_PASSWORD` ou cadastre manualmente.

| Papel | E-mail | Senha | Dados extras |
|-------|--------|-------|--------------|
| ADMIN | admin@freelancers.com | admin123 | Nome: Administrador |
| EMPRESA | contato@techcorp.com | senha123 | CNPJ: 12.345.678/0001-90, Nome: TechCorp Ltda |
| DESENVOLVEDOR | joao@dev.com | dev123 | CPF: 123.456.789-00, Nome: João Silva |

**Dados seed adicionais:**

- 1 projeto: "E-commerce Spring Boot" (stack: Java, Spring Boot, Thymeleaf, MySQL)
- 1 proposta com status **ABERTO** (João Silva → E-commerce Spring Boot)
- 1 anexo: `mockup-home.png`

### Papéis e rotas

| Papel | Prefixo de rotas | Funcionalidades |
|-------|------------------|-----------------|
| Público | `/`, `/projetos/**` | Listagem e detalhe de projetos (R4) |
| ADMIN | `/admin/**` | CRUD de desenvolvedores (R1) e empresas (R2) |
| EMPRESA | `/empresa/**` | Cadastro/listagem de projetos (R3, R6) |
| DESENVOLVEDOR | `/dev/**` | Propostas (R5, R7) e análise pela empresa (R8) |

### Configuração de e-mail (Brevo) — R8

Por padrão, e-mails são **impressos no log do terminal** (`MAIL_ENABLED=false`), útil para demo local.

Para envio real via [Brevo](https://www.brevo.com/) (tier gratuito):

1. Crie uma conta gratuita em brevo.com
2. Em *SMTP & API* → gere a chave SMTP
3. Defina as variáveis antes de `make run`:

```bash
export MAIL_ENABLED=true
export MAIL_HOST=smtp-relay.brevo.com
export MAIL_PORT=587
export MAIL_USERNAME=seu-login-smtp@exemplo.com
export MAIL_PASSWORD=sua-chave-smtp
export MAIL_FROM=seu-email-verificado@exemplo.com
```

### REST API (AA-2 / T7)

Base URL: `http://localhost:8080/api` — **sem autenticação** (conforme Obs 2 da atividade).
Os endpoints retornam/recebem JSON e seguem os requisitos da AA-2.

#### Desenvolvedores (CRUD)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/desenvolvedores` | Cria um desenvolvedor |
| GET | `/api/desenvolvedores` | Lista todos os desenvolvedores |
| GET | `/api/desenvolvedores/{id}` | Retorna o desenvolvedor de id |
| PUT | `/api/desenvolvedores/{id}` | Atualiza o desenvolvedor de id |
| DELETE | `/api/desenvolvedores/{id}` | Remove o desenvolvedor de id |

Corpo (POST/PUT):

```json
{
  "email": "novo@dev.com",
  "senha": "dev123",
  "cpf": "111.222.333-44",
  "nome": "Maria Souza",
  "telefone": "(16) 99999-0000",
  "sexo": "F",
  "dataNascimento": "1995-04-20"
}
```

#### Empresas (CRUD)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/empresas` | Cria uma empresa |
| GET | `/api/empresas` | Lista todas as empresas |
| GET | `/api/empresas/{id}` | Retorna a empresa de id |
| PUT | `/api/empresas/{id}` | Atualiza a empresa de id |
| DELETE | `/api/empresas/{id}` | Remove a empresa de id |

Corpo (POST/PUT):

```json
{
  "email": "nova@empresa.com",
  "senha": "senha123",
  "cnpj": "98.765.432/0001-10",
  "nome": "NovaSoft Ltda",
  "descricao": "Consultoria de software"
}
```

#### Projetos e Propostas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/projetos/empresas/{id}` | Cria um projeto na empresa de id |
| GET | `/api/projetos/empresas/{id}` | Lista os projetos da empresa de id |
| GET | `/api/projetos/stacks/{nome}` | Lista projetos filtrados por stack |
| GET | `/api/propostas/projetos/{id}` | Lista as propostas do projeto de id |
| GET | `/api/propostas/desenvolvedores/{id}` | Lista as propostas do desenvolvedor de id |

Corpo (POST de projeto):

```json
{
  "titulo": "App de delivery",
  "descricao": "Aplicativo mobile com backend Spring Boot",
  "stackTecnologica": "Java, Spring Boot, React Native",
  "orcamentoEstimado": 15000.00,
  "prazoEntrega": "2026-12-31"
}
```

#### Exemplos com curl

```bash
# Criar desenvolvedor
curl -X POST http://localhost:8080/api/desenvolvedores \
  -H "Content-Type: application/json" \
  -d '{"email":"novo@dev.com","senha":"dev123","cpf":"111.222.333-44","nome":"Maria Souza","telefone":"(16) 99999-0000","sexo":"F","dataNascimento":"1995-04-20"}'

# Listar empresas
curl http://localhost:8080/api/empresas

# Filtrar projetos por stack
curl http://localhost:8080/api/projetos/stacks/Java
```

Uma collection do Postman pronta para importar está em
[`postman/Freelancers.postman_collection.json`](postman/Freelancers.postman_collection.json).

Códigos de status: `200` (OK), `201` (criado), `204` (removido), `400` (dados inválidos),
`404` (não encontrado), `409` (e-mail/CPF/CNPJ duplicado).

### Status do projeto

| Req | Descrição | Status |
|-----|-----------|--------|
| R1 | CRUD desenvolvedores (admin) | Implementado |
| R2 | CRUD empresas (admin) | Implementado |
| R3 | Cadastro de projetos + anexos (empresa) | Implementado |
| R4 | Listagem pública + filtro por stack | Implementado |
| R5 | Proposta de serviço (desenvolvedor) | Implementado |
| R6 | Listagem de projetos da empresa | Implementado |
| R7 | Listagem de propostas do desenvolvedor | Implementado |
| R8 | Análise de propostas + e-mail | Implementado |
| R9 | Internacionalização PT + EN | Implementado |
| R10 | Validação e tratamento de erros | Implementado |
| AA-2 | REST API | Implementado |

### Deploy em produção

Ver [docs/DEPLOY.md](docs/DEPLOY.md).

## Tecnologias

- Spring MVC, Spring Data JPA, Spring Security, Thymeleaf
- SQLite, Maven, Bootstrap 5
- CI/CD com GitHub Actions
