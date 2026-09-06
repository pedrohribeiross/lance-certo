# LanceCerto

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

Backend de uma plataforma de leilões online, construído em Java e Spring Boot.

> Projeto pessoal de estudo, sem vínculo, afiliação ou endosso de qualquer empresa ou plataforma de leilões existente. O domínio foi usado como inspiração a partir de regulação pública sobre leilão judicial eletrônico.

## Sobre o projeto

O LanceCerto é uma API REST para gestão de leilões online: cadastro de leilões e lotes, ciclo de vida controlado por estados, busca avançada com múltiplos filtros e registro de lances com validação de valor e de estado. O projeto está em desenvolvimento incremental, com autenticação e disputa em tempo real previstas para as próximas etapas.

## Funcionalidades

- Cadastro completo de leilões e lotes (criar, consultar, editar, remover)
- Ciclo de vida controlado por máquina de estados (leilão: agendado, ativo, encerrado, cancelado; lote: disponível, suspenso)
- Registro de lances com validação de estado (leilão ativo, lote disponível) e de valor (margem mínima de incremento por lote)
- Busca avançada de lotes com filtros combináveis (categoria, faixa de valor, status, palavra-chave), paginação e ordenação
- Contrato de erro padronizado em toda a API

## Tecnologias utilizadas

- Java 21
- Spring Boot 4.x
- Spring Data JPA / Hibernate
- PostgreSQL 16
- Flyway
- Docker / Docker Compose
- Maven
- JUnit 5, Mockito, AssertJ

## Como executar o projeto

```bash
# Clonar o repositório
git clone https://github.com/pedrohribeiross/lance-certo.git
cd lance-certo

# Definir as variáveis de ambiente (host aponta para o Postgres local)
export DB_HOST=localhost DB_NAME=lancecerto DB_USER=lancecerto DB_PASS=lancecerto

# Subir o banco de dados
docker compose up -d

# Subir a aplicação (aplica as migrations do Flyway automaticamente)
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080/api/v1`. Ainda não há documentação OpenAPI/Swagger nesta fase; os endpoints são testáveis via Postman ou Insomnia.

### Rodando os testes

```bash
./mvnw test
```

## Autor

**Pedro Ribeiro**
GitHub: [@pedrohribeiross](https://github.com/pedrohribeiross)
LinkedIn: [linkedin.com/in/pedrohrss](https://www.linkedin.com/in/pedrohrss/)

## Licença

Distribuído sob a licença MIT. Veja o arquivo [LICENSE.md](./LICENSE) para mais detalhes.