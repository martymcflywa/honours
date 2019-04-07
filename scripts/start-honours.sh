#!/bin/bash

# @author Martin Ponce 10371381
#
# This script conveniently wraps the execution of the server and client into
# a single command. It's a bash script, so it only supports linux/macOS, but it
# might work if you're running Bash for Win 10?  ¯\_(ツ)_/¯
#
# The script assumes you have called it from the root folder of this repository.
# ie. ./scripts/start-honours.sh
#
# If you're on an unsupported OS, just follow the steps below manually in your
# console of choice.
#
# 1) Set CLASSPATH environment variable to: "./honours-interfaces/target/classes"
# 2) Run: rmiregistry & (as background process)
# 3) Wait a few seconds
# 4) Run: java -jar -Djava.security.policy=security.policy ./honours-server/target/honours-server-0.1.0-SNAPSHOT-jar-with-dependencies.jar & (as background process)
# 5) Run: java -jar -Djava.security.policy=security.policy ./honours-client/target/honours-client-0.1.0-SNAPSHOT-jar-with-dependencies.jar

validatePath() {
  if [ ! -f "$1" ]; then
    echo "$1 does not exist, try 'maven clean verify' first.";
    exit 1;
  fi
}

serverAssembly="./honours-server/target/honours-server-0.1.0-SNAPSHOT-jar-with-dependencies.jar";
clientAssembly="./honours-client/target/honours-client-0.1.0-SNAPSHOT-jar-with-dependencies.jar";
wait=20;

echo "Check java exists";
java -version

echo "Set classpath for remote interface";
interfacePath="./honours-interfaces/target/honours-interfaces-0.1.0-SNAPSHOT.jar";
validatePath $interfacePath
export CLASSPATH=$interfacePath;
printenv | grep CLASSPATH;

echo "Start rmiregistry as background process";
rmiregistry & # -J-Djava.rmi.server.logCalls=true # use switch for debug mode

echo "Wait $wait seconds, make sure rmiregistry has started";
sleep $wait

echo "Start honours server as background process";
java -jar -Djava.security.policy=security.policy -Djava.rmi.server.hostname=localhost $serverAssembly &
sleep 1

echo "Start honours client in foreground";
java -jar -Djava.security.policy=security.policy -Djava.rmi.server.hostname=localhost $clientAssembly

echo "Kill honours client";
pkill -f $clientAssembly;
echo "Kill honours server";
pkill -f $serverAssembly;
echo "Kill rmiregistry";
pkill -f rmiregistry
echo "Done";