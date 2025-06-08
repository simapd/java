# SIMAPD API

## 🚀 Sobre o Projeto

A **SIMAPD API** (Sistema Integrado de Monitoramento e Alerta para Prevenção de Desastres) é uma API RESTful desenvolvida em Spring Boot para monitoramento inteligente de áreas de risco e prevenção de desastres naturais. A API fornece endpoints completos para gerenciamento de áreas de risco, medições de sensores IoT, relatórios de usuários e autenticação, com integração ao banco de dados Oracle, sistema de cache avançado, validações robustas e paginação em todas as consultas de listagem.

O sistema representa uma solução tecnológica inovadora para enfrentar uma das maiores vulnerabilidades socioambientais do Brasil: os desastres naturais causados por deslizamentos e inundações. Entre 1988 e 2022, o país registrou 4.146 mortes por deslizamentos, e atualmente cerca de 3,9 milhões de pessoas vivem em 13.297 áreas de risco mapeadas.

## 👥 Equipe de Desenvolvimento

| Nome | RM | E-mail | GitHub | LinkedIn |
|------|-------|---------|---------|----------|
| Arthur Vieira Mariano | RM554742 | arthvm@proton.me | [@arthvm](https://github.com/arthvm) | [arthvm](https://linkedin.com/in/arthvm/) |
| Guilherme Henrique Maggiorini | RM554745 | guimaggiorini@gmail.com | [@guimaggiorini](https://github.com/guimaggiorini) | [guimaggiorini](https://linkedin.com/in/guimaggiorini/) |
| Ian Rossato Braga | RM554989 | ian007953@gmail.com | [@iannrb](https://github.com/iannrb) | [ianrossato](https://linkedin.com/in/ianrossato/) |

## 🛠️ Tecnologias Utilizadas

### Stack Principal
- **Java 17** - Linguagem de programação
- **Spring Boot 3.5.0** - Framework principal
- **Spring Web** - Criação da API REST
- **Spring Data JPA** - ORM para acesso ao banco de dados
- **Spring Security** - Autenticação e autorização
- **Oracle Database** - Banco de dados principal (com driver OJDBC8 19.8.0.0)
- **Lombok 1.18.38** - Redução de boilerplate code
- **Bean Validation (Jakarta)** - Validação de campos
- **Spring Cache + Caffeine** - Sistema de cache em memória
- **Spring Data Pagination** - Sistema de paginação automática

### Dependências de Segurança
- **JWT (JSON Web Tokens) 0.12.3** - Autenticação stateless
- **Spring Security** - Controle de acesso e autorização
- **BCrypt** - Criptografia de senhas

### Dependências Adicionais
- **CUID 2.0.3** - Geração de IDs únicos
- **Spring DotEnv 4.0.0** - Gerenciamento de variáveis de ambiente
- **SpringDoc OpenAPI 2.8.6** - Documentação automática da API (Swagger)
- **Maven** - Gerenciamento de dependências

### Arquitetura
- **Clean Architecture** - Separação em camadas (entities, use cases, controllers)
- **Repository Pattern** - Abstração de acesso a dados
- **DTOs** - Transferência de dados
- **Exception Handler** - Tratamento centralizado de erros
- **Paginação Uniforme** - Todas as consultas de listagem utilizam paginação
- **Cache Inteligente** - Sistema de cache multi-camadas com Caffeine

## 🚀 Como Executar o Projeto

### Pré-requisitos

- Java 17+
- Maven 3.6+
- Oracle Database
- Git

### Instalação

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/simapd/java.git
   cd java
   ```

2. **Configure as variáveis de ambiente:**
   ```bash
   # Crie um arquivo .env na raiz do projeto
   cat > .env << EOF
   SPRING_DATASOURCE_URL=jdbc:oracle:thin:@<host>:<port>:<sid>
   SPRING_DATASOURCE_USERNAME=<username>
   SPRING_DATASOURCE_PASSWORD=<password>
   SPRING_DATASOURCE_DRIVERCLASSNAME=oracle.jdbc.OracleDriver
   JWT_SECRET=<sua_chave_secreta_jwt>
   JWT_EXPIRATION=86400000
   EOF
   ```

3. **Compile o projeto:**
   ```bash
   mvn clean compile
   ```

4. **Execute o projeto:**
   ```bash
   mvn spring-boot:run
   ```

5. **Acesse a API:**
   - Aplicação: `http://localhost:8080`
   - Documentação Swagger: `http://localhost:8080/swagger-ui.html`

## 📋 Endpoints da API

### 🔐 Autenticação

| Método | Endpoint | Descrição | Autenticação | Retorno |
|--------|----------|-----------|--------------|---------|
| POST | `/api/auth/login` | Realizar login | Não | 200 OK - `LoginResponseDTO` |
| GET | `/api/auth/me` | Dados do usuário autenticado | JWT | 200 OK - `UserResponseDTO` |

### 👥 Users (Usuários)

| Método | Endpoint | Descrição | Autenticação | Retorno |
|--------|----------|-----------|--------------|---------|
| POST | `/api/users/register` | Registrar novo usuário | Não | 201 Created - `LoginResponseDTO` |
| GET | `/api/users/` | Lista todos os usuários **com paginação** | JWT | 200 OK - `Page<UserResponseDTO>` |
| GET | `/api/users/{id}` | Busca usuário por ID | JWT | 200 OK - `UserResponseDTO` |
| PUT | `/api/users/{id}` | Atualiza usuário | JWT | 200 OK - `UserResponseDTO` |
| DELETE | `/api/users/{id}` | Remove usuário | JWT | 204 NoContent |

### 🏔️ Risk Areas (Áreas de Risco)

| Método | Endpoint | Descrição | Autenticação | Retorno |
|--------|----------|-----------|--------------|---------|
| GET | `/api/risk-areas/` | Lista todas as áreas **com paginação** | JWT | 200 OK - `Page<RiskAreasDTO>` |
| GET | `/api/risk-areas/{id}` | Busca área por ID | JWT | 200 OK - `RiskAreasDTO` |
| POST | `/api/risk-areas/` | Cria nova área de risco | JWT | 201 Created - `RiskAreasDTO` |
| PUT | `/api/risk-areas/{id}` | Atualiza área de risco | JWT | 204 NoContent |
| DELETE | `/api/risk-areas/{id}` | Remove área de risco | JWT | 204 NoContent |

### 📊 Measurements (Medições dos Sensores)

| Método | Endpoint | Descrição | Autenticação | Retorno |
|--------|----------|-----------|--------------|---------|
| GET | `/api/measurements/` | Lista todas as medições **com paginação** | JWT | 200 OK - `Page<MeasurementsDTO>` |
| GET | `/api/measurements/{id}` | Busca medição por ID | JWT | 200 OK - `MeasurementsDTO` |
| GET | `/api/measurements/sensor/{sensorId}` | Medições por sensor | JWT | 200 OK - `List<MeasurementsDTO>` |
| GET | `/api/measurements/area/{areaId}` | Medições por área | JWT | 200 OK - `List<MeasurementsDTO>` |
| GET | `/api/measurements/filter` | Filtrar medições | JWT | 200 OK - `List<MeasurementsDTO>` |
| GET | `/api/measurements/daily-aggregation` | Agregação diária | JWT | 200 OK - `List<DailyAggregationDTO>` |
| GET | `/api/measurements/daily-average` | Média diária | JWT | 200 OK - `Double` |

### 📝 User Reports (Relatórios de Usuários)

| Método | Endpoint | Descrição | Autenticação | Retorno |
|--------|----------|-----------|--------------|---------|
| GET | `/api/user-reports/` | Lista todos os relatórios **com paginação** | JWT | 200 OK - `Page<UserReportsDTO>` |
| GET | `/api/user-reports/{id}` | Busca relatório por ID | JWT | 200 OK - `UserReportsDTO` |
| GET | `/api/user-reports/area/{areaId}` | Relatórios por área **com paginação** | JWT | 200 OK - `Page<UserReportsDTO>` |
| GET | `/api/user-reports/user/{userId}` | Relatórios por usuário **com paginação** | JWT | 200 OK - `Page<UserReportsDTO>` |
| POST | `/api/user-reports/` | Cria novo relatório | JWT | 201 Created - `UserReportsDTO` |
| PUT | `/api/user-reports/{id}` | Atualiza relatório | JWT | 200 OK - `UserReportsDTO` |
| DELETE | `/api/user-reports/{id}` | Remove relatório | JWT | 204 NoContent |

### 🔄 Parâmetros de Paginação

Todos os endpoints de listagem aceitam os seguintes parâmetros de query:

- **`page`** (int): Número da página (base 0) - Padrão: `0`
- **`size`** (int): Tamanho da página - Padrão: `10`
- **`sortBy`** (string): Campo para ordenação - Padrão varia por endpoint
- **`sortDir`** (string): Direção da ordenação (asc/desc) - Padrão: `desc`

### 📊 Ordenação Automática

- **Measurements**: Ordenadas por `measuredAt` em ordem decrescente
- **UserReports**: Ordenadas por `reportedAt` em ordem decrescente
- **RiskAreas**: Ordenadas por `createdAt` em ordem decrescente
- **Users**: Ordenação padrão do banco de dados

## 🔧 Funcionalidades de Negócio

### 🛡️ Sistema de Autenticação
- **JWT (JSON Web Tokens)** para autenticação stateless
- **Registro de usuários** com validação de email único
- **Login seguro** com criptografia BCrypt
- **Middleware de autenticação** para proteção de rotas
- **Expiração configurável** de tokens (padrão: 24 horas)

### 🔧 Validações Implementadas
- **IDs Únicos**: Formato CUID2 para todos os identificadores
- **Email**: Formato válido e unicidade no sistema
- **Coordenadas**: Validação de latitude e longitude
- **Níveis de Risco**: Valores entre 1-4 (Baixo, Médio, Alto, Crítico)
- **Tipos de Sensor**: Valores válidos (1=Chuva, 2=Umidade, 3=Aceleração, 4=Temperatura)
- **Dados JSON**: Validação de estrutura para valores de medições
- **Associações**: Verificação de existência de áreas e usuários relacionados

### 🏗️ Arquitetura em Camadas
- **Controllers**: Recebem requisições HTTP e delegam para use cases
- **Use Cases**: Contêm a lógica de negócio específica
- **Repositories**: Abstração para acesso aos dados
- **Entities**: Representam as tabelas do banco de dados
- **DTOs**: Objetos para transferência de dados
- **Mappers**: Conversão entre entities e DTOs
- **Services**: Serviços auxiliares (JWT, Cache, etc.)

### 📈 Sistema de Cache Avançado
- **Cache em memória** com Caffeine para otimizar consultas frequentes
- **Invalidação automática** em operações de escrita
- **Caches separados** por tipo de consulta para maior eficiência
- **Configuração flexível**: TTL de 1 hora, máximo 1000 entradas
- **Cache paginado**: Otimização específica para consultas paginadas
- **Múltiplas camadas**: users, riskAreas, userReports, measurements

### 📄 Sistema de Paginação Universal
- **Paginação automática**: Todas as rotas de listagem utilizam paginação
- **Ordenação inteligente**: Ordenação por campos relevantes (datas, etc.)
- **Performance otimizada**: Consultas limitadas para melhor performance
- **Flexibilidade total**: Parâmetros configuráveis de página, tamanho e ordenação
- **Compatibilidade**: Formato padrão do Spring Data

### 📊 Análise de Dados de Sensores
- **Agregação diária**: Cálculos de média, mínimo, máximo e contagem
- **Filtros avançados**: Por sensor, área, tipo e nível de risco
- **Processamento JSON**: Extração inteligente de valores numéricos
- **Diferentes tipos de sensor**: Suporte a múltiplos tipos de medições
- **Análise temporal**: Consultas por períodos específicos

## 📊 Exemplos de Uso

### Registrar um Usuário
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@exemplo.com",
    "password": "senha123",
    "areaId": "cm456789012345678901234567"
  }'
```

### Fazer Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@exemplo.com",
    "password": "senha123"
  }'
```

### Criar uma Área de Risco
```bash
curl -X POST http://localhost:8080/api/risk-areas/ \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <seu_token_jwt>" \
  -d '{
    "name": "Morro da Esperança",
    "description": "Área de risco de deslizamento",
    "latitude": "-23.5505",
    "longitude": "-46.6333"
  }'
```

### Criar um Relatório de Usuário
```bash
curl -X POST http://localhost:8080/api/user-reports/ \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <seu_token_jwt>" \
  -d '{
    "userId": "cm123456789012345678901234",
    "areaId": "cm456789012345678901234567",
    "description": "Observei rachaduras na encosta após a chuva",
    "locationInfo": "Próximo à casa número 45",
    "photoUrl": "https://exemplo.com/foto.jpg"
  }'
```

### Listar Medições com Filtros
```bash
# Medições de um sensor específico
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/measurements/sensor/sensor123456789012345678"

# Filtrar por área e nível de risco
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/measurements/filter?areaId=area123&riskLevel=3"

# Agregação diária por período
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/measurements/daily-aggregation?startDate=2024-01-01&endDate=2024-01-31"
```

### Listar com Paginação e Ordenação
```bash
# Primeira página de áreas de risco, ordenadas por nome
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/risk-areas/?page=0&size=10&sortBy=name&sortDir=asc"

# Relatórios mais recentes primeiro
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/user-reports/?page=0&size=5&sortBy=reportedAt&sortDir=desc"
```

## 🔧 Tratamento de Erros

O sistema possui tratamento centralizado de erros que retorna:

### Erros de Validação (400 Bad Request)
```json
{
  "name": "Name must not exceed 150 characters",
  "email": "Invalid email format"
}
```

### Erros de Autenticação (401 Unauthorized)
```json
{
  "error": "Invalid email or password"
}
```

### Erros de Autorização (403 Forbidden)
```json
{
  "error": "Access denied"
}
```

### Erros de Recurso Não Encontrado (404 Not Found)
```json
{
  "error": "User not found"
}
```

### Erros de Negócio Específicos
- **Email já cadastrado**: Tentativa de registro com email existente
- **Área não encontrada**: Referência a área de risco inexistente
- **Usuário não encontrado**: Referência a usuário inexistente
- **Token inválido**: JWT expirado ou malformado

## 🌟 Funcionalidades Especiais

### 📡 Integração com Sensores IoT
- **Múltiplos tipos de sensor**: Chuva, umidade, aceleração
- **Dados em JSON**: Flexibilidade para diferentes estruturas de dados
- **Processamento inteligente**: Extração automática de valores numéricos
- **Níveis de risco**: Classificação automática baseada em thresholds

### 🚨 Sistema de Alertas
- **Classificação de risco**: 4 níveis (Baixo, Médio, Alto, Crítico)
- **Monitoramento contínuo**: Análise em tempo real das medições
- **Relatórios comunitários**: Participação ativa dos usuários
- **Verificação de relatórios**: Sistema de validação de informações

### 📈 Análise e Relatórios
- **Agregações temporais**: Dados diários, médias e tendências
- **Filtros avançados**: Múltiplos critérios de busca
- **Exportação de dados**: APIs para integração com dashboards
- **Histórico completo**: Preservação de dados para análise temporal

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos como parte do Global Solution da FIAP - JAVA ADVANCED.