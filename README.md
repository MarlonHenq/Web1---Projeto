# FreelancersJPA

Sistema para Contratação de Desenvolvedores (Freelancers) — entrega T5 (Spring Data JPA).

Produção: [web1.marlonhenq.dev](https://web1.marlonhenq.dev)

## CI/CD

- **PR → `main`:** Maven build/test, Gitleaks, Semgrep, avaliação de segurança por IA (nota ≥ 7/10)
- **Merge → `main`:** deploy automático via GitHub Actions (SSH + migrações Lua + systemd)

Detalhes em [docs/DEPLOY.md](docs/DEPLOY.md).

## Pré-requisitos

- Java 17+
- Maven 3.8+

O banco SQLite (`Freelancers.db`) é criado automaticamente na raiz do projeto ao executar.

## Executar

```bash
make run
```

Ao subir, a aplicação executa um demo CRUD no console (Create, Read, Update, Delete).

## Outros comandos

```bash
make build    # compila o projeto
make test     # executa os testes
make clean    # limpa artefatos Maven
```
