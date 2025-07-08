// History page JavaScript per Meteo Italia

class HistoryApp {
    constructor() {
        this.baseUrl = '/api/weather';
        this.chart = null;
        this.init();
    }

    init() {
        // Controlla se c'è una città nei parametri URL
        const urlParams = new URLSearchParams(window.location.search);
        const cityParam = urlParams.get('city');
        
        if (cityParam) {
            // Normalizza e seleziona la città anche se differisce per maiuscole/minuscole/spazi
            const citySelect = document.getElementById('historyCitySelect');
            const options = Array.from(citySelect.options);
            const normalizedParam = cityParam.trim().toLowerCase();
            let found = false;
            for (const opt of options) {
                if (opt.value.trim().toLowerCase() === normalizedParam) {
                    citySelect.value = opt.value;
                    found = true;
                    break;
                }
            }
            if (found) {
                this.loadHistory();
            }
        }
    }

    // Carica lo storico meteo
    async loadHistory() {
        const citySelect = document.getElementById('historyCitySelect');
        const daysSelect = document.getElementById('historyDaysSelect');
        
        const cityName = citySelect.value;
        const days = parseInt(daysSelect.value);

        if (!cityName) {
            alert('Seleziona una città');
            return;
        }

        this.showLoading();
        this.hideResults();

        try {
            const response = await fetch(`${this.baseUrl}/history/${encodeURIComponent(cityName)}?days=${days}`);
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const historyData = await response.json();
            
            if (historyData.length === 0) {
                this.showNoData();
            } else {
                this.displayHistory(historyData, cityName, days);
            }
            
        } catch (error) {
            console.error('Errore nel caricamento storico:', error);
            this.showError('Errore nel caricamento dei dati storici. Riprova più tardi.');
        } finally {
            this.hideLoading();
        }
    }

    // Mostra i dati storici
    displayHistory(historyData, cityName, days) {
        this.createChart(historyData, cityName, days);
        this.createTable(historyData);
        this.showResults();
    }

    // Crea il grafico delle temperature
    createChart(historyData, cityName, days) {
        const ctx = document.getElementById('temperatureChart').getContext('2d');
        
        // Distruggi il grafico esistente se presente
        if (this.chart) {
            this.chart.destroy();
        }

        // Prepara i dati per il grafico
        const labels = historyData.map(item => {
            const date = new Date(item.timestamp);
            return date.toLocaleDateString('it-IT', { 
                month: 'short', 
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
        });

        const temperatures = historyData.map(item => item.temperature);
        const feelsLike = historyData.map(item => item.feelsLike);
        const humidity = historyData.map(item => item.humidity);

        // Aggiorna il titolo del grafico
        document.getElementById('chartTitle').textContent = 
            `Andamento Temperature - ${cityName} (ultimi ${days} ${days === 1 ? 'giorno' : 'giorni'})`;

        this.chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels.reverse(), // Inverti per mostrare dal più vecchio al più recente
                datasets: [
                    {
                        label: 'Temperatura (°C)',
                        data: temperatures.reverse(),
                        borderColor: 'rgb(75, 192, 192)',
                        backgroundColor: 'rgba(75, 192, 192, 0.1)',
                        tension: 0.4,
                        fill: true
                    },
                    {
                        label: 'Temperatura Percepita (°C)',
                        data: feelsLike.reverse(),
                        borderColor: 'rgb(255, 99, 132)',
                        backgroundColor: 'rgba(255, 99, 132, 0.1)',
                        tension: 0.4,
                        fill: false
                    },
                    {
                        label: 'Umidità (%)',
                        data: humidity.reverse(),
                        borderColor: 'rgb(54, 162, 235)',
                        backgroundColor: 'rgba(54, 162, 235, 0.1)',
                        tension: 0.4,
                        fill: false,
                        yAxisID: 'y1'
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    title: {
                        display: true,
                        text: `Storico Meteo - ${cityName}`
                    },
                    legend: {
                        display: true,
                        position: 'top'
                    }
                },
                scales: {
                    y: {
                        type: 'linear',
                        display: true,
                        position: 'left',
                        title: {
                            display: true,
                            text: 'Temperatura (°C)'
                        }
                    },
                    y1: {
                        type: 'linear',
                        display: true,
                        position: 'right',
                        title: {
                            display: true,
                            text: 'Umidità (%)'
                        },
                        grid: {
                            drawOnChartArea: false,
                        },
                    },
                    x: {
                        title: {
                            display: true,
                            text: 'Data/Ora'
                        }
                    }
                },
                interaction: {
                    mode: 'index',
                    intersect: false,
                },
                elements: {
                    point: {
                        radius: 3,
                        hoverRadius: 6
                    }
                }
            }
        });
    }

