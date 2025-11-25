-- Adiciona coluna motivo_cancelamento na tabela consultas
ALTER TABLE consultas 
ADD COLUMN motivo_cancelamento VARCHAR(50) NULL;