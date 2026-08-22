# ClienteCRUD

Aplicação web desenvolvida com **Java + Spring Boot** para gerenciamento de clientes, utilizando arquitetura em camadas, API REST e interface web com HTML5, Bootstrap e jQuery.

O projeto foi criado com foco didático para demonstrar conceitos de desenvolvimento de aplicações corporativas e servir como base para práticas de **testes unitários, testes funcionais, testes de carga e análise de qualidade de código**.

## Tecnologias Utilizadas

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Bean Validation
* Maven
* H2 Database
* HTML5
* CSS3
* Bootstrap 5
* jQuery
* Toastr
* jQuery Mask
* Swagger / OpenAPI
* JUnit
* Selenium IDE
* Apache JMeter
* SonarQube

## Arquitetura

A aplicação utiliza uma arquitetura em camadas:

```text
Frontend
HTML + CSS + jQuery
        |
        | HTTP / JSON
        v
Controller
        |
        v
Service
        |
        v
Repository
        |
        v
Banco de Dados H2
```

### Controller

Responsável por receber as requisições HTTP da aplicação.

### Service

Contém as regras de negócio.

### Repository

Responsável pela comunicação com o banco de dados.

### Model

Representa as entidades da aplicação.

---

# Funcionalidades

O sistema possui um CRUD completo de clientes.

* Cadastrar cliente
* Listar clientes
* Consultar cliente
* Atualizar cliente
* Excluir cliente
* Validação de campos
* Validação de e-mail
* Tratamento de erros
* Mensagens de sucesso e erro
* Máscara de CPF
* Máscara de telefone

## Dados do Cliente

Cada cliente possui:

```text
id
nome
email
cpf
telefone
```

---

# Estrutura do Projeto

```text
clientecrud/
│
├── pom.xml
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── br/
    │   │       └── unifametro/
    │   │           ├── ClienteCrudApplication.java
    │   │           │
    │   │           ├── controller/
    │   │           │   └── ClienteController.java
    │   │           │
    │   │           ├── model/
    │   │           │   └── Cliente.java
    │   │           │
    │   │           ├── repository/
    │   │           │   └── ClienteRepository.java
    │   │           │
    │   │           ├── service/
    │   │           │   └── ClienteService.java
    │   │           │
    │   │           └── exception/
    │   │               ├── GlobalExceptionHandler.java
    │   │               └── RecursoNaoEncontradoException.java
    │   │
    │   └── resources/
    │       ├── application.properties
    │       │
    │       └── static/
    │           ├── index.html
    │           │
    │           ├── css/
    │           │   └── app.css
    │           │
    │           └── js/
    │               └── app.js
    │
    └── test/
        └── java/
```

---

# Pré-requisitos

Antes de executar o projeto, certifique-se de possuir:

```text
Java 21+
Maven 3+
Git
```

Verifique as versões:

```bash
java -version
```

```bash
mvn -version
```

```bash
git --version
```

---

# Clonando o Projeto

```bash
git clone https://github.com/SEU-USUARIO/clientecrud.git
```

Entre na pasta:

```bash
cd clientecrud
```

---

# Executando a Aplicação

Execute:

```bash
mvn spring-boot:run
```

Ou, caso utilize Maven Wrapper:

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em:

```text
http://localhost:8080
```

---

# Interface Web

Ao acessar:

```text
http://localhost:8080/
```

será exibida a tela de gerenciamento de clientes.

A interface permite:

```text
Cadastrar
Editar
Excluir
Listar
Atualizar registros
```

As mensagens de operação são exibidas utilizando **Toastr**.

---

# API REST

A API utiliza o endpoint base:

```text
/api/clientes
```

## Listar Clientes

```http
GET /api/clientes
```

## Buscar Cliente

```http
GET /api/clientes/{id}
```

Exemplo:

```http
GET /api/clientes/1
```

## Cadastrar Cliente

```http
POST /api/clientes
```

