# MetaMapa: Sistema de gestión de mapeos colaborativos

## 📌 Project Overview
MetaMapa is a web application designed to aggregate, visualize, and strategically manage nationwide events across Argentina. The platform serves as a centralized hub for large-scale geographic data, allowing users to access real-time insights through an interactive geographic interface.

The system was engineered with a focus on scalability and modularity, utilizing a Microservices architecture to decouple core business domains such as user management, statistics, and event aggregation.

## 🏗️ Architectural Focus
The system is built following **Microservices** and **Clean Architecture** principles, ensuring a clear separation of concerns between domain logic, application rules, and infrastructure.
The project implements a Layered Architecture (Controllers, Services, Domein Models and Repositories).

## 🛠️ Tech Stack
- **Backend:** Java 17, Spring Boot.
- **Persistence:** **MySQL** and Hibernate / JPA.
- **Quality Assurance and Tooling:** JUnit 5, Mockito, Maven, **Lombok**.
- **Architecture:** Microservices with shared **Commons Library**.
- **Security:** Stateless authentication using **JWT (JSON Web Tokens)** and **Spring Security**.
- **Multimedia:** Image management and cloud storage integration with **Cloudinary API**.
- **Frontend:** Server-side rendering with **Thymeleaf**.

## 🔧 API
- RESTful API Provider: Each microservice exposes a comprehensive REST API, allowing for seamless integration with frontend clients and potential third-party consumers.
- External API Consumption: The platform enriches its data by consuming external RESTful services, such as other MetaMapa instances, geographic data providers and cloud media management.
- Inter-Service Communication: Microservices communicate with each other using REST calls, ensuring a decoupled and scalable network architecture.
  
## 👥 Contributors
- Juan Fernandez
- Juan Pablo Montemarani
- Manuel Rafael
- Fausto Rodríguez
- Juan Tarducci

## 📄 Documentation & Requirements
Detailed project specifications, business rules, and functional requirements can be found in the dedicated documentation folder:
[Project Specifications & Requirements](./docs/)

----

## ⚠️ This project template is designed for:

* Java 17. While the project does not explicitly limit it, the mvn verify command will not function with older Java versions.
* JUnit 5. JUnit version 5 is the latest framework version and introduces several differences compared to the "classic" version (JUnit 4). For further details, see:
  *  [Tools Guide](https://docs.google.com/document/d/1VYBey56M0UU6C0689hAClAvF9ILE6E7nKIuOqrRJnWQ/edit#heading=h.dnwhvummp994)
  *  [Blog Post (English)](https://www.baeldung.com/junit-5-migration) 
  *  [Blog Post (Spanish)](https://www.paradigmadigital.com/dev/nos-espera-junit-5/)
* Maven 3.8.1 or higher.

## 🧪 Running Tests

```
mvn test
```

## 🔍 Exhaustive Project Validation

```
mvn clean verify
```

This command will perform the following actions:

1. Run all tests.
2. Validate formatting conventions using Checkstyle.
3. Detect code smells (specific patterns of poor design).
4. Validate project coverage (to ensure most of the code is tested).
