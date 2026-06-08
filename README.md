# FreelancersJPA

Sistema para Contratação de Desenvolvedores (Freelancers) — Web1 UFSCar.

Produção: [web1.marlonhenq.dev](https://web1.marlonhenq.dev)

## Status atual (Sprints 1–2)

- Modelo único `Usuario` com papéis (`ADMIN`, `EMPRESA`, `DESENVOLVEDOR`)
- UI escura estilo [MarlonHenq.dev](https://marlonhenq.dev)
- Login com Spring Security + BCrypt

## Executar

```bash
make run
```

Acesse `http://localhost:8080`

### Logins de demonstração

| Papel | E-mail | Senha |
|-------|--------|-------|
| Admin | admin@freelancers.com | admin123 |
| Empresa | contato@techcorp.com | senha123 |
| Desenvolvedor | joao@dev.com | dev123 |

## Comandos

```bash
make build
make test
make clean
```

## Próximos passos (Sprint 3+)

- CRUD admin (R1, R2)
- Projetos da empresa (R3, R6)
- Listagem pública e propostas (R4, R5, R7)
- E-mail, i18n, validação (R8–R10)
- REST API (AA-2)
