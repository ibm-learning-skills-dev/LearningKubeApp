#!/bin/bash

printf "IBMid:"
read userid
printf "Password:"
stty -echo
read password
stty echo
starttime=`date`

bx login -a api.$region.bluemix.net -u "$userid" -p "$password" | tee login.out
logerr=`grep FAILED login.out | wc -l`
rm login.out
if [ $logerr -eq 1 ]; then
  echo "#    Login failed... Exiting"
  exit
fi

suffix=`echo -e $userid | tr -d '@_.-' | tr -d '[:space:]'`

bx cs init
clusterName=`bx cs clusters | grep normal | awk 'print $4'`

if [ $clusterName -eq "" ]; then
  echo "#  cluster not found - create one and wait until the status is normal"
  exit
fi

bx service create cloudantNoSQLDB Lite cloudantdb
bx service key-create cloudantdb cred

bx cs cluster-service-bind $clusterName default cloudantdb

cfg=`bx cs cluster-config $clusterName | grep KUBECONFIG`
$cfg
kubectl create -f lightbluecompute-min.yml
extIP=`bx cs workers $clusterName | grep Ready | awk '{print $2}'`
cd  web-app-lite
# edit manifest.yml
sed -i -e 's/lightblue-web-bmxedu/lightblue-web-'$suffix'/g' manifest.yml
# 
# edit config/default.json
sed -i -e 's/50.23.5.233/'$extIP'/g' config/default.json

bx app push
