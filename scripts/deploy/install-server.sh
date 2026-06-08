#!/usr/bin/env bash
# Bootstrap único do servidor para Web1 Freelancers.
# Uso (como root): bash scripts/deploy/install-server.sh
set -euo pipefail

DEPLOY_PATH="${DEPLOY_PATH:-/var/www/freelancers}"
DEPLOY_USER="${DEPLOY_USER:-deploy}"
DOMAIN="${DOMAIN:-web1.marlonhenq.dev}"
KEY_PATH="${KEY_PATH:-/root/freelancers-deploy-key}"

if [[ "${EUID}" -ne 0 ]]; then
  echo "Execute como root." >&2
  exit 1
fi

echo "==> Instalando pacotes..."
export DEBIAN_FRONTEND=noninteractive
apt-get update -qq
apt-get install -y -qq \
  openjdk-17-jre-headless \
  lua5.4 \
  luarocks \
  libsqlite3-dev \
  sqlite3 \
  apache2 \
  certbot \
  python3-certbot-apache \
  rsync

echo "==> Instalando lsqlite3 para Lua..."
luarocks install lsqlite3

echo "==> Habilitando módulos Apache..."
a2enmod proxy proxy_http rewrite ssl headers
systemctl enable apache2

if ! id "${DEPLOY_USER}" &>/dev/null; then
  echo "==> Criando usuário ${DEPLOY_USER}..."
  useradd -m -s /bin/bash "${DEPLOY_USER}"
fi

echo "==> Criando diretórios em ${DEPLOY_PATH}..."
mkdir -p "${DEPLOY_PATH}"/{app,data,backups/sqlite,scripts/deploy,database/migrations}
chown -R "${DEPLOY_USER}:${DEPLOY_USER}" "${DEPLOY_PATH}"

if [[ ! -f "${KEY_PATH}" ]]; then
  echo "==> Gerando chave SSH para GitHub Actions..."
  ssh-keygen -t ed25519 -f "${KEY_PATH}" -N "" -C "github-actions-freelancers"
fi

mkdir -p "/home/${DEPLOY_USER}/.ssh"
chmod 700 "/home/${DEPLOY_USER}/.ssh"
touch "/home/${DEPLOY_USER}/.ssh/authorized_keys"
grep -qF "$(cat "${KEY_PATH}.pub")" "/home/${DEPLOY_USER}/.ssh/authorized_keys" 2>/dev/null \
  || cat "${KEY_PATH}.pub" >> "/home/${DEPLOY_USER}/.ssh/authorized_keys"
chown -R "${DEPLOY_USER}:${DEPLOY_USER}" "/home/${DEPLOY_USER}/.ssh"
chmod 600 "/home/${DEPLOY_USER}/.ssh/authorized_keys"

echo "==> Configurando sudo para restart do serviço..."
cat > /etc/sudoers.d/freelancers-deploy <<EOF
${DEPLOY_USER} ALL=(ALL) NOPASSWD: /bin/systemctl restart freelancers, /bin/systemctl status freelancers, /bin/systemctl is-active freelancers, /usr/bin/systemctl restart freelancers, /usr/bin/systemctl status freelancers, /usr/bin/systemctl is-active freelancers
EOF
chmod 440 /etc/sudoers.d/freelancers-deploy

echo "==> Instalando unit systemd..."
if [[ -f "${DEPLOY_PATH}/deploy/systemd/freelancers.service" ]]; then
  cp "${DEPLOY_PATH}/deploy/systemd/freelancers.service" /etc/systemd/system/freelancers.service
else
  echo "AVISO: copie deploy/systemd/freelancers.service após o primeiro deploy." >&2
fi
systemctl daemon-reload
systemctl enable freelancers 2>/dev/null || true

echo "==> Configurando Apache para ${DOMAIN}..."
if [[ -f "${DEPLOY_PATH}/deploy/apache/web1.marlonhenq.dev.conf" ]]; then
  cp "${DEPLOY_PATH}/deploy/apache/web1.marlonhenq.dev.conf" "/etc/apache2/sites-available/${DOMAIN}.conf"
  a2ensite "${DOMAIN}.conf" 2>/dev/null || true
  systemctl reload apache2
fi

echo "==> Cron de backup diário..."
CRON_LINE="0 3 * * * ${DEPLOY_PATH}/scripts/deploy/backup-sqlite.sh >> ${DEPLOY_PATH}/backups/backup.log 2>&1"
(crontab -u "${DEPLOY_USER}" -l 2>/dev/null | grep -v 'backup-sqlite.sh' || true; echo "${CRON_LINE}") | crontab -u "${DEPLOY_USER}" -

cat <<EOF

Bootstrap concluído.

Próximos passos:
1. Adicione ao GitHub Secrets (repo Web1---Projeto):
   SSH_HOST=$(curl -s ifconfig.me 2>/dev/null || echo 'SEU_IP')
   SSH_PORT=22
   SSH_USER=${DEPLOY_USER}
   SSH_PRIVATE_KEY=$(cat "${KEY_PATH}")
   DEPLOY_PATH=${DEPLOY_PATH}
   WEB_SERVER_USER=www-data

2. DNS Cloudflare: web1 -> IP do servidor

3. HTTPS: certbot --apache -d ${DOMAIN}

4. Primeiro deploy via merge na main.

Chave privada salva em: ${KEY_PATH}
EOF
