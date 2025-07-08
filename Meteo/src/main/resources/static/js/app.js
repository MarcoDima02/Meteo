// App Meteo Italia - JavaScript principale

class MeteoApp {
    constructor() {
        this.baseUrl = '/api/weather';
        this.currentCityData = null;
        this.init();
    }

    init() {
        this.loadAllCitiesWeather();
        this.updateLastUpdateTime();
        
        // Auto-refresh ogni 5 minuti
        setInterval(() => {
            this.loadAllCitiesWeather(false);
        }, 300000);
    }

    // Carica i dati meteo di tutte le città italiane
    async loadAllCitiesWeather(showLoading = true) {
        if (showLoading) {
            this.showLoading();
        }

        try {
            const response = await fetch(`${this.baseUrl}/italy/all`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const weatherData = await response.json();
            this.displayWeatherCards(weatherData);
            this.updateLastUpdateTime();
            
        } catch (error) {
            console.error('Errore nel caricamento dati meteo:', error);
            this.showError('Errore nel caricamento dei dati meteo. Riprova più tardi.');
        } finally {
            if (showLoading) {
                this.hideLoading();
            }
        }
    }

    // Cerca una città specifica
    async searchCity() {
        const cityName = document.getElementById('citySearch').value.trim();
        if (!cityName) {
            alert('Inserisci il nome di una città');
            return;
        }

        this.showLoading();

        try {
            const response = await fetch(`${this.baseUrl}/current/${encodeURIComponent(cityName)}`);
            if (!response.ok) {
                throw new Error(`Città non trovata: ${cityName}`);
            }
            
            const weatherData = await response.json();
            this.displaySingleWeatherCard(weatherData);
            
        } catch (error) {
            console.error('Errore nella ricerca città:', error);
            this.showError(`Errore nella ricerca di "${cityName}". Verifica il nome della città.`);
        } finally {
            this.hideLoading();
        }
    }

    // Mostra le card meteo
    displayWeatherCards(weatherDataArray) {
        const container = document.getElementById('weatherCards');
        container.innerHTML = '';

        weatherDataArray.forEach(weather => {
            const card = this.createWeatherCard(weather);
            container.appendChild(card);
        });

        // Aggiungi animazione
        const cards = container.querySelectorAll('.weather-card');
        cards.forEach((card, index) => {
            setTimeout(() => {
                card.classList.add('fade-in');
            }, index * 100);
        });
    }

    // Mostra una singola card per la ricerca
    displaySingleWeatherCard(weatherData) {
        const container = document.getElementById('weatherCards');
        container.innerHTML = '';
        
        const card = this.createWeatherCard(weatherData);
        container.appendChild(card);
        
        card.classList.add('fade-in');
        
        // Aggiungi un pulsante per tornare alla vista completa
        const backButton = document.createElement('div');
        backButton.className = 'col-12 text-center mt-3';
        backButton.innerHTML = `
            <button class="btn btn-outline-primary" onclick="meteoApp.loadAllCitiesWeather()">
                <i class="fas fa-arrow-left me-2"></i>Torna alla vista completa
            </button>
        `;
        container.appendChild(backButton);
    }

    // Crea una card meteo
    createWeatherCard(weather) {
        const col = document.createElement('div');
        col.className = 'col-lg-3 col-md-4 col-sm-6 mb-4';

        const iconClass = this.getWeatherIcon(weather.icon);
        const timestamp = new Date(weather.timestamp).toLocaleString('it-IT');

        col.innerHTML = `
            <div class="card weather-card h-100 shadow-sm" onclick="meteoApp.showCityDetails('${weather.cityName}')">
                <div class="card-body text-center">
                    <div class="city-name">${weather.cityName}</div>
                    <div class="weather-icon text-primary">
                        <i class="${iconClass}"></i>
                    </div>
                    <div class="temperature">${Math.round(weather.temperature)}°C</div>
                    <div class="feels-like">Percepita: ${Math.round(weather.feelsLike)}°C</div>
                    <div class="weather-description mt-2">${weather.description}</div>
                    
                    <div class="weather-details mt-3">
                        <div class="weather-detail-item">
                            <span><i class="fas fa-tint weather-detail-icon"></i> Umidità</span>
                            <span>${weather.humidity}%</span>
                        </div>
                        <div class="weather-detail-item">
                            <span><i class="fas fa-wind weather-detail-icon"></i> Vento</span>
                            <span>${weather.windSpeed} km/h</span>
                        </div>
                        <div class="weather-detail-item">
                            <span><i class="fas fa-thermometer-half weather-detail-icon"></i> Pressione</span>
                            <span>${weather.pressure} hPa</span>
                        </div>
                    </div>
                    
                    <div class="timestamp">
                        Aggiornato: ${timestamp}
                    </div>
                </div>
            </div>
        `;

        return col;
    }

    // Mostra dettagli città nel modal
    async showCityDetails(cityName) {
        this.currentCityData = cityName;
        
        try {
            const response = await fetch(`${this.baseUrl}/current/${encodeURIComponent(cityName)}`);
            const weather = await response.json();

            const modalTitle = document.getElementById('cityModalTitle');
            const modalBody = document.getElementById('cityModalBody');

            modalTitle.textContent = `Meteo ${weather.cityName}`;

            const iconClass = this.getWeatherIcon(weather.icon);
            const timestamp = new Date(weather.timestamp).toLocaleString('it-IT');

            modalBody.innerHTML = `
                <div class="row">
                    <div class="col-md-4 text-center">
                        <div class="weather-icon text-primary mb-3">
                            <i class="${iconClass}" style="font-size: 4rem;"></i>
                        </div>
                        <h2 class="temperature mb-0">${Math.round(weather.temperature)}°C</h2>
                        <p class="feels-like">Percepita: ${Math.round(weather.feelsLike)}°C</p>
                    </div>
                    <div class="col-md-8">
                        <h5 class="weather-description mb-4">${weather.description}</h5>
                        
                        <div class="row">
                            <div class="col-6">
                                <div class="weather-detail-item">
                                    <span><i class="fas fa-tint text-info me-2"></i> Umidità</span>
                                    <strong>${weather.humidity}%</strong>
                                </div>
                                <div class="weather-detail-item">
                                    <span><i class="fas fa-wind text-success me-2"></i> Vento</span>
                                    <strong>${weather.windSpeed} km/h</strong>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="weather-detail-item">
                                    <span><i class="fas fa-thermometer-half text-warning me-2"></i> Pressione</span>
                                    <strong>${weather.pressure} hPa</strong>
                                </div>
                                <div class="weather-detail-item">
                                    <span><i class="fas fa-clock text-secondary me-2"></i> Aggiornato</span>
                                    <strong>${timestamp}</strong>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;

            const modal = new bootstrap.Modal(document.getElementById('cityModal'));
            modal.show();

        } catch (error) {
            console.error('Errore nel caricamento dettagli città:', error);
            this.showError('Errore nel caricamento dei dettagli della città');
        }
    }

    // Vai alla pagina storico per la città corrente
    viewCityHistory() {
        if (this.currentCityData) {
            window.location.href = `/city-history?city=${encodeURIComponent(this.currentCityData)}`;
        }
    }

    // Converte l'icona API in classe FontAwesome
    getWeatherIcon(iconCode) {
        const iconMap = {
            '01d': 'fas fa-sun',           // clear sky day
            '01n': 'fas fa-moon',          // clear sky night
            '02d': 'fas fa-cloud-sun',     // few clouds day
            '02n': 'fas fa-cloud-moon',    // few clouds night
            '03d': 'fas fa-cloud',         // scattered clouds
            '03n': 'fas fa-cloud',
            '04d': 'fas fa-cloud',         // broken clouds
            '04n': 'fas fa-cloud',
            '09d': 'fas fa-cloud-rain',    // shower rain
            '09n': 'fas fa-cloud-rain',
            '10d': 'fas fa-cloud-sun-rain', // rain day
            '10n': 'fas fa-cloud-moon-rain', // rain night
            '11d': 'fas fa-bolt',          // thunderstorm
            '11n': 'fas fa-bolt',
            '13d': 'fas fa-snowflake',     // snow
            '13n': 'fas fa-snowflake',
            '50d': 'fas fa-smog',          // mist
            '50n': 'fas fa-smog'
        };

        return iconMap[iconCode] || 'fas fa-cloud';
    }

    // Aggiorna l'ora dell'ultimo aggiornamento
    updateLastUpdateTime() {
        const now = new Date().toLocaleString('it-IT');
        const lastUpdateElement = document.getElementById('lastUpdate');
        if (lastUpdateElement) {
            lastUpdateElement.textContent = `Ultimo aggiornamento: ${now}`;
        }
    }

    // Mostra spinner di caricamento
    showLoading() {
        document.getElementById('loadingSpinner').style.display = 'block';
        document.getElementById('weatherSection').style.display = 'none';
    }

    // Nascondi spinner di caricamento
    hideLoading() {
        document.getElementById('loadingSpinner').style.display = 'none';
        document.getElementById('weatherSection').style.display = 'block';
    }

    // Mostra messaggio di errore
    showError(message) {
        const container = document.getElementById('weatherCards');
        container.innerHTML = `
            <div class="col-12">
                <div class="alert alert-danger text-center" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    ${message}
                    <br><br>
                    <button class="btn btn-outline-danger" onclick="meteoApp.loadAllCitiesWeather()">
                        <i class="fas fa-sync-alt me-2"></i>Riprova
                    </button>
                </div>
            </div>
        `;
    }
}

// Funzioni globali
function searchOnEnter(event) {
    if (event.key === 'Enter') {
        meteoApp.searchCity();
    }
}

function searchCity() {
    meteoApp.searchCity();
}

async function refreshWeatherData() {
    try {
        const response = await fetch('/api/weather/refresh', { method: 'POST' });
        if (response.ok) {
            meteoApp.loadAllCitiesWeather();
            
            // Mostra notifica di successo
            const toast = document.createElement('div');
            toast.className = 'position-fixed top-0 end-0 p-3';
            toast.style.zIndex = '9999';
            toast.innerHTML = `
                <div class="toast show" role="alert">
                    <div class="toast-header">
                        <i class="fas fa-check-circle text-success me-2"></i>
                        <strong class="me-auto">Aggiornamento</strong>
                    </div>
                    <div class="toast-body">
                        Dati meteo aggiornati con successo!
                    </div>
                </div>
            `;
            document.body.appendChild(toast);
            
            setTimeout(() => {
                document.body.removeChild(toast);
            }, 3000);
        }
    } catch (error) {
        console.error('Errore nell\'aggiornamento:', error);
    }
}

// Inizializza l'app quando il DOM è pronto
let meteoApp;
document.addEventListener('DOMContentLoaded', function() {
    meteoApp = new MeteoApp();
});
