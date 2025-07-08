@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-24
echo Starting Meteo application...
.\mvnw.cmd spring-boot:run
echo Application finished. Press any key to exit.
pause
