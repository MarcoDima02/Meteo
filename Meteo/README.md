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

Roma, Milano, Napoli, Torino, Palermo, Genova, Bologna, Firenze, Bari, Catania, Venezia, Verona, Messina, Padova, Trieste, Brescia, Parma, Prato, Modena, Reggio Calabria

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

L'applicazione utilizza un database H2 in-memory configurato automaticamente. I dati vengono persi al riavvio dell'applicazione.

Per accedere alla console H2:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:meteo_db`
- Username: `sa`
- Password: `password`

## Installazione e Avvio

### Prerequisiti

- Java 17 o superiore
- Maven 3.6 o superiore

### Avvio Applicazione

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

Per utilizzare un database persistente, modifica `application.properties`:

```properties
# Esempio per database file H2
spring.datasource.url=jdbc:h2:file:./data/meteo_db
spring.jpa.hibernate.ddl-auto=update
```

## Troubleshooting

### Errori Comuni

1. **API Key non valida**

   - Verifica che la chiave API sia corretta
   - Controlla i limiti di utilizzo su OpenWeatherMap
2. **Errori di connessione**

   - Verifica la connessione internet
   - Controlla che OpenWeatherMap sia raggiungibile
3. **Database H2**

   - In caso di errori, riavvia l'applicazione
   - I dati in-memory verranno ricreati

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

- Sviluppatore: Dima Marco
- Data: Luglio 2025
