# Meteo Italia - Applicazione Spring Boot

Un'applicazione web per visualizzare le condizioni meteorologiche delle principali città italiane, con database H2 per memorizzare lo storico dei dati.

## Caratteristiche

- 🌤️ **Dati meteo in tempo reale** per le principali città italiane
- 📊 **Database H2** integrato per memorizzare lo storico
- 🔄 **Aggiornamento automatico** ogni 5 minuti
- 📱 **Interfaccia responsive** con Bootstrap 5
- 📈 **Grafici storici** con Chart.js
- 🎨 **Design moderno** e user-friendly
- 🆓 **API Open Meteo** completamente gratuita (nessuna API key richiesta!)

## Città Supportate

Roma, Milano, Napoli, Torino, Palermo, Genova, Bologna, Firenze, Bari, Catania, Venezia, Verona, Messina, Padova, Trieste, Brescia, Parma, Prato, Modena, Reggio Calabria, Cisternino

## Tecnologie Utilizzate

### Backend
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Spring Web**
- **WebFlux** per chiamate API esterne
- **Thymeleaf** per i template
- **Open Meteo API** per dati meteo

### Frontend
- **HTML5/CSS3**
- **Bootstrap 5**
- **JavaScript ES6+**
- **Chart.js** per i grafici
- **Font Awesome** per le icone

## Configurazione

### 🎉 Nessuna Configurazione Richiesta!

L'applicazione utilizza **Open Meteo API** che è:
- ✅ **Completamente gratuita**
- ✅ **Nessuna API key richiesta**
- ✅ **Nessuna registrazione necessaria**
- ✅ **Dati meteo di alta qualità**

Per maggiori informazioni su Open Meteo, consulta [OPEN_METEO_INFO.md](OPEN_METEO_INFO.md).

### 2. Configurazione Database


### Database H2

- In modalità **locale**: database H2 in-memory (dati persi al riavvio)
- In modalità **Docker**: database H2 persistente su file (volume Docker)

Per accedere alla console H2 (solo per debug):
- URL: http://localhost:8080/h2-console
- JDBC URL (Docker): `jdbc:h2:file:/app/data/meteo_db`
- Username: `sa`
- Password: `password`

## Installazione e Avvio


### Prerequisiti

- **Per esecuzione locale:**
  - Java 17 o superiore
  - Maven 3.6 o superiore
- **Per esecuzione in Docker:** 🐳
  - Docker Desktop installato e **avviato**
  - Docker Compose (incluso in Docker Desktop)
  - **Windows**: PowerShell o Command Prompt
  - **Linux/macOS**: Bash shell

### Avvio Applicazione (modalità locale)

```bash
# Clona il repository (se necessario)
git clone [repository-url]
cd meteo-app

# Compila e avvia l'applicazione
mvn spring-boot:run

# Oppure
mvn clean package
java -jar target/meteo-app-0.0.1-SNAPSHOT.jar
```

L'applicazione sarà disponibile su: http://localhost:8080

### Avvio Applicazione con Docker 🐳

#### Windows (PowerShell):
```powershell
# Usando lo script predefinito (consigliato)
.\deploy.bat

# Oppure manualmente:
docker-compose down
docker-compose up --build
```

#### Linux/macOS:
```bash
# Usando lo script predefinito (consigliato)
./deploy.sh

# Oppure manualmente:
docker-compose down
docker-compose up --build
```

#### 🌐 Accesso all'applicazione:
- **Applicazione principale**: http://localhost:8080
- **Nginx reverse proxy**: http://localhost:8081
- **Console H2**: http://localhost:8080/h2-console

#### 🔧 Comandi utili:
```powershell
# Visualizza log in tempo reale
docker-compose logs -f

# Ferma l'applicazione
docker-compose down

# Riavvia solo l'app
docker-compose restart meteo-app

# Stato dei container
docker-compose ps
```

**✅ Caratteristiche Docker:**
- ✅ **Multi-stage build** per immagini ottimizzate
- ✅ **Dati persistenti** grazie al volume Docker
- ✅ **Health checks** automatici
- ✅ **Nginx reverse proxy** opzionale
- ✅ **Utente non-root** per sicurezza
- ✅ **Compatibilità Windows/Linux** (line endings corretti)

## Struttura del Progetto

