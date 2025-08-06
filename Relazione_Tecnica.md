# Relazione Tecnica - Steam Clone Backend

## Introduzione

Questo documento descrive la struttura, le scelte progettuali e le funzionalità principali del progetto **Steam Clone Backend**, una web application sviluppata in Java utilizzando il framework Spring Boot.

L'obiettivo è realizzare un backend completo e sicuro per una piattaforma di distribuzione di videogiochi, simile per logica a Steam, con gestione utenti, autenticazione, CRUD di giochi e tag, e una libreria personale per ogni utente.

## Struttura dell'applicativo

L'applicazione adotta una struttura modulare e ben organizzata, basata sull'**architettura a livelli (layered architecture)**, tipica delle applicazioni enterprise Spring. Questa separazione permette un'elevata manutenibilità e testabilità del codice.

### Principali Livelli Architetturali

- **Controller (Presentation Layer):** riceve le richieste HTTP e gestisce la logica di routing verso i servizi appropriati
- **Service (Business Logic Layer):** incapsula la logica applicativa, isolando i controller dalla logica di accesso ai dati
- **Repository (Persistence Layer):** fornisce l'accesso al database mediante Spring Data JPA
- **Security Layer:** si occupa dell'autenticazione e autorizzazione mediante JWT e Spring Security

### Struttura dei Pacchetti

```
src/main/java/itsprodigi/matteocasini/steam_clone_backend/
├── config/        # Configurazioni Spring e Security
├── controller/    # REST Controllers
├── dto/          # Data Transfer Objects
├── exception/    # Eccezioni personalizzate
├── filter/       # Filtri JWT per autenticazione
├── model/        # Entità JPA
├── repository/   # Repository JPA
└── service/      # Logica di business
```

Questa struttura garantisce una chiara separazione delle responsabilità, facilitando la scalabilità dell'applicazione e la comprensione del codice.

## Scelte Progettuali

### Sicurezza e Autenticazione

L'autenticazione è gestita tramite **JWT (JSON Web Token)**, che consente una comunicazione stateless tra client e server. Gli utenti possono autenticarsi tramite login e ricevere un token, da includere successivamente nelle richieste HTTP protette.

Sono stati definiti due ruoli principali:
- `ROLE_USER`
- `ROLE_ADMIN`

che determinano il livello di accesso agli endpoint. Le password vengono criptate con **BCrypt**, un algoritmo sicuro e ampiamente utilizzato.

### Persistenza dei Dati

Il database utilizzato è **MySQL**, scelto per la sua affidabilità e semplicità di integrazione con Spring Boot. Per la gestione della persistenza, si fa uso di **Spring Data JPA**, che consente la creazione automatica di query basate sulle convenzioni.

Come chiavi primarie è stato deciso di utilizzare **UUID** anziché ID auto-incrementali, per una maggiore sicurezza e per evitare collisioni in ambienti distribuiti.

### API REST e Scambio Dati

L'applicazione espone **API RESTful**, progettate seguendo le convenzioni HTTP (GET, POST, PUT, DELETE) e utilizzando oggetti **DTO (Data Transfer Object)** per il trasferimento dei dati, separando le entità interne dal payload esposto.

Le richieste in ingresso vengono validate tramite annotazioni Spring (`@Valid`, `@NotBlank`, ecc.), e sono gestite le eccezioni tramite una classe centralizzata di exception handling.

### Containerizzazione

Per facilitare l'esecuzione e il testing del progetto in ambienti diversi, l'intero backend è stato containerizzato usando **Docker**. L'applicazione può essere eseguita in locale tramite `docker-compose`, che avvia contemporaneamente un container per il backend e uno per il database MySQL, inizializzato automaticamente con uno script SQL.

## Funzionalità delle Principali Classi Implementate

### Modelli (Model)

- **User:** rappresenta un utente registrato, con campi come `username`, `email`, `password` e `role`. Ha una relazione `OneToOne` con `UserProfile` e `OneToMany` con `UserGame`, per rappresentare la sua libreria

- **Game:** descrive un videogioco, con campi come titolo, descrizione, prezzo. È in relazione `ManyToMany` con `Tag` e con gli utenti tramite `UserGame`

- **UserProfile:** estensione del modello `User`, con campi come `nickname`, `avatarUrl` e `bio`

### Controller

- **AuthController:** gestisce le operazioni di autenticazione (login e registrazione), restituendo JWT in caso di login corretto

- **UserController:** consente la visualizzazione, modifica ed eliminazione di utenti, oltre alla gestione della propria libreria di giochi

- **GameController:** gestisce le operazioni CRUD sui giochi e l'assegnazione di tag

- **TagController:** espone endpoint per creare e consultare i tag disponibili

### Servizi (Service)

- **UserService:** contiene la logica per la registrazione, aggiornamento e cancellazione degli utenti. Controlla l'autorizzazione tramite il ruolo o l'identità dell'utente

- **GameService:** gestisce le operazioni legate ai giochi, compresa la validazione dell'input e la gestione delle relazioni con i tag

- **UserProfileService:** permette la creazione, visualizzazione e aggiornamento del profilo utente associato

- **UserGameService:** permette agli utenti di aggiungere giochi alla propria libreria, aggiornare lo stato (giocato, wishlist, ecc.) e le ore giocate

### Sicurezza

- **JwtAuthFilter:** è un filtro che intercetta le richieste HTTP, estrae il token JWT, lo valida e, se corretto, imposta l'utente autenticato nel contesto di sicurezza

- **SecurityConfig:** configura le rotte protette, le regole di accesso in base ai ruoli, e abilita CORS e CSRF in base alle necessità

### DTOs (Data Transfer Objects)

I DTO consentono di trasferire in modo sicuro i dati tra client e server, evitando di esporre direttamente le entità del database. Sono stati definiti DTO separati per la registrazione, aggiornamento, visualizzazione di utenti, giochi, profili e tag.

## Considerazioni Finali

Il progetto è stato progettato con attenzione alla chiarezza del codice, alla separazione delle responsabilità e alla sicurezza. L'utilizzo dei ruoli e dei token JWT consente una gestione sicura degli accessi, mentre la containerizzazione con Docker facilita il testing e la distribuzione.

La copertura dei test unitari ha raggiunto il **43%**. Le classi critiche come `UserService` e `GameService` sono state testate in isolamento con JUnit e Mockito.

### Possibili Estensioni Future

In prospettiva, il progetto può essere esteso con uno Store di giochi, implementando funzionalità come:
- Acquisto e download di giochi
- Sistema di recensioni e valutazioni
- Integrazione con servizi esterni per pagamenti