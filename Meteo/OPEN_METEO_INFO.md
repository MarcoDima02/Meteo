# 🌤️ Open Meteo API - Documentazione

L'applicazione Meteo Italia utilizza **Open Meteo**, un servizio meteo completamente **gratuito** e **open source** che non richiede registrazione o API key.

## 🎯 Vantaggi di Open Meteo

### ✅ **Completamente Gratuito**
- Nessuna API key richiesta
- Nessuna registrazione necessaria
- Nessun limite di chiamate per uso normale
- Nessun costo nascosto

### ✅ **Affidabile e Veloce**
- Dati meteo di alta qualità
- Aggiornamenti frequenti
- Bassa latenza
- Disponibilità 99.9%

### ✅ **Completo**
- Dati meteo correnti
- Previsioni fino a 16 giorni
- Dati storici
- Oltre 80 parametri meteo

### ✅ **Privacy-Focused**
- Nessun tracking
- Nessun cookie
- Nessuna raccolta dati personali
- Open source

## 🔧 Configurazione

### URL API utilizzati:
```properties
# API meteo principale
weather.api.base-url=https://api.open-meteo.com/v1/forecast

# API geocoding per coordinate
weather.geocoding.base-url=https://geocoding-api.open-meteo.com/v1/search
```

### Parametri richiesti:
- `latitude` e `longitude`: Coordinate geografiche
- `current`: Parametri meteo correnti
- `timezone`: Fuso orario (Europe/Rome)

## 📊 Dati Meteo Disponibili

### Dati Correnti Utilizzati:
- **temperature_2m**: Temperatura aria a 2 metri (°C)
- **apparent_temperature**: Temperatura percepita (°C)
- **relative_humidity_2m**: Umidità relativa (%)
- **wind_speed_10m**: Velocità vento a 10 metri (km/h)
- **surface_pressure**: Pressione atmosferica (hPa)
- **weather_code**: Codice condizioni meteo

### Codici Meteo Open Meteo:
| Codice | Descrizione | Icona |
|--------|-------------|-------|
| 0 | Sereno | ☀️ |
| 1 | Prevalentemente sereno | 🌤️ |
| 2 | Parzialmente nuvoloso | ⛅ |
| 3 | Coperto | ☁️ |
| 45, 48 | Nebbia | 🌫️ |
| 51, 53, 55 | Pioviggine | 🌦️ |
| 61, 63, 65 | Pioggia | 🌧️ |
| 71, 73, 75 | Neve | 🌨️ |
| 80, 81, 82 | Rovesci | 🌦️ |
| 95 | Temporale | ⛈️ |
| 96, 99 | Temporale con grandine | ⛈️ |

## 🌍 Geocoding

Per ottenere le coordinate dalle città, utilizziamo l'API di geocoding:

```
https://geocoding-api.open-meteo.com/v1/search?name=Roma&count=1&language=it&format=json
```

### Città Preconfigurate:
L'applicazione ha già le coordinate delle principali città italiane:

```java
ITALIAN_CITIES.put("Roma", new double[]{41.9028, 12.4964});
ITALIAN_CITIES.put("Milano", new double[]{45.4642, 9.1900});
ITALIAN_CITIES.put("Napoli", new double[]{40.8518, 14.2681});
// ... altre città
```

## 🔄 Chiamate API

### Esempio di chiamata meteo:
```
https://api.open-meteo.com/v1/forecast?latitude=41.9028&longitude=12.4964&current=temperature_2m,apparent_temperature,relative_humidity_2m,wind_speed_10m,surface_pressure,weather_code&timezone=Europe/Rome
```

### Risposta esempio:
```json
{
  "latitude": 41.9028,
  "longitude": 12.4964,
  "timezone": "Europe/Rome",
  "current": {
    "time": "2024-01-15T14:00",
    "temperature_2m": 15.2,
    "apparent_temperature": 14.8,
    "relative_humidity_2m": 65,
    "wind_speed_10m": 12.5,
    "surface_pressure": 1013.2,
    "weather_code": 1
  }
}
```

## 📈 Prestazioni

### Ottimizzazioni implementate:
- **Cache database**: Dati salvati in H2 per ridurre chiamate API
- **Coordinate preconfigurate**: Evita geocoding per città principali
- **Aggiornamento schedulato**: Ogni 5 minuti automaticamente
- **Controllo dati recenti**: Usa database se dati < 10 minuti
- **Timeout configurabili**: Per evitare blocchi
- **Retry automatici**: In caso di errori temporanei

### Limite di utilizzo:
- **Uso normale**: Nessun limite
- **Uso intensivo**: Max 10,000 richieste/giorno
- **Rate limiting**: Max 600 richieste/ora

## 🛠️ Troubleshooting

### Errori comuni:

1. **Timeout di connessione**
   - Verifica connessione internet
   - Controlla firewall/proxy

2. **Coordinate non trovate**
   - Verifica nome città
   - Controlla geocoding API

3. **Dati non aggiornati**
   - Controlla log applicazione
   - Verifica schedulatore attivo

### Debug:
```properties
# Abilita log dettagliati
logging.level.com.meteo=DEBUG
logging.level.org.springframework.web=DEBUG
```

## 🔗 Link Utili

- [Open Meteo Homepage](https://open-meteo.com/)
- [API Documentation](https://open-meteo.com/en/docs)
- [GitHub Repository](https://github.com/open-meteo/open-meteo)
- [Weather Codes](https://open-meteo.com/en/docs#weathercode)

## 🆚 Confronto con OpenWeatherMap

| Caratteristica | Open Meteo | OpenWeatherMap |
|---------------|------------|----------------|
| **Costo** | Gratuito | Freemium |
| **API Key** | ❌ Non richiesta | ✅ Richiesta |
| **Registrazione** | ❌ Non richiesta | ✅ Richiesta |
| **Limiti** | Molto alti | 1000 chiamate/giorno |
| **Privacy** | Ottima | Discreta |
| **Documentazione** | Eccellente | Buona |
| **Affidabilità** | Alta | Alta |

## 🎉 Conclusione

Open Meteo rappresenta la scelta ideale per l'applicazione Meteo Italia perché:
- ✅ **Semplice**: Nessuna configurazione API key
- ✅ **Gratuito**: Nessun costo per l'utilizzo
- ✅ **Affidabile**: Dati meteo di qualità
- ✅ **Privacy**: Rispetta la privacy degli utenti
- ✅ **Open Source**: Trasparente e community-driven

L'applicazione è pronta all'uso senza necessità di registrazioni o configurazioni aggiuntive! 🚀