Exemplo de JSON:

```json
{
  "nome": "João da Silva",
  "email": "joao@email.com",
  "cpf": "123.456.789-00",
  "telefone": "(85) 99999-9999"
}
```

## Atualizar Cliente

```http
PUT /api/clientes/{id}
```

Exemplo:

```http
PUT /api/clientes/1
```

```json
{
  "nome": "João da Silva Atualizado",
  "email": "joao@email.com",
  "cpf": "123.456.789-00",
  "telefone": "(85) 98888-7777"
}
```

## Excluir Cliente

```http
DELETE /api/clientes/{id}
```

Exemplo:

```http
DELETE /api/clientes/1
```

---

# Banco de Dados H2

O projeto utiliza banco H2 em memória.

Console:

```text
http://localhost:8080/h2-console
```

Exemplo de configuração:

```text
JDBC URL:
jdbc:h2:mem:clientesdb

User:
sa

Password:
```

Como o banco está em memória, os dados são apagados quando a aplicação é encerrada.

---

# Swagger / OpenAPI

A API pode ser documentada e testada utilizando Swagger UI.

Após iniciar o projeto, acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

A documentação OpenAPI permite testar os endpoints diretamente pelo navegador.

---

# Testes Unitários

O projeto pode utilizar **JUnit 5** para testar regras de negócio.

Executar todos os testes:

```bash
mvn test
```

Exemplo de fluxo:

```text
Código
   |
   v
Teste Unitário
   |
   v
Resultado esperado
```

---

# Testes Funcionais com Selenium IDE

A interface foi construída utilizando identificadores estáveis para facilitar testes automatizados.

Exemplos:

```text
id=nome
id=email
id=cpf
id=telefone
id=btnSalvar
id=btnCancelar
id=btnAtualizarLista
id=tabelaClientes
```

Exemplo de cenário:

```text
1. Abrir a aplicação
2. Preencher nome
3. Preencher e-mail
4. Preencher CPF
5. Preencher telefone
6. Clicar em Salvar Cliente
7. Validar mensagem de sucesso
8. Validar cliente na tabela
```

Também podem ser criados cenários de erro:

```text
Nome vazio
E-mail inválido
CPF vazio
E-mail duplicado
```

---

# Testes de Carga com JMeter

A API também pode ser utilizada em exercícios de carga e stress.

Exemplo:

```text
GET /api/clientes
```

Executar com diferentes quantidades de usuários virtuais:

```text
10 Threads
50 Threads
100 Threads
250 Threads
500 Threads
```

Também pode ser testado:

```text
POST /api/clientes
```

Para operações de cadastro sob carga.

Métricas importantes:

```text
Tempo de resposta
Throughput
Latência
Percentual de erros
Número de requisições
```

---

# Análise de Qualidade com SonarQube

O projeto pode ser utilizado para demonstrar análise estática de código.

Exemplos de problemas analisados:

* Bugs
* Vulnerabilidades
* Security Hotspots
* Code Smells
* Duplicação de código
* Cobertura de testes

Fluxo:

```text
Projeto
   |
   v
Build
   |
   v
Testes
   |
   v
SonarQube
   |
   v
Dashboard de Qualidade
```

---

# Objetivo Acadêmico

Este projeto foi desenvolvido como apoio às disciplinas de desenvolvimento de software, permitindo trabalhar de forma integrada:

```text
Spring Boot
API REST
Arquitetura em Camadas
Maven
CRUD
Validação
Frontend
JUnit
Selenium IDE
JMeter
SonarQube
```

A proposta é acompanhar o ciclo:

```text
DESENVOLVER
    ↓
TESTAR
    ↓
VALIDAR
    ↓
ANALISAR
    ↓
MELHORAR
```

---

# Professor

**Prof. Esp. Vicente das Graça Magalhães Júnior**

Unifametro

---

# Licença

Projeto desenvolvido para fins acadêmicos e educacionais.
