# 🏥 Tech Challenge - Sistema de Agendamento Hospitalar

## 📌 Sobre o Projeto

Este projeto implementa um sistema de agendamento de consultas médicas baseado em **arquitetura de microsserviços**, com comunicação assíncrona via mensageria.

O sistema permite:

* Cadastro de usuários (pacientes e médicos)
* Agendamento de consultas
* Atualização e cancelamento de consultas
* Notificações de eventos
* Registro de histórico de ações

---

## 🧠 Arquitetura

O sistema é composto por três microsserviços:

### 🔹 agendamento-service

Responsável pela lógica principal:

* CRUD de usuários
* CRUD de consultas
* Publicação de eventos no RabbitMQ

### 🔹 notification-service

Responsável por:

* Consumir eventos de consulta
* Simular envio de notificações

### 🔹 history-service

Responsável por:

* Consumir eventos
* Persistir histórico das ações no banco de dados

---

## 🔄 Comunicação entre serviços

A comunicação ocorre de forma **assíncrona**, utilizando o **RabbitMQ**.

Fluxo:

```text
Cliente → agendamento-service → RabbitMQ → (notification + history)
```

---

## 🧰 Tecnologias utilizadas

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Security (Basic Auth)
* RabbitMQ
* PostgreSQL
* Docker & Docker Compose
* Maven

---

## 🔐 Segurança

O sistema utiliza **Basic Authentication** com controle de acesso por roles:

* `PACIENTE`
* `MEDICO`
* `ENFERMEIRO`

Regras:

* Apenas médicos podem atualizar consultas
* Pacientes só acessam suas próprias consultas

---

## 🐳 Como executar o projeto

### 📌 Pré-requisitos

* Docker
* Docker Compose

---

### 🚀 Subindo a aplicação

Na raiz do projeto:

```bash
docker-compose up --build
```

---

### 📍 Serviços disponíveis

| Serviço      | URL                    |
| ------------ | ---------------------- |
| Agendamento  | http://localhost:8080  |
| Notification | http://localhost:8081  |
| History      | http://localhost:8082  |
| RabbitMQ UI  | http://localhost:15672 |

---

### 🔐 RabbitMQ

Login:

```
guest / guest
```

---

## 🧪 Testando a API - Endpoints Principais

* A collection completa pode ser encontrada dentro da pasta '/postman' a partir da raiz do projeto.

### 🔹 Criar Paciente

```http
POST /usuarios
```

Body:

```json
{
  "nome": "Paciente 1",
  "email": "paciente@email.com",
  "password": "123",
  "role": "PACIENTE"
}
```

Response:

```json
{
  "id:": 1,
  "nome": "Paciente 1",
  "email": "paciente@email.com",
  "role": "PACIENTE"
}
```

---
### 🔹 Criar Médico

```http
POST /usuarios
```

Body:

```json
{
  "nome": "Médico 1",
  "email": "medico@email.com",
  "password": "123",
  "role": "MEDICO"
}
```
Response:
```json
{
  "id:": 1,
  "nome": "Médico 1",
  "email": "medico@email.com",
  "role": "MEDICO"
}
```
---

### 🔹 Criar Enfermeiro

```http
POST /usuarios
```

Body:

```json
{
  "nome": "Enfermeiro 1",
  "email": "enfermeiro@email.com",
  "password": "123",
  "role": "ENFERMEIRO"
}
```
Response:
```json
{
  "id:": 1,
  "nome": "Enfermeiro 1",
  "email": "enfermeiro@email.com",
  "role": "ENFERMEIRO"
}
```
---

### 🔹 Criar consulta

```http
POST /consultas
```
```json
{
  "pacienteId": 1,
  "medicoId": 2,
  "dataConsulta": "2026-03-20T16:00",
  "descricao": "Cardiologia"
}
```
---

### 🔹 Criar consulta

```http
PATCH /consultas/1
```
```json
{
  "descricao": "Consulta cardiológica",
  "status": "CANCELADA"
}
```
---

## 📊 Banco de dados

O sistema utiliza PostgreSQL com as seguintes tabelas principais:

* `usuario`
* `consulta`
* `historico`

---

## 🔔 Eventos

Os eventos publicados são:

* `CONSULTA_CRIADA`
* `CONSULTA_ATUALIZADA`
* `CONSULTA_CANCELADA`

---

## 📈 Logs

Os serviços registram:

* envio de eventos
* consumo de eventos
* persistência de histórico

---

## 📦 Estrutura do Projeto

```text
/agendamento-service
/notification-service
/history-service
/docker-compose.yml
```

---

## 💡 Decisões Técnicas

* Uso de microsserviços para separação de responsabilidades
* Comunicação assíncrona para desacoplamento
* PostgreSQL para persistência relacional
* Docker para padronização do ambiente
* Spring Security para controle de acesso

---

## 🚀 Possíveis melhorias

* Implementação de GraphQL para consultas e histórico
* Implementação de autenticação com JWT
* Uso de API Gateway
* Observabilidade (ELK, Prometheus)
* Testes de integração mais robustos
* Deploy em cloud (AWS, Azure)

---

## 👨‍💻 Autor

Projeto desenvolvido por Rafael Caroba.
