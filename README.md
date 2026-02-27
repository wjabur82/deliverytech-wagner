# Delivery Tech API

Uma API REST completa para gerenciamento de ecossistemas de delivery, permitindo o controle de clientes, cardápios de restaurantes e o fluxo de processamento de pedidos em tempo real.

---

# Tecnologias Utilizadas

* **Java 17/21**: Linguagem base do projeto.
* **Spring Boot 3**: Framework para construção da aplicação e gestão de dependências.
* **Spring Data JPA**: Abstração de persistência de dados.
* **H2 Database**: Banco de dados em memória para desenvolvimento ágil.
* **Lombok**: Redução de código boilerplate (Getters, Setters, Construtores).
* **Jakarta Persistence (Hibernate)**: Mapeamento Objeto-Relacional (ORM).

---

# Modelo de Dados

A API utiliza relacionamentos complexos para garantir a integridade do negócio:

* **Cliente ↔ Pedido**: Um cliente pode ter vários pedidos (1:N).
* **Restaurante ↔ Produto**: Um restaurante gerencia seu próprio cardápio (1:N).
* **Pedido ↔ ItemPedido**: Um pedido é composto por vários itens (1:N).
* **Produto ↔ ItemPedido**: Um produto pode estar presente em vários itens de pedidos diferentes.

---

# Arquitetura do Projeto

O projeto segue o padrão de camadas:

* **Model (Entities)**: Representação do banco de dados.
* **Repository**: Camada de acesso aos dados com JPQL e SQL Nativo.
* **Service**: Regras de negócio e cálculos.
* **Controller**: Exposição dos Endpoints REST.
* **DTO/Projection**: Camada de transporte e otimização de dados.

---

#  Configuração e Execução

## Como Rodar o Projeto

1. Clone o repositório.
2. Certifique-se de que o Lombok está instalado na sua IDE.
3. Execute a aplicação através da classe `DeliveryApiApplication`.

O sistema utiliza um **DataLoader automático** que popula o banco de dados H2 ao iniciar.

### Certifique-se de ter o Maven instalado e execute:

```bash
./mvnw spring-boot:run
```

A API estará disponível em:

```
http://localhost:8080
```

---

# Endpoints da API

## CLIENTE

* `POST /clientes` – Cadastra um novo cliente no sistema.
* `GET /clientes` – Retorna a lista de todos os clientes com status ativo.
* `GET /clientes/{id}` – Busca os detalhes de um cliente específico pelo seu ID.
* `PUT /clientes/{id}` – Atualiza as informações cadastrais (nome, endereço, etc).
* `DELETE /clientes/{id}` – Realiza a inativação (exclusão lógica) do cliente.

---

## RESTAURANTE

* `POST /restaurantes` – Cadastra um novo estabelecimento.
* `GET /restaurantes` – Lista todos os restaurantes cadastrados.
* `GET /restaurantes/categoria/{cat}` – Filtra estabelecimentos por categoria (ex: Pizza, Japonesa).

---

## PRODUTO

* `POST /produtos` – Adiciona um novo item ao cardápio de um restaurante.
* `GET /produtos/restaurante/{id}` – Lista todos os produtos vinculados a um restaurante específico.

---

## PEDIDO

* `POST /pedidos` – Registra um novo pedido contendo múltiplos itens.
* `GET /pedidos/cliente/{id}` – Consulta o histórico completo de pedidos de um cliente.
* `PATCH /pedidos/{id}/status` – Atualiza o status do pedido (Ex: PENDENTE, CONFIRMADO, ENTREGUE).

---

## ITEMPEDIDO

* `GET /item-pedidos` – Consulta o histórico completo de itens de pedidos.
* `GET /item-pedidos/pedido{id}` – Busca os detalhes de um item pedido específico pelo seu ID.

---

## RELATÓRIO

* `GET /relatorio/total-vendas-por-restaurante` – Consulta o histórico total de vendas por restaurantes.

---

# Postman Collection

Para facilitar os testes de integração e validar os fluxos da API:

* Localize o arquivo na raiz do projeto:
  `delivery_api.postman_collection.json`

* No Postman, clique em **Import** e selecione o arquivo.

* A coleção já está configurada para usar a variável `{{baseUrl}}`.
  Certifique-se de que ela aponta para:

  ```
  http://localhost:8080
  ```

---

# Documentação da API

### Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

(É aqui que você visualiza os botões, testa os endpoints e vê os modelos JSON.)

### OpenAPI JSON

```
http://localhost:8080/v3/api-docs
```

(Este link retorna um JSON puro com toda a especificação da sua API, usado por ferramentas de automação.)

---

# Guia de Testes e Qualidade

## Tecnologias de Teste Utilizadas

