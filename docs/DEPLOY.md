# Deploy em produção (GitHub Actions + Java + Apache + SQLite)

## Fluxo

1. Desenvolvimento em branch de feature
2. **Pull Request** para `main` (CI: Maven, Gitleaks, Semgrep, IA de segurança ≥ 7/10)
3. Após **merge** → workflow **Deploy Production** envia o JAR via **SSH/rsync**
4. No servidor: migrações Lua, restart systemd, Apache faz proxy para `web1.marlonhenq.dev`

A branch `main` **não deve receber push direto** — use proteção de branch.

---

## Proteger a branch `main`

```bash
./scripts/github/setup-branch-protection.sh MarlonHenq/Web1---Projeto
```

Ou no GitHub: **Settings → Branches → Add rule** para `main`:

- Require a pull request before merging
- Require status checks: **Maven build and test**, **Gitleaks**, **Semgrep**, **AI security review**
- Do not allow bypassing

---

## Secrets do GitHub (Settings → Secrets → Actions)

| Secret | Descrição |
|--------|-----------|
| `SSH_HOST` | IP ou hostname (`192.241.255.195`) |
| `SSH_PORT` | Porta SSH (padrão `22`) |
| `SSH_USER` | Usuário deploy (`deploy`) |
| `SSH_PRIVATE_KEY` | Chave privada OpenSSH (conteúdo completo) |
| `DEPLOY_PATH` | `/var/www/freelancers` |
| `WEB_SERVER_USER` | `www-data` (opcional) |
| `OPENAI_API_KEY` | Chave OpenAI para avaliação de segurança nos PRs |

### Environment `production`

Em **Settings → Environments → production** você pode exigir aprovação manual antes de cada deploy.

---

## Preparar o servidor (primeira vez)

```bash
# No servidor, como root (após copiar o repositório ou rodar bootstrap):
bash /var/www/freelancers/scripts/deploy/install-server.sh
```

Ou use o script local via SSH:

```bash
scp -r scripts/deploy/install-server.sh root@192.241.255.195:/tmp/
ssh root@192.241.255.195 'bash /tmp/install-server.sh'
```

O script instala: Java 17, Lua 5.4 + lsqlite3, Apache, certbot, usuário `deploy`, chave SSH para Actions, systemd e cron de backup.

### HTTPS (Cloudflare + certbot)

1. DNS no Cloudflare: `web1` → IP do servidor (modo **Full** ou **Full Strict**)
2. No servidor: `certbot --apache -d web1.marlonhenq.dev`

### Layout no servidor

```
/var/www/freelancers/
  app/FreelancersJPA.jar
  data/Freelancers.db
  backups/sqlite/
  scripts/migrate.lua
  database/migrations/
  deploy/
```

---

## Migrações (Lua)

Migrações SQL ficam em `database/migrations/*.sql` (ordem lexicográfica).

```bash
lua scripts/migrate.lua --db /var/www/freelancers/data/Freelancers.db
```

O deploy executa migrações automaticamente antes de reiniciar o serviço.

---

## Backup manual

```bash
/var/www/freelancers/scripts/deploy/backup-sqlite.sh
```

Backups em `backups/sqlite/freelancers_YYYY-MM-DD_HHMMSS.sqlite.gz` (retenção 31 dias).

---

## O que o deploy **não** sobrescreve

- `data/Freelancers.db` (dados de produção)
- `backups/`
- `.env` (se existir no servidor)

---

## Avaliação de segurança (IA)

- Workflow: `.github/workflows/ai-security-review.yml`
- Nota mínima: **7/10**
- Sem `OPENAI_API_KEY`: PR não é bloqueado (aviso no comentário)

---

## Troubleshooting

| Problema | Solução |
|----------|---------|
| `systemctl is-active freelancers` falha | `journalctl -u freelancers -n 50` |
| Hibernate validate falha | Conferir se migrações rodaram; schema deve bater com entidades JPA |
| `lua: module 'lsqlite3' not found` | `luarocks install lsqlite3` no servidor |
| Apache 502 | App ainda sem HTTP (profile prod com `web-application-type=none`) — normal até implementar MVC |
