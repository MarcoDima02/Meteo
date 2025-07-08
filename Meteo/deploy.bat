@echo off
REM Script per il deployment dell'applicazione Meteo Italia su Windows

echo 🌤️  Deployment Meteo Italia con Open Meteo API
echo ================================================

REM Controlla se Docker è installato
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker non è installato. Installare Docker Desktop prima di continuare.
    pause
    exit /b 1
)

REM Controlla se Docker Compose è installato
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker Compose non è installato. Installare Docker Compose prima di continuare.
    pause
    exit /b 1
)

echo ✅ Utilizzo Open Meteo API - nessuna API key richiesta!

REM Ferma i container esistenti
echo 🛑 Fermando i container esistenti...
docker-compose down

REM Rimuovi le immagini vecchie
echo 🧹 Rimuovendo le immagini vecchie...
docker rmi meteo_meteo-app:latest 2>nul

REM Builda la nuova immagine
echo 🔨 Building dell'applicazione...
docker-compose build

REM Avvia i servizi
echo 🚀 Avviando i servizi...
docker-compose up -d

REM Attendi che l'applicazione sia pronta
echo ⏳ Attendendo che l'applicazione sia pronta...
timeout /t 30 /nobreak >nul

REM Controlla lo stato dei container
echo 📊 Stato dei container:
docker-compose ps

REM Controlla la salute dell'applicazione
echo 🏥 Controllo salute applicazione...
curl -f http://localhost:8080/api/weather/italy/all >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Applicazione avviata con successo!
    echo.
    echo 🌐 URL disponibili:
    echo    - Applicazione: http://localhost:8080
    echo    - Database H2:  http://localhost:8080/h2-console
    echo    - Nginx:        http://localhost (se abilitato)
    echo.
    echo 📝 Comandi utili:
    echo    - Visualizza log: docker-compose logs -f
    echo    - Ferma app:      docker-compose down
    echo    - Riavvia app:    docker-compose restart
) else (
    echo ❌ Errore nell'avvio dell'applicazione. Controlla i log:
    echo    docker-compose logs meteo-app
)

echo.
echo Premi un tasto per continuare...
pause >nul
