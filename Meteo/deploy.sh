#!/bin/bash

# Script per il deployment dell'applicazione Meteo Italia

echo "🌤️  Deployment Meteo Italia con Open Meteo API"
echo "================================================"

# Controlla se Docker è installato
if ! command -v docker &> /dev/null; then
    echo "❌ Docker non è installato. Installare Docker prima di continuare."
    exit 1
fi

# Controlla se Docker Compose è installato
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose non è installato. Installare Docker Compose prima di continuare."
    exit 1
fi

echo "✅ Utilizzo Open Meteo API - nessuna API key richiesta!"

# Ferma i container esistenti
echo "🛑 Fermando i container esistenti..."
docker-compose down

# Rimuovi le immagini vecchie
echo "🧹 Rimuovendo le immagini vecchie..."
docker rmi meteo_meteo-app:latest 2>/dev/null || true

# Builda la nuova immagine
echo "🔨 Building dell'applicazione..."
docker-compose build

# Avvia i servizi
echo "🚀 Avviando i servizi..."
docker-compose up -d

# Attendi che l'applicazione sia pronta
echo "⏳ Attendendo che l'applicazione sia pronta..."
sleep 30

# Controlla lo stato dei container
echo "📊 Stato dei container:"
docker-compose ps

# Controlla la salute dell'applicazione
echo "🏥 Controllo salute applicazione..."
if curl -f http://localhost:8080/api/weather/italy/all > /dev/null 2>&1; then
    echo "✅ Applicazione avviata con successo!"
    echo ""
    echo "🌐 URL disponibili:"
    echo "   - Applicazione: http://localhost:8080"
    echo "   - Database H2:  http://localhost:8080/h2-console"
    echo "   - Nginx:        http://localhost (se abilitato)"
    echo ""
    echo "📝 Comandi utili:"
    echo "   - Visualizza log: docker-compose logs -f"
    echo "   - Ferma app:      docker-compose down"
    echo "   - Riavvia app:    docker-compose restart"
else
    echo "❌ Errore nell'avvio dell'applicazione. Controlla i log:"
    echo "   docker-compose logs meteo-app"
fi
