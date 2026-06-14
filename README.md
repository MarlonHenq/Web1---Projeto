# FreelancersJPA

Sistema para Contratação de Desenvolvedores (Freelancers) — Web1 UFSCar (T6).

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
| DESENVOLVEDOR | `/dev/**` | Propostas (R5, R7 — pendente) |

### REST API (AA-2) — pendente

Base URL prevista: `http://localhost:8080/api` (sem autenticação).

### Status do projeto

| Req | Descrição | Status |
|-----|-----------|--------|
| R1 | CRUD desenvolvedores (admin) | Implementado |
| R2 | CRUD empresas (admin) | Implementado |
| R3 | Cadastro de projetos + anexos (empresa) | Implementado |
| R4 | Listagem pública + filtro por stack | Implementado |
| R5 | Proposta de serviço (desenvolvedor) | Pendente |
| R6 | Listagem de projetos da empresa | Implementado |
| R7 | Listagem de propostas do desenvolvedor | Pendente |
| R8 | Análise de propostas + e-mail | Pendente |
| R9 | Internacionalização PT + EN | Pendente |
| R10 | Validação e tratamento de erros | Parcial |
| AA-2 | REST API | Pendente |

### Deploy em produção

Ver [docs/DEPLOY.md](docs/DEPLOY.md).

## Tecnologias

- Spring MVC, Spring Data JPA, Spring Security, Thymeleaf
- SQLite, Maven, Bootstrap 5
- CI/CD com GitHub Actions
