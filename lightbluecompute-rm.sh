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

cfg=`bx cs cluster-config $clusterName | grep KUBECONFIG`
$cfg

extIP=`bx cs workers $clusterName | grep Ready | awk '{print $2}'`

if [ -z "$clusterName" ]; then
  echo "#  cluster not found - create one and wait until the status is normal"
  exit
fi
echo "#  Deploying to $clusterName #"
echo "#  Mapped IP: $extIP         #"
bx service key-delete -f cloudantdb cred
bx service delete -f cloudantdb

cfg=`bx cs cluster-config $clusterName | grep KUBECONFIG`
$cfg
kubectl delete secret binding-cloudantdb
kubectl delete -f lightbluecompute-min.yml