* **JUnit 5**: Framework base para execução de testes unitários e de integração.
* **Mockito**: Utilizado para criar dublês de teste (Mocks) em camadas de Service.
* **MockMvc**: Utilizado para simular requisições HTTP em testes de integração de Controllers.
* **JaCoCo**: Ferramenta de análise de cobertura de código.

---

## Cobertura de Código (JaCoCo)

### Meta de Cobertura

* Mínimo de **80% de linhas cobertas**.

### Exclusões

Classes de configuração (config), DTOs, entidades (model) e exceções personalizadas são excluídas da métrica para focar na lógica de negócio (Services e Controllers).

---

## Comandos Maven

###  Execução Completa (Verificação de Qualidade)

```bash
mvn clean test jacoco:report 
```

---

###  Apenas Testes de Integração

```bash
mvn test -Dtest=*IT
```

---

###  Gerar Relatório Visual

```bash
mvn jacoco:report
```

Relatório gerado em:

```
target/site/jacoco/index.html
```

---

### Ignorar Verificação de Cobertura

```bash
mvn install -Djacoco.skip=true
```

---

## Como Analisar o Relatório

* 🟢 Verde: Código totalmente coberto por testes.
* 🟡 Amarelo: Branches parcialmente cobertos.
* 🔴 Vermelho: Código não exercitado pelos testes.

---

# Autenticação e Fluxo de Usuário
A API utiliza segurança baseada em tokens Bearer para proteger os recursos. Abaixo estão os exemplos das requisições principais de acesso:

### Registrar Usuário
```bash
Endpoint: POST /api/auth/register
```
Payload: Cria um novo usuário com nome, e-mail, senha e atribuição de role (ex: ADMIN).
Resposta: Retorna os dados do usuário e o status 201 Created.

### Login de Usuário
```bash
Endpoint: POST /api/auth/login
```
Payload: E-mail e senha cadastrados.
Resposta: Retorna um token JWT do tipo Bearer com validade de 24 horas.

### Recuperar Perfil (Me)
```bash
Endpoint: GET /api/auth/me
```
Segurança: Requer o token JWT enviado no Header de autorização.
Resposta: Retorna os dados do perfil do usuário atualmente autenticado.

## Autenticação nos Testes

Como a API é protegida por Spring Security, os testes de integração de Controller utilizam a anotação `@WithMockUser` para simular credenciais válidas e evitar erros 403 Forbidden.

**Dica de Desenvolvimento:**
Antes de realizar um push, sempre execute o "Combo de Qualidade" para garantir que sua alteração não reduziu a cobertura global do projeto para menos de 80%.

---

# Gerenciamento com Docker Compose

O Docker Compose gerencia a API, o banco MySQL e o cache Redis de forma integrada na mesma rede.

## Subir o ambiente completo

```bash
docker-compose up -d --build
```

---

## Parar e remover os serviços

```bash
docker-compose down
```

---

## Reset total (Limpeza de volumes)

```bash
docker-compose down -v
```

---

# Monitoramento com Prometheus

Os arquivos de configuração estão organizados na pasta `/prometheus`.

```bash
Endpoint: http://localhost:9090/actuator/prometheus
```

## Subir Prometheus (Bash/Git Bash)

```bash
docker run -d \
  --name delivery-prometheus \
  -p 9090:9090 \
  -v "$(pwd)/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml" \
  -v "$(pwd)/prometheus/alert_rules.yml:/etc/prometheus/alert_rules.yml" \
  prom/prometheus
```

---

## Subir Prometheus (PowerShell)

```powershell
docker run -d `
  --name delivery-prometheus `
  -p 9090:9090 `
  -v "${PWD}/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml" `
  -v "${PWD}/prometheus/alert_rules.yml:/etc/prometheus/alert_rules.yml" `
  prom/prometheus
```

---

# Pipeline de CI/CD (GitHub Actions)

O projeto utiliza um pipeline automatizado para garantir a qualidade e agilizar o deploy.

## Etapas do Pipeline

### Build & Test

Compila o projeto usando Java 21 e executa todos os testes unitários e de integração.
Gera também o relatório de cobertura de testes com JaCoCo.

### Docker Build

Constrói uma nova imagem Docker da aplicação após a aprovação dos testes.

### Deploy Automático

Realiza o deploy da imagem em ambiente de homologação sempre que um push é feito na branch `main`.

### Como monitorar

Acesse a aba **Actions** no seu repositório do GitHub para visualizar a execução em tempo real de cada etapa e os logs de erro, caso ocorram.

---

# Desenvolvedor

**Wagner Jabur Junior**
**TURMA 2602**
Desenvolvido com **JDK 21** e **Spring Boot 3.2.x**
