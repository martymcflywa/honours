#!/bin/bash
set -e

# @author Martin Ponce 10371381
# This script conveniently wraps the building of the project, and execution of
# distributed services into a single command. The script assumes you have
# invoked it from the root folder of this repository, ie:
# ./scripts/start-honours.ps1
#
# This script assumes that java and maven executables exist in your path.

validatePath() {
  if [ ! -f "$1" ]; then
    echo "$1 does not exist, try 'mvn clean verify' first.";
    exit 1;
  fi
}

semver=2.1.0;
interfaces="./honours-interfaces/target/honours-interfaces-$semver-SNAPSHOT.jar";
persistAssembly="./honours-persist/target/honours-persist-$semver-SNAPSHOT-jar-with-dependencies.jar";
serverAssembly="./honours-server/target/honours-server-$semver-SNAPSHOT-jar-with-dependencies.jar";
clientAssembly="./honours-client/target/honours-client-$semver-SNAPSHOT-jar-with-dependencies.jar";
securityParam="-Djava.security.policy=security.policy";
hostnameParam="-Djava.rmi.server.hostname=localhost";

wait=30;

echo "Check java exists";
java -version;
echo "Check maven exists";
./mvnw -version;
echo "Building";
./mvnw package -Dmaven.test.skip=true;
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
rmiregistry &
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