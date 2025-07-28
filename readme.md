
# Relazione Tecnica

**Progetto:** Steam Clone Backend  
**Autore:** Matteo Casini  
**Data:** luglio 2025

---

## 1. Descrizione Generale del Progetto

Il progetto è un clone semplificato di Steam, sviluppato come applicazione backend con Java e Spring Boot.  
Le principali funzionalità sono:

- Registrazione e login degli utenti con JWT
- Gestione di ruoli (ROLE_USER, ROLE_ADMIN)
- CRUD su giochi e tag
- Libreria personale: stato di gioco e ore giocate
- API REST documentate e testabili via Postman

Il database utilizzato è MySQL. Il deployment è orchestrato con Docker Compose.

---

## 2. Tecnologie Utilizzate

- **Java 21** – Linguaggio di programmazione  
- **Spring Boot** – Framework per il backend REST  
- **Spring Security + JWT** – Autenticazione e autorizzazione  
- **Spring Data JPA / Hibernate** – ORM e persistenza su MySQL  
- **MySQL 8** – Database relazionale  
- **Docker & Docker Compose** – Containerizzazione dell’applicazione e del DB  
- **Jacoco** – Analisi della copertura dei test (41%)  
- **Postman** – Testing degli endpoint REST  

---

## 3. Struttura del Progetto

L’applicazione segue l’architettura MVC (Model–View–Controller), con separazione in pacchetti:

```

steam-clone-backend
├── src
│   ├── main
│   │   ├── java/itsprodigi/matteocasini/steam\_clone\_backend
│   │   │   ├── controller   # gestisce le richieste HTTP
│   │   │   ├── service      # logica di business
│   │   │   ├── repository   # interfacce JpaRepository
│   │   │   ├── model        # entità JPA
│   │   │   ├── dto          # Data Transfer Object
│   │   │   └── security     # configurazione Spring Security e JWT
│   └── resources
│       ├── application.properties
│       └── application-docker.properties
└── docker-compose.yml       # definizione dei container (MySQL + app)

````

- **Controller**: riceve HTTP, valida input, chiama i service  
- **Service**: incapsula la logica di business, astratta dalle tecnologie  
- **Repository**: mappa entità e query al DB con Spring Data JPA  
- **Security**: filtro JWT, configurazioni di accesso e ruoli  

---

## 4. Casi d’Uso Principali

1. **Registrazione utente**  
   - Endpoint: `POST /api/auth/register`  
   - Input: username, email, password  
   - Output: conferma registrazione  

2. **Login e ottenimento token**  
   - Endpoint: `POST /api/auth/login`  
   - Input: username, password  
   - Output: JWT da usare negli header Authorization  

3. **Visualizzazione giochi**  
   - Endpoint: `GET /api/games`  
   - Descrive lista di tutti i giochi disponibili  

4. **Assegnazione tag a un gioco**  
   - Endpoint: `POST /api/games/{id}/tags`  
   - Input: lista di ID tag  

5. **Gestione libreria utente**  
   - Endpoint: `GET /api/users/{userId}/library`  
   - Endpoint: `POST /api/users/{userId}/library` (aggiunta)  
   - Input: gameId, status, hoursPlayed  

---

## 5. Persistenza e Relazioni JPA

- **OneToOne**: `User` ↔ `UserProfile`  
- **OneToMany / ManyToOne**: `User` ↔ `UserGame`  
- **ManyToMany**: `Game` ↔ `Tag` (join table `game_tags`)  

Le entità sono annotate con `@Entity`, `@Table`, e le relazioni con `@OneToOne`, `@OneToMany`, `@ManyToMany` nel package `model`.

---

## 6. Containerizzazione con Docker

- **`Dockerfile`**:
  ```dockerfile
  FROM eclipse-temurin:21-jdk
  WORKDIR /app
  COPY target/steam-clone-backend-0.0.1-SNAPSHOT.jar app.jar
  EXPOSE 8080
  ENTRYPOINT ["java", "-jar", "app.jar"]
````

* **`docker-compose.yml`**:

  ```yaml
  version: '3.8'
  services:
    mysql:
      image: mysql:8.0
      environment:
        MYSQL_ROOT_PASSWORD: root
        MYSQL_DATABASE: steamdb
        MYSQL_USER: user
        MYSQL_PASSWORD: password
      ports:
        - "3307:3306"
      volumes:
        - db_data:/var/lib/mysql
        - ./docker/sql:/docker-entrypoint-initdb.d
    app:
      build: .
      environment:
        SPRING_PROFILES_ACTIVE: docker
      ports:
        - "8080:8080"
      depends_on:
        - mysql
  volumes:
    db_data:
  ```
* **`application-docker.properties`**:

  ```properties
  spring.datasource.url=jdbc:mysql://mysql:3306/steamdb
  spring.datasource.username=user
  spring.datasource.password=password
  spring.jpa.hibernate.ddl-auto=none
  management.endpoints.web.exposure.include=health
  ```

Al primo avvio, MySQL esegue automaticamente lo script `init.sql` presente in `./docker/sql`.

---

## 7. Testing e Copertura

* Test implementati con **JUnit 5** e **Spring Boot Test**
* **Jacoco** configurato in `pom.xml` genera report in `target/site/jacoco/index.html`
* Copertura attuale: **41%**

---

> **Next Steps**: integra la Postman Collection e aggiungi, se vuoi, diagrammi UML dei modelli o dei flussi di autenticazione.

---

*Fine della relazione tecnica.*
