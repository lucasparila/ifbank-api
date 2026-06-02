# IFBank API

API REST bancária desenvolvida como projeto acadêmico utilizando Java, Spring Boot, Oracle Database e Docker.

## Tecnologias

- Java 21
- Spring Boot
- Spring Data JPA
- Oracle Database
- Docker
- Maven

## Funcionalidades

- Cadastro de usuários
- Cadastro de clientes
- Gerenciamento de contas bancárias
- Persistência de dados em Oracle

## Executando o projeto

### Docker

```bash
docker compose up --build
```

### Aplicação

A API ficará disponível em:

```text
http://localhost:8080
```

## Estrutura do projeto

```text
src/
├── controller
├── service
├── repository
├── model
└── config
```
