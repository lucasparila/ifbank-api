#!/bin/bash
set -e

echo "=========================================="
echo "Aguardando Oracle estar online..."
echo "=========================================="

# Aguarda o Oracle estar pronto (máx 120 segundos)
for i in {1..120}; do
  if docker exec ifbank_oracle_container bash -c "echo 'SELECT 1 FROM dual;' | sqlplus -S ifbank_database/senha123@FREEPDB1" &>/dev/null; then
    echo "✅ Oracle está online!"
    break
  fi
  echo "  Tentativa $i/120..."
  sleep 1
done

echo ""
echo "=========================================="
echo "Carregando schema.sql no banco..."
echo "=========================================="

# Executa o schema.sql
docker exec -i ifbank_oracle_container sqlplus -S ifbank_database/senha123@FREEPDB1 < ./src/main/resources/schema.sql

echo ""
echo "=========================================="
echo "✅ Schema carregado com sucesso!"
echo "=========================================="
echo ""
echo "Dados de teste criados:"
echo "  Cliente: cliente@ifbank.com / senha123"
echo "  Gerente: gerente@ifbank.com / senha321"
echo "  Conta: 10001-2 (saldo R$ 500.00)"
echo ""
