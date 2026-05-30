# Coupon Manager API

API Rest desenvolvida para gerenciamento de cupons.

## Sobre o projeto

O Coupon Manager é uma aplicação responsável por realizar operações de cadastro, consulta e remoção de cupons via API REST.

O projeto foi desenvolvido utilizando a seguinte arquitetura em camadas:

* Config
* Controller
* Service
* DTO
* Repository
* Entity

---

## Tecnologias Utilizadas

### Backend

* Java 21
* Spring Boot
* Spring Data JPA
* Jakarta Validation
* Maven

### Banco de Dados

* H2

### Testes

* JUnit 5
---

## Arquitetura

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

---

# Executando o Projeto

## Pré-requisitos

* Java 21+
* Maven 3.9+

* Atenção: Se for rodar a aplicação com Docker (Docker Desktop é pré-requisito)
---

## Clonar projeto

```bash
git clone https://github.com/NogueiraLn/coupon-manager.git
```

```bash
cd coupon-manager
```

## Executar aplicação

```bash
mvn spring-boot:run
```

# Executar aplicação via Docker

```bash
docker-compose up -d
```


A aplicação estará disponível em:

```text
http://localhost:8080
```

---

# Endpoints

## Buscar cupom por ID

### Request

```http
GET /coupon/{id}
```

### Exemplo

```http
GET /coupon/e13c4d5c-0f65-4f67-8e89-d97f0d9b68d4
```

### Response Body

```json
{
    "code": "ABC033",
    "description": "Coupon Testing",
    "discountValue": 0.6,
    "expirationDate": "2026-06-01T17:18:46.577Z",
    "id": "e13c4d5c-0f65-4f67-8e89-d97f0d9b68d4",
    "published": false,
    "redeemed": false,
    "status": "ACTIVE"
}
```

### Status HTTP

| Código | Descrição            |
| ------ | -------------------- |
| 200    | Cupom encontrado     |
| 404    | Cupom não encontrado |

---

## Criar cupom

### Request

```http
POST /coupon
```

### Request Body

```json
{
    "code": "ABC033",
    "description": "Coupon to Test",
    "discountValue": 0.8,
    "expirationDate": "2026-07-30T17:18:46.577Z",
    "published": false
}
```

### Response Body

```json
{
    "code": "ABC033",
    "description": "Coupon to Test",
    "discountValue": 0.8,
    "expirationDate": "2026-07-30T17:18:46.577Z",
    "id": "108c8acf-b370-4a7d-ab2f-d0e5f80c9979",
    "published": false,
    "redeemed": false,
    "status": "ACTIVE"
}
```

### Status HTTP

| Código | Descrição          |
| ------ | ------------------ |
| 201    | Criado com sucesso |
| 400    | Dados inválidos    |

---

## Remover cupom

### Request

```http
DELETE /coupon/{id}
```

### Exemplo

```http
DELETE /coupon/e13c4d5c-0f65-4f67-8e89-d97f0d9b68d4
```

### Status HTTP

| Código | Descrição            |
| ------ | -------------------- |
| 204    | Removido com sucesso |
| 404    | Cupom não encontrado |

---

# Autor
Lucas Nogueira

GitHub:
https://github.com/NogueiraLn
