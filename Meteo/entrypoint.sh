#!/bin/sh
# Entrypoint per fix permessi volume H2 e avvio app
chown -R meteo:meteo /app/data
exec java $JAVA_OPTS -jar app.jar
