FROM jboss/wildfly:10.1.0.Final
ADD taskqueue-api.war /opt/jboss/wildfly/standalone/deployments/
 
EXPOSE 8080

# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interface
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]