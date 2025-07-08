# 🔑 Come Ottenere la API Key di OpenWeatherMap

Per utilizzare l'applicazione Meteo Italia, è necessario ottenere una chiave API gratuita da OpenWeatherMap.

## Passaggi per Ottenere la API Key

### 1. Registrazione
1. Vai su [OpenWeatherMap](https://openweathermap.org/)
2. Clicca su "Sign Up" nell'angolo in alto a destra
3. Compila il modulo di registrazione con:
   - Nome utente
   - Email
   - Password
   - Conferma password
4. Seleziona il tuo scopo di utilizzo (es. "Education", "Personal", etc.)
5. Clicca su "Create Account"

### 2. Verifica Email
1. Controlla la tua email per il messaggio di verifica
2. Clicca sul link di verifica nell'email
3. Questo attiverà il tuo account

### 3. Ottenere la API Key
1. Accedi al tuo account OpenWeatherMap
2. Vai alla sezione "API Keys" nel menu dell'utente
3. Troverai una chiave API di default già generata
4. Se necessario, puoi generare nuove chiavi cliccando su "Generate"

### 4. Configurazione nell'Applicazione

#### Metodo 1: File di Configurazione
1. Apri il file `src/main/resources/application.properties`
2. Trova la riga: `weather.api.key=your_api_key_here`
3. Sostituisci `your_api_key_here` con la tua chiave API

```properties
weather.api.key=la_tua_chiave_api_qui
```

#### Metodo 2: Variabile d'Ambiente (Consigliato per Docker)
1. Crea un file `.env` nella root del progetto
2. Aggiungi la tua API key:

```bash
WEATHER_API_KEY=la_tua_chiave_api_qui
```

#### Metodo 3: Script di Deploy
Quando esegui `deploy.sh` o `deploy.bat`, lo script ti chiederà automaticamente di inserire la API key.

## Limiti del Piano Gratuito

Il piano gratuito di OpenWeatherMap include:
- ✅ 1,000 chiamate API al giorno
- ✅ 60 chiamate API al minuto
- ✅ Dati meteo correnti
- ✅ Previsioni a 5 giorni
- ❌ Dati storici limitati

Questo è più che sufficiente per l'applicazione Meteo Italia che:
- Aggiorna i dati ogni 5 minuti
- Monitora ~20 città italiane
- Effettua circa 288 chiamate al giorno (20 città × 12 aggiornamenti/ora × 24 ore ÷ 20)

## Risoluzione Problemi

### Errore "API key not valid"
- Verifica che la chiave sia stata copiata correttamente
- Assicurati che l'account sia stato verificato via email
- Attendi qualche minuto dopo la registrazione per l'attivazione

### Errore "API quota exceeded"
- Controlla l'utilizzo nella dashboard OpenWeatherMap
- Riduci la frequenza di aggiornamento se necessario
- Considera l'upgrade a un piano superiore

### Errore di connessione
- Verifica la connessione internet
- Controlla che OpenWeatherMap sia raggiungibile
- Verifica eventuali firewall o proxy

## Sicurezza

⚠️ **IMPORTANTE**: Non condividere mai la tua API key pubblicamente!

- ❌ Non committare la API key nei repository pubblici
- ✅ Usa variabili d'ambiente per la produzione
- ✅ Aggiungi `.env` al file `.gitignore`
- ✅ Usa secret management in produzione

## Link Utili

- [OpenWeatherMap API Documentation](https://openweathermap.org/api)
- [Dashboard OpenWeatherMap](https://home.openweathermap.org/users/sign_in)
- [Pricing Plans](https://openweathermap.org/price)
- [FAQ OpenWeatherMap](https://openweathermap.org/faq)

## Supporto

Se hai problemi con la API key:
1. Controlla la documentazione ufficiale OpenWeatherMap
2. Verifica la tua dashboard per l'utilizzo della API
3. Contatta il supporto OpenWeatherMap se necessario

---

Una volta ottenuta la API key, potrai utilizzare completamente l'applicazione Meteo Italia! 🌤️
