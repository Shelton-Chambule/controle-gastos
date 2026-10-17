# Controle de Gastos

🇬🇧 [English](#english) · 🇵🇹 [Português](#português)

---

## English

REST API for personal expense tracking, built in Java with Spring Boot. Lets users create income/expense categories and register financial entries ("launches"), with JWT authentication and role-based access control (ADMIN/USER).

### Features

- User registration and JWT authentication
- Two access profiles: `ADMIN` and `USER`
- Category CRUD (income/expense)
- Financial launch (entry) CRUD
- Balance calculation (income, expenses and total) per user
- Each user can only access their own data; administrators have broader access

### Tech Stack

- Java + Spring Boot
- Spring Data JPA
- Spring Security
- JWT (java-jwt / Auth0)
- Lombok
- Bean Validation (Jakarta Validation)
- BCrypt

### Architecture

Layers: `Controller → Service → Repository → Entity`, with DTOs for input/output and custom exceptions.

```
com.chambule.controle_gastos
├── configuration   # Security: SecurityConfiguration, SecurityFilter, SecurityExceptionHandler
├── controller      # REST endpoints
├── services        # Business logic
├── repository      # Spring Data JPA
├── entities        # JPA entities (User, Category, Launch)
├── dto             # Input/output objects
└── exception       # Custom exceptions
```

#### Entities

**User**
- `login`, `password` (BCrypt hash), `creationDate` (automatic), `role` (`ADMIN`/`USER`)
- Related to `Category` and `Launch`

**Category**
- `nameCategory`, `type` (income/expense), `creationDate`, owning user
- Related to `Launch`

**Launch**
- `description`, `value`, `launchType` (`INCOME`/`EXPENSE`), `transactionDate`, `paymentMethod`, `creationDate`
- Related to `Category` and `User`

### Security

- Stateless JWT authentication (`Bearer` token in the `Authorization` header)
- Token generated at login, signed with HMAC256, valid for 2 hours
- `SecurityFilter` intercepts requests, validates the token and populates the `SecurityContext`
- Custom handling of `401` (unauthenticated) and `403` (access denied) errors
- Role-based access rules defined per endpoint in `SecurityConfiguration`

### Endpoints

#### Users — `/users`

| Method | Route | Access | Description |
|---|---|---|---|
| POST | `/users/save` | Public | Register user |
| POST | `/users/save/admin` | ADMIN | Register administrator |
| POST | `/users/login` | Public | Authenticate and get JWT token |
| PUT | `/users/{userId}` | ADMIN, USER | Update user |
| GET | `/users/getAll` | ADMIN | List all users |
| GET | `/users/{userId}` | ADMIN, USER | Get user by id |

#### Categories — `/categories`

| Method | Route | Access | Description |
|---|---|---|---|
| POST | `/categories/save` | ADMIN, USER | Create category |
| GET | `/categories/getAll` | ADMIN, USER | List categories (admin sees all; user sees own) |
| GET | `/categories/{categoryId}` | ADMIN, USER | Get category by id |
| PUT | `/categories/{id}` | USER | Update category |
| DELETE | `/categories/{categoryId}` | USER | Delete category |

#### Launches — `/launchs`

| Method | Route | Access | Description |
|---|---|---|---|
| POST | `/launchs/save` | ADMIN, USER | Create launch |
| GET | `/launchs/find` | ADMIN, USER | List launches |
| GET | `/launchs/getOne/{launchId}` | ADMIN, USER | Get launch by id |
| PUT | `/launchs/{launchId}` | ADMIN, USER | Update launch |
| DELETE | `/launchs/{launchId}` | ADMIN, USER | Delete launch |
| GET | `/launchs/category/{categoryId}` | ADMIN, USER | List launches by category |
| GET | `/launchs/balance/{userId}` | ADMIN, USER | Get balance (income, expenses, total) |

### Authentication in requests

All routes, except `/users/save` and `/users/login`, require the header:

```
Authorization: Bearer <token>
```

### Running the project

```bash
git clone <repository-url>
cd controle-gastos
```

Set the key used to sign the JWT token in `application.properties`:

```properties
api.controle.gastos=your_secret_key
```

Run with Maven:

```bash
./mvnw spring-boot:run
```

### License

Academic project, built for study and portfolio purposes.

---

## Português

API REST para controle de gastos pessoais, desenvolvida em Java com Spring Boot. Permite cadastrar categorias de receitas/despesas e registrar lançamentos financeiros, com autenticação JWT e controle de acesso baseado em papéis (ADMIN/USER).

### Funcionalidades

- Cadastro e autenticação de usuários via JWT
- Dois perfis de acesso: `ADMIN` e `USER`
- CRUD de categorias (receita/despesa)
- CRUD de lançamentos financeiros
- Cálculo de saldo (receitas, despesas e total) por usuário
- Cada usuário acessa apenas os próprios dados; administradores têm acesso amplo

### Tecnologias

- Java + Spring Boot
- Spring Data JPA
- Spring Security
- JWT (java-jwt / Auth0)
- Lombok
- Bean Validation (Jakarta Validation)
- BCrypt

### Arquitetura

Camadas: `Controller → Service → Repository → Entity`, com DTOs para entrada/saída e exceptions customizadas.

```
com.chambule.controle_gastos
├── configuration   # Segurança: SecurityConfiguration, SecurityFilter, SecurityExceptionHandler
├── controller      # Endpoints REST
├── services        # Regras de negócio
├── repository      # Spring Data JPA
├── entities        # Entidades JPA (User, Category, Launch)
├── dto             # Objetos de entrada/saída
└── exception       # Exceptions customizadas
```

#### Entidades

**User**
- `login`, `password` (hash BCrypt), `creationDate` (automática), `role` (`ADMIN`/`USER`)
- Relaciona-se com `Category` e `Launch`

**Category**
- `nameCategory`, `type` (receita/despesa), `creationDate`, usuário dono
- Relaciona-se com `Launch`

**Launch** (lançamento)
- `description`, `value`, `launchType` (`INCOME`/`EXPENSE`), `transactionDate`, `paymentMethod`, `creationDate`
- Relaciona-se com `Category` e `User`

### Segurança

- Autenticação stateless via JWT (token `Bearer` no header `Authorization`)
- Token gerado no login, assinado com HMAC256, válido por 2 horas
- `SecurityFilter` intercepta as requisições, valida o token e popula o `SecurityContext`
- Tratamento customizado de erros `401` (não autenticado) e `403` (acesso negado)
- Regras de acesso por papel definidas por endpoint em `SecurityConfiguration`

### Endpoints

#### Usuários — `/users`

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/users/save` | Público | Cadastrar usuário |
| POST | `/users/save/admin` | ADMIN | Cadastrar administrador |
| POST | `/users/login` | Público | Autenticar e obter token JWT |
| PUT | `/users/{userId}` | ADMIN, USER | Atualizar usuário |
| GET | `/users/getAll` | ADMIN | Listar todos os usuários |
| GET | `/users/{userId}` | ADMIN, USER | Buscar usuário por id |

#### Categorias — `/categories`

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/categories/save` | ADMIN, USER | Criar categoria |
| GET | `/categories/getAll` | ADMIN, USER | Listar categorias (admin vê todas; usuário vê as suas) |
| GET | `/categories/{categoryId}` | ADMIN, USER | Buscar categoria por id |
| PUT | `/categories/{id}` | USER | Atualizar categoria |
| DELETE | `/categories/{categoryId}` | USER | Remover categoria |

#### Lançamentos — `/launchs`

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/launchs/save` | ADMIN, USER | Criar lançamento |
| GET | `/launchs/find` | ADMIN, USER | Listar lançamentos |
| GET | `/launchs/getOne/{launchId}` | ADMIN, USER | Buscar lançamento por id |
| PUT | `/launchs/{launchId}` | ADMIN, USER | Atualizar lançamento |
| DELETE | `/launchs/{launchId}` | ADMIN, USER | Remover lançamento |
| GET | `/launchs/category/{categoryId}` | ADMIN, USER | Listar lançamentos por categoria |
| GET | `/launchs/balance/{userId}` | ADMIN, USER | Consultar saldo (receitas, despesas, total) |

### Autenticação nas requisições

Todas as rotas, exceto `/users/save` e `/users/login`, exigem o header:

```
Authorization: Bearer <token>
```

### Como executar

```bash
git clone <url-do-repositorio>
cd controle-gastos
```

Defina a chave usada para assinar o token JWT em `application.properties`:

```properties
api.controle.gastos=sua_chave_secreta
```

Execute com Maven:

```bash
./mvnw spring-boot:run
```

### Licença

Projeto acadêmico, desenvolvido para fins de estudo e portfólio.

---

## Author / Autor

**Shelton Chambule**