```
src/
├── main/
│   ├── java/com/meteo/
│   │   ├── MeteoApplication.java          # Classe principale
│   │   ├── controller/
│   │   │   ├── WeatherController.java     # REST API
│   │   │   └── WebController.java         # Pagine web
│   │   ├── service/
│   │   │   └── WeatherService.java        # Logica business
│   │   ├── entity/
│   │   │   └── WeatherData.java           # Entità JPA
│   │   ├── repository/
│   │   │   └── WeatherDataRepository.java # Repository JPA
│   │   └── dto/
│   │       ├── WeatherApiResponse.java    # DTO API esterna
│   │       └── WeatherResponse.java       # DTO risposta
│   └── resources/
│       ├── templates/                     # Template Thymeleaf
│       │   ├── index.html
│       │   └── history.html
│       ├── static/
│       │   ├── css/style.css              # Stili CSS
│       │   └── js/
│       │       ├── app.js                 # JavaScript principale
│       │       └── history.js             # JavaScript storico
│       └── application.properties         # Configurazione
```

## API Endpoints

### REST API

- `GET /api/weather/italy/all` - Tutti i dati meteo delle città italiane
- `GET /api/weather/current/{cityName}` - Meteo corrente per una città
- `GET /api/weather/history/{cityName}?days={n}` - Storico meteo per una città
- `POST /api/weather/refresh` - Forza aggiornamento dati

### Pagine Web

- `/` - Homepage con tutte le città
- `/history` - Pagina storico meteo
- `/h2-console` - Console database H2

## Funzionalità

### 1. Vista Principale

- Visualizzazione card meteo per tutte le città italiane
- Aggiornamento automatico ogni 5 minuti
- Ricerca città specifica
- Click su card per dettagli

### 2. Dettagli Città

- Modal con informazioni dettagliate
- Temperatura, umidità, vento, pressione
- Link per visualizzare lo storico

### 3. Storico Meteo

- Grafici con Chart.js
- Tabella dati dettagliata
- Filtro per periodo (1-30 giorni)
- Selezione città

### 4. Aggiornamento Automatico

- Task schedulato ogni 5 minuti
- Pulizia automatica dati vecchi (> 7 giorni)
- Pulsante aggiornamento manuale

## Personalizzazione

### Aggiungere Nuove Città

Modifica la lista `ITALIAN_CITIES` in `WeatherService.java`:

```java
private static final List<String> ITALIAN_CITIES = Arrays.asList(
    "Roma", "Milano", "Napoli", // ... aggiungi qui
    "NuovaCitta"
);
```

### Modificare Intervallo Aggiornamento

Nel file `application.properties`:

```properties
# Intervallo in millisecondi (300000 = 5 minuti)
weather.update.interval=300000
```


### Personalizzare Database

- In locale puoi usare H2 in-memory o su file modificando `application.properties`.
- In Docker la persistenza è già configurata in `application-docker.properties` e nel volume Docker.

## Troubleshooting

### Errori Comuni

1. **API Key non valida**
   - Non serve più alcuna API key: viene usato solo Open-Meteo (gratuito)

2. **Errori di connessione**
   - Verifica la connessione internet
   - Controlla che Open-Meteo sia raggiungibile

3. **Database H2**
   - In caso di errori, riavvia l'applicazione
   - I dati in-memory verranno ricreati

4. **Problemi Docker** 🐳
   - **Docker Desktop non avviato**: Assicurati che Docker Desktop sia in esecuzione
   - **Errore "entrypoint.sh not found"**: Ricompila con `docker-compose build --no-cache`
   - **Warning "version obsolete"**: Il warning è stato corretto, ignora se ancora appare
   - **Container non si avvia**: Controlla i log con `docker-compose logs meteo-app`

### Soluzioni Rapide Docker

```powershell
# Ricompila tutto da zero
docker-compose down
docker-compose build --no-cache
docker-compose up

# Ripulisci tutto e riavvia
docker-compose down -v
docker system prune -f
docker-compose up --build
```

### Log di Debug

Per abilitare log dettagliati, modifica `application.properties`:

```properties
logging.level.com.meteo=DEBUG
logging.level.org.springframework.web=DEBUG
```

## Sviluppi Futuri

- [ ] Previsioni meteo a 5 giorni
- [ ] Notifiche push per allerte meteo
- [ ] Geolocalizzazione automatica
- [ ] Tema dark/light
- [ ] API per mobile app
- [ ] Cache Redis per performance
- [ ] Database PostgreSQL per produzione

## Licenza

Questo progetto è distribuito sotto licenza MIT. Vedi file LICENSE per dettagli.

## Contributori

- **Sviluppatore**: Dima Marco
- **Data**: Luglio 2025
- **Ultima build Docker**: 14 Luglio 2025 ✅

---

## 🎉 Stato del Progetto

**✅ COMPLETAMENTE FUNZIONALE**

L'applicazione è stata testata e funziona correttamente sia in modalità locale che Docker. Tutti i problemi di compatibilità Windows/Linux sono stati risolti.

**Quick Start:**
```powershell
# Windows - Avvio rapido
.\deploy.bat

# Accedi all'app: http://localhost:8080
# Nginx proxy: http://localhost:8081
```
