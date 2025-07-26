#!/bin/bash

# Path to your Tomcat installation
TOMCAT_DIR="$HOME/home/dataStore/build/tomcat/apache-tomcat-9.0.107"

# Make sure catalina.sh is executable
chmod +x "$TOMCAT_DIR/bin/catalina.sh"

"$TOMCAT_DIR/bin/catalina.sh" stop || true

sleep 5

# Export JPDA options (default debug port: 8000)
export JPDA_ADDRESS=8000
export JPDA_TRANSPORT=dt_socket

# Start Tomcat with JPDA
"$TOMCAT_DIR/bin/catalina.sh" jpda start >> ~/tomcat.log 2>&1

echo "Tomcat server has started successfully in port 8080 :)"
