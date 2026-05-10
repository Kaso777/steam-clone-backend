# Steam Clone Backend API

Questo progetto implementa il backend di una piattaforma di gestione utenti e giochi, ispirata a Steam. Fornisce un'API RESTful per la gestione di utenti, profili utente, giochi, librerie personali e tag, integrando la sicurezza e la persistenza dei dati.

## 🛠️ Tecnologie Utilizzate

- **Linguaggio:** Java 21
- **Framework:** Spring Boot 3.5.3
- **Sicurezza:** Spring Security (con JWT - JSON Web Tokens)
- **Persistenza Dati:** Spring Data JPA (Hibernate)
- **Database:** MySQL 8.0
- **Gestione Dipendenze:** Apache Maven
- **Containerizzazione:** Docker & Docker Compose
- **Test:** JUnit 5, Mockito, Spring Security Test
- **Copertura Codice:** Jacoco
- **Testing API:** Postman

## 📋 Prerequisiti

Per compilare il progetto, eseguire i test e avviare l'applicazione, assicurarsi di avere installati i seguenti strumenti:

- Java Development Kit (JDK) 21 o superiore
- Docker Engine e Docker Compose

## Configurazione locale

Il progetto include un file `.env.example` con valori demo per avviare l'applicazione in locale.
Per usare variabili personalizzate, copiare `.env.example` in `.env` e modificare i valori secondo il proprio ambiente.

Il file `.env` non deve essere versionato. Le credenziali presenti nella documentazione e nello script SQL sono account demo locali creati per testare l'applicazione.

## 🚀 Setup e Avvio dell'Applicazione

### Compilare il progetto e generare il file JAR
1. **Aprire un terminale** nella directory principale del progetto (`steam-clone-backend/`)
2. **Eseguire il comando:**
   
   ```bash
   ./mvnw clean package
   # oppure, se Maven è installato globalmente
   mvn clean package
   ```
   
   Questo comando:
   - Pulisce il progetto
   - Compila il codice sorgente
   - Esegue i test
   - Genera un file JAR eseguibile nella cartella `target/`
   - Il file JAR sarà chiamato `steam-clone-backend-0.0.1-SNAPSHOT.jar`

### Avvio con Docker Compose

Seguire questi passaggi per avviare sia il database MySQL che l'applicazione Spring Boot, entrambi come container Docker.

1. **Aprire un terminale** nella directory principale del progetto (`steam-clone-backend/`)

2. **Avviare i container Docker:**
   
   Per la prima volta, o ogni volta che si modifica il codice Java o il `Dockerfile`:
   
   ```bash
   docker compose up --build -d
   ```
   
   - `--build`: Forza la ricostruzione dell'immagine Docker dell'applicazione Spring Boot. Questo è **fondamentale** ogni volta che si modifica il codice Java
   - `-d`: Avvia i container in modalità background
   
   Se le immagini sono già state builddate e non è stato modificato il codice:
   
   ```bash
   docker compose up -d
   ```

3. **Verifica del funzionamento:**
   - Al primo avvio del container MySQL, lo script `init.sql` verrà eseguito per creare e popolare il database
   - L'applicazione Spring Boot sarà disponibile su `http://localhost:8080`

## 🧪 Testing dell'API con Postman

È inclusa una collection Postman per testare tutti gli endpoint dell'API.

### Importazione della Collection

1. Aprire Postman
2. Cliccare su `File > Import` (o l'icona `Import` nella sidebar)
3. Selezionare il file `SteamClone API.postman_collection.json` fornito nella cartella `postman/`

### Flusso di Test Manuale

Il database viene creato con già un ADMIN ed uno USER con le seguenti credenziali:

#### Credenziali Pre-configurate

- **ADMIN**
  - Username: `admin`
  - Password: `adminpass`

- **USER**
  - Username: `user`  
  - Password: `userpass`

#### Procedura di Autenticazione

1. **Login e Token JWT:**
   - Eseguire la richiesta `Auth > User Login`
   - Nel body della richiesta, inserire le credenziali di un utente (es. `admin/adminpass` o `user/userpass`)
   - Dopo aver inviato la richiesta, **copiare il token JWT** dalla risposta JSON
   - Incollare questo token nella variabile d'ambiente `ADMIN_token` o `USER_token` (a seconda di chi hai loggato) nell'ambiente Postman

### 🔒 Autorizzazioni

- Le uniche due route accessibili senza autenticazione sono **login** e **register**
- Agli **ADMIN** sono concesse tutte le azioni
- Agli **USER** è concesso fare ricerche ma non modifiche o creazioni, eccetto per il proprio account

## 👤 Credenziali di Test Pre-Popolate

Lo script `init.sql` popola il database con i seguenti utenti di test:

| Ruolo | Username | Password | ID |
|-------|----------|----------|-------|
| **Amministratore** | `admin` | `adminpass` | `11111111-1111-1111-1111-111111111111` |
| **Utente Standard** | `user` | `userpass` | `22222222-2222-2222-2222-222222222222` |

> **Nota:** Nelle variabili della collection sono salvate alcune di queste credenziali per semplificare l'utilizzo e sono già pronte le voci per i token JWT da inserire quando si effettua il login.

## ✅ Esecuzione dei Test Unitari e Report Jacoco

Per eseguire tutti i test del progetto e generare il report di copertura del codice:

1. **Aprire un terminale** nella directory principale del progetto (`steam-clone-backend/`)

2. **Eseguire il comando:**
   
   ```bash
   ./mvnw clean verify
   # oppure
   mvn clean verify
   ```
   
   Questo comando:
   - Pulisce il progetto
   - Esegue tutti i test (unitari e di integrazione)
   - Genera il report di copertura Jacoco

3. **Visualizzare il report:**
   
   Il report HTML sarà disponibile in: `target/site/jacoco/index.html`

### 📊 Copertura del Codice

Alla consegna il report registra il **43%** di copertura del codice.
