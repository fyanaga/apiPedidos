# 📦 apiPedidos - Spring Boot API

Este projeto é uma API RESTful desenvolvida com **Spring Boot** para gerenciamento de pedidos. A arquitetura do sistema foi baseada na **Arquitetura Limpa (Clean Architecture)**, utilizando boas práticas como separação de responsabilidades, uso de **DTOs**, tratamento de exceções global, e containers Docker para provisionamento da infraestrutura.

---

## 🚀 Tecnologias Utilizadas

### ✅ Spring Boot
Framework que facilita a criação de aplicações Java baseadas no Spring. Ele fornece uma configuração automática e estrutura padronizada, agilizando o desenvolvimento.

🔗 [Documentação oficial](https://spring.io/projects/spring-boot)

---

### 🗃 Spring Data JPA
Facilita a implementação de repositórios com base em JPA (Java Persistence API), eliminando grande parte do código boilerplate necessário para acessar bancos de dados relacionais.

🔗 [Documentação oficial](https://spring.io/projects/spring-data-jpa)

---

### 🐘 PostgreSQL
Banco de dados relacional e open-source, conhecido por sua robustez e conformidade com os padrões SQL. Ele armazena as entidades persistidas da aplicação.

🔗 [Site oficial](https://www.postgresql.org/)

---

### 📦 Bean Validation (Jakarta Validation)
Usada para validações automáticas de campos nas classes DTOs utilizando anotações como `@NotNull`, `@Email`, `@Size`, entre outras.

🔗 [Documentação da especificação](https://jakarta.ee/specifications/bean-validation/)

---

### 🔄 ModelMapper
Biblioteca utilizada para mapear automaticamente objetos de uma classe para outra (ex: de DTO para Entity e vice-versa), reduzindo código repetitivo.

🔗 [GitHub oficial](https://github.com/modelmapper/modelmapper)

---

### 🐳 Docker
Tecnologia de contêineres utilizada para empacotar a aplicação com todas as suas dependências, permitindo executar em qualquer ambiente.

#### Arquivos no projeto:
- `Dockerfile`: Define como a imagem da aplicação será construída.
- `docker-compose.yml`: Define os serviços (API + PostgreSQL) e como eles interagem entre si.

🔗 [Documentação oficial](https://docs.docker.com/)

---

## 🧱 Padrões e Arquitetura

### 🧼 Clean Architecture (Arquitetura Limpa)
Organização da aplicação baseada em camadas bem definidas:
- **Entities (Domínio):** Regra de negócio e lógica central.
- **Use Cases / Services:** Casos de uso da aplicação.
- **Controllers:** Pontos de entrada da aplicação (HTTP).
- **Repositories:** Interface com a persistência de dados.

🔗 [Explicação de Clean Architecture - Uncle Bob](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html)

---

### 📦 DTOs (Data Transfer Objects)
Objetos intermediários usados para transportar dados entre a camada de apresentação e de serviço. Eles ajudam a controlar o que é exposto ao cliente e validado na entrada.

---

### 🌐 Global Exception Handlers
Tratamento centralizado de exceções da aplicação, fornecendo respostas personalizadas e amigáveis para o cliente.

---

### 🧾 Repositories
Interfaces baseadas em `JpaRepository` que encapsulam as operações de acesso ao banco de dados, mantendo a separação entre lógica de negócio e persistência.

---

### 🧠 Services
Camada intermediária entre os controladores e os repositórios. Contém a lógica de negócio da aplicação e orquestra as operações sobre os dados.

---

### 📡 Controllers
Camada de entrada da aplicação que expõe os endpoints da API e interage com os serviços.

---

## 🧪 Como executar

### Pré-requisitos
- Docker instalado
- Docker Compose instalado

### Passos:

```bash
# Subir a aplicação com Docker
docker-compose up --build
```

A aplicação estará disponível em: `http://localhost:8082`

---

## 📂 Estrutura do Projeto

```
apiPedidos/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/cotiinformatica/
│   │   │       ├── configurations/
│   │   │       ├── controllers/
│   │   │       ├── domain/
│   │   │       ├── repositories/
│   │   │       └── exceptions/
│   │   └── resources/
│   │       └── application.yml
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 👨‍💻 Autor

Desenvolvido por [Seu Nome Aqui] - Professor na Coti Informática  
📧 Contato: [seu-email@email.com]  
🔗 LinkedIn: [Seu LinkedIn]

---
