# FreelancersJPA

Sistema para Contratação de Desenvolvedores (Freelancers) — Web1 UFSCar.

Produção: [web1.marlonhenq.dev](https://web1.marlonhenq.dev)

## Status atual

- Modelo único `Usuario` com papéis (`ADMIN`, `EMPRESA`, `DESENVOLVEDOR`)
- UI de login basica
- Login com Spring Security + BCrypt
- Verificações de segurança com IA e cobertura de falhas comuns como no [DC-Hub](https://github.com/patos-ufscar/DC-Hub)
- Deploy com CI/CD do github actions para produção
- Sccript de migração do banco em lua baseado no do PHP do [DC-Hub](https://github.com/patos-ufscar/DC-Hub)


## Executar

```bash
make run
```

Acesse `http://localhost:8080`

### Logins de demonstração (somente ambiente local / MVP)

> **Aviso de segurança (MVP acadêmico):** as credenciais abaixo são **fictícias e apenas para desenvolvimento local**.
> Elas são criadas automaticamente pelo seed (`@Profile("!prod")`) ao rodar `make run` em máquina de desenvolvimento.
> **Não use essas senhas em produção.** Em deploy real, o seed não roda e as senhas devem ser definidas via variáveis de ambiente (`DEMO_*_PASSWORD`) ou cadastro manual.

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