    // Crea la tabella dei dati
    createTable(historyData) {
        const tbody = document.getElementById('historyTableBody');
        tbody.innerHTML = '';

        historyData.forEach(item => {
            const row = document.createElement('tr');
            const timestamp = new Date(item.timestamp).toLocaleString('it-IT');
            
            row.innerHTML = `
                <td>${timestamp}</td>
                <td><strong>${Math.round(item.temperature)}°C</strong></td>
                <td>${Math.round(item.feelsLike)}°C</td>
                <td>
                    <i class="${this.getWeatherIcon(item.icon)} me-2"></i>
                    ${item.description}
                </td>
                <td>${item.humidity}%</td>
                <td>${item.windSpeed} km/h</td>
            `;
            
            tbody.appendChild(row);
        });
    }

    // Converte l'icona API in classe FontAwesome
    getWeatherIcon(iconCode) {
        const iconMap = {
            '01d': 'fas fa-sun text-warning',
            '01n': 'fas fa-moon text-secondary',
            '02d': 'fas fa-cloud-sun text-warning',
            '02n': 'fas fa-cloud-moon text-secondary',
            '03d': 'fas fa-cloud text-secondary',
            '03n': 'fas fa-cloud text-secondary',
            '04d': 'fas fa-cloud text-dark',
            '04n': 'fas fa-cloud text-dark',
            '09d': 'fas fa-cloud-rain text-primary',
            '09n': 'fas fa-cloud-rain text-primary',
            '10d': 'fas fa-cloud-sun-rain text-primary',
            '10n': 'fas fa-cloud-moon-rain text-primary',
            '11d': 'fas fa-bolt text-warning',
            '11n': 'fas fa-bolt text-warning',
            '13d': 'fas fa-snowflake text-info',
            '13n': 'fas fa-snowflake text-info',
            '50d': 'fas fa-smog text-secondary',
            '50n': 'fas fa-smog text-secondary'
        };

        return iconMap[iconCode] || 'fas fa-cloud text-secondary';
    }

    // Mostra spinner di caricamento
    showLoading() {
        document.getElementById('historyLoadingSpinner').style.display = 'block';
    }

    // Nascondi spinner di caricamento
    hideLoading() {
        document.getElementById('historyLoadingSpinner').style.display = 'none';
    }

    // Mostra i risultati
    showResults() {
        document.getElementById('historyChart').style.display = 'block';
        document.getElementById('historyTable').style.display = 'block';
        
        // Aggiungi animazione
        setTimeout(() => {
            document.getElementById('historyChart').classList.add('fade-in');
            document.getElementById('historyTable').classList.add('fade-in');
        }, 100);
    }

    // Nascondi i risultati
    hideResults() {
        document.getElementById('historyChart').style.display = 'none';
        document.getElementById('historyTable').style.display = 'none';
        document.getElementById('noDataMessage').style.display = 'none';
    }

    // Mostra messaggio nessun dato
    showNoData() {
        document.getElementById('noDataMessage').style.display = 'block';
    }

    // Mostra messaggio di errore
    showError(message) {
        const container = document.querySelector('.container');
        const errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger text-center mt-4';
        errorDiv.innerHTML = `
            <i class="fas fa-exclamation-triangle me-2"></i>
            ${message}
        `;
        container.appendChild(errorDiv);
        
        setTimeout(() => {
            container.removeChild(errorDiv);
        }, 5000);
    }
}

// Funzione globale per caricare lo storico
function loadHistory() {
    historyApp.loadHistory();
}

// Inizializza l'app quando il DOM è pronto
let historyApp;
document.addEventListener('DOMContentLoaded', function() {
    historyApp = new HistoryApp();
});
