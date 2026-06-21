-- Migration: coluna tipo (discriminador JPA), alinhada ao papel

ALTER TABLE Usuario ADD COLUMN tipo VARCHAR(20);

UPDATE Usuario SET tipo = papel WHERE tipo IS NULL;
