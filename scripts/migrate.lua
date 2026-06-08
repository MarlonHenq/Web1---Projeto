#!/usr/bin/env lua
--[[
  Aplica migrações SQL pendentes em database/migrations/*.sql (ordem lexicográfica).
  Uso: lua scripts/migrate.lua [--db /caminho/Freelancers.db] [--migrations /caminho/migrations]
]]

local sqlite3 = require("lsqlite3")

local function trim(s)
  return (s:gsub("^%s+", ""):gsub("%s+$", ""))
end

local function parse_args(args)
  local opts = {
    db = os.getenv("DB_PATH") or "/var/www/freelancers/data/Freelancers.db",
    migrations = nil,
  }
  local i = 1
  while i <= #args do
    if args[i] == "--db" then
      opts.db = args[i + 1]
      i = i + 2
    elseif args[i] == "--migrations" then
      opts.migrations = args[i + 1]
      i = i + 2
    else
      io.stderr:write("Argumento desconhecido: " .. args[i] .. "\n")
      os.exit(1)
    end
  end
  if not opts.migrations then
    local script_dir = debug.getinfo(1, "S").source:sub(2):match("(.*/)")
    opts.migrations = (script_dir or "./") .. "../database/migrations"
  end
  return opts
end

local function list_migrations(dir)
  local files = {}
  local handle = io.popen('find "' .. dir .. '" -maxdepth 1 -type f -name "*.sql" | sort')
  if not handle then
    error("Não foi possível listar migrações em: " .. dir)
  end
  for line in handle:lines() do
    if line ~= "" then
      table.insert(files, line)
    end
  end
  handle:close()
  return files
end

local function read_file(path)
  local f = io.open(path, "r")
  if not f then
    error("Arquivo não encontrado: " .. path)
  end
  local content = f:read("*a")
  f:close()
  return content
end

local function ensure_migrations_table(db)
  db:exec([[
    CREATE TABLE IF NOT EXISTS schema_migrations (
      version TEXT PRIMARY KEY,
      applied_at TEXT NOT NULL
    );
  ]])
end

local function is_applied(db, version)
  local applied = false
  local stmt = db:prepare("SELECT 1 FROM schema_migrations WHERE version = ? LIMIT 1")
  stmt:bind_values(version)
  for _ in stmt:nrows() do
    applied = true
  end
  stmt:finalize()
  return applied
end

local function mark_applied(db, version)
  local stmt = db:prepare("INSERT INTO schema_migrations (version, applied_at) VALUES (?, datetime('now'))")
  stmt:bind_values(version)
  local ok = stmt:step()
  stmt:finalize()
  if ok ~= sqlite3.DONE then
    error("Falha ao registrar migração: " .. version)
  end
end

local function apply_migration(db, path)
  local version = path:match("([^/]+)$")
  if is_applied(db, version) then
    print("SKIP " .. version)
    return
  end

  local sql = read_file(path)
  if trim(sql) == "" then
    mark_applied(db, version)
    print("SKIP (vazio) " .. version)
    return
  end

  print("APPLY " .. version)
  db:exec("BEGIN")
  local result = db:exec(sql)
  if result ~= sqlite3.OK then
    db:exec("ROLLBACK")
    error("Erro na migração " .. version .. ": " .. db:errmsg())
  end
  mark_applied(db, version)
  db:exec("COMMIT")
  print("OK " .. version)
end

local function main(args)
  local opts = parse_args(args or {})
  local migrations_dir = opts.migrations

  local db_dir = opts.db:match("(.*/)")
  if db_dir and db_dir ~= "" then
    os.execute('mkdir -p "' .. db_dir .. '"')
  end

  local db = sqlite3.open(opts.db)
  if not db then
    io.stderr:write("Não foi possível abrir o banco: " .. opts.db .. "\n")
    os.exit(1)
  end

  db:exec("PRAGMA foreign_keys = ON;")
  ensure_migrations_table(db)

  local files = list_migrations(migrations_dir)
  if #files == 0 then
    print("Nenhuma migração encontrada em " .. migrations_dir)
    db:close()
    return
  end

  for _, path in ipairs(files) do
    local ok, err = pcall(apply_migration, db, path)
    if not ok then
      io.stderr:write(err .. "\n")
      db:close()
      os.exit(1)
    end
  end

  db:close()
  print("Migrações concluídas.")
end

main(arg)
