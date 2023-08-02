# Subscription service
This project mange access to ewan microservices

## Installation
- [Java 8](https://jdk.java.net/8/)
- [Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL 15](https://www.postgresql.org/download/)

### Run project in localhost

#### Create Database

`` CREATE DATABASE multiple_clud_services;``

#### Create user and grant access
`` CREATE USER ewan WITH PASSWORD 'mcs'; ``

`` GRANT ALL PRIVILEGES ON DATABASE ewan_subscription TO ewan;``

`` GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ewan;``

### Run and execute test
Place in root directory of the project

``cd path/to/the/project``

#### Run tests

`` mvn test ``

### Health Check

http://localhost:8095/healthCheck 