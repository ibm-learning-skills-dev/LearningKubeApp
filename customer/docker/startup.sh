#!/bin/bash
set -e

# Set basic java options
export JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

# open the secrets
if [ -d  /var/run/secrets/binding-refarch-cloudantdb ] 
then
  cloudant_username=`cat /var/run/secrets/binding-refarch-cloudantdb/binding | jq '.username' | sed -e 's/"//g'`
  cloudant_password=`cat /var/run/secrets/binding-refarch-cloudantdb/binding | jq '.password' | sed -e 's/"//g'`
  cloudant_host=`cat /var/run/secrets/binding-refarch-cloudantdb/binding | jq '.host' | sed -e 's/"//g'`
  cloudant_port=`cat /var/run/secrets/binding-refarch-cloudantdb/binding | jq '.port' | sed -e 's/"//g'`
  cloudant_proto=`cat /var/run/secrets/binding-refarch-cloudantdb/binding | jq '.url' | sed -e 's/"//g' | sed -e 's|://.*||'`

  JAVA_OPTS="${JAVA_OPTS} -Dspring.application.cloudant.username=${cloudant_username} -Dspring.application.cloudant.password=${cloudant_password} -Dspring.application.cloudant.host=${cloudant_host} -Dspring.application.cloudant.port=${cloudant_port} -Dspring.application.cloudant.protocol=${cloudant_proto}"
fi
echo "Starting with Java Options ${JAVA_OPTS}"

# Start the application
exec java ${JAVA_OPTS} -jar /app.jar

