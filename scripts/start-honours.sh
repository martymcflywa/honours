#!/bin/bash
set -e

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
# 1) mvn clean verify; # to build the project
# 1) java -jar -Djava.security.policy=security.policy ./honours-server/target/honours-server-1.0.0-SNAPSHOT-jar-with-dependencies.jar &; # as background process
# 2) java -jar -Djava.security.policy=security.policy ./honours-client/target/honours-client-1.0.0-SNAPSHOT-jar-with-dependencies.jar

validatePath() {
  if [ ! -f "$1" ]; then
    echo "$1 does not exist, try 'mvn clean verify' first.";
    exit 1;
  fi
}

semver=1.0.0;
interfaces="./honours-interfaces/target/honours-interfaces-$semver-SNAPSHOT.jar";
persistAssembly="./honours-persist/target/honours-persist-$semver-SNAPSHOT-jar-with-dependencies.jar";
serverAssembly="./honours-server/target/honours-server-$semver-SNAPSHOT-jar-with-dependencies.jar";
clientAssembly="./honours-client/target/honours-client-$semver-SNAPSHOT-jar-with-dependencies.jar";
securityParam="-Djava.security.policy=security.policy";
hostnameParam="-Djava.rmi.server.hostname=localhost";

wait=20;

echo "Check java exists";
java -version;
echo "Check maven exists";
mvn -version;
echo "Building";
mvn clean package -Dmaven.test.skip=true;
echo "Check remote interfaces exists $interfaces";
validatePath $interfaces;
echo "Check persist executable exists $persistAssembly";
validatePath $persistAssembly;
echo "Check server executable exists $serverAssembly";
validatePath $serverAssembly;
echo "Check client executable exists $clientAssembly";
validatePath $clientAssembly;

echo "Add remote interfaces to classpath"
export CLASSPATH=$interfaces;
printenv | grep CLASSPATH;

echo "Start rmiregistry as background process";
rmiregistry -J-Djava.rmi.server.logCalls=true &
echo "Wait $wait seconds for rmiregistry to begin";
sleep $wait;

echo "Start honours persist as background process";
java -jar $securityParam $hostnameParam $persistAssembly &
echo "Wait $wait seconds for honours persist to begin";
sleep $wait;
echo "Start honours server as background process";
java -jar $securityParam $hostnameParam $serverAssembly &
sleep 1;
echo "Start honours client in foreground";
java -jar $securityParam $hostnameParam $clientAssembly

echo "Kill honours server";
pkill -f $serverAssembly;
echo "Kill honours persist";
pkill -f $persistAssembly;
echo "Kill rmiregistry";
pkill -f rmiregistry;
echo "Done";