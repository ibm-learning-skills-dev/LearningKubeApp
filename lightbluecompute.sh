#!/bin/bash

printf "IBMid:"
read userid
printf "Password:"
stty -echo
read password
stty echo
starttime=`date`

bx login -a api.ng.bluemix.net -u "$userid" -p "$password" | tee login.out
logerr=`grep FAILED login.out | wc -l`
rm login.out
if [ $logerr -eq 1 ]; then
  echo "#    Login failed... Exiting"
  exit
fi

suffix=`echo -e $userid | tr -d '@_.-' | tr -d '[:space:]'`

bx cs init
clusterName=`bx cs clusters | grep normal | awk '{print $1}'`

if [ -z "$clusterName" ]; then
  echo "#  cluster not found - create one and wait until the status is normal"
  exit
fi
echo "#  Deploying to $clusterName #"
extIP=`bx cs workers $clusterName | grep Ready | awk '{print $2}'`
echo "# Mapped IP: $extIP #"
bx service create cloudantNoSQLDB Lite cloudantdb
bx service key-create cloudantdb cred

bx cs cluster-service-bind $clusterName default cloudantdb

cfg=`bx cs cluster-config $clusterName | grep KUBECONFIG`
$cfg
kubectl create -f lightbluecompute-min.yml
sleep 30
mysql -udbuser -pPass4dbUs3R -h${extIP} -P30006 < mysql/scripts/load-data.sql

curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '{"username": "foo", "password": "bar", "firstName": "foo", "lastName": "bar", "email": "foo@bar.com"}' -i http://${extIP}:30110/micro/customer

