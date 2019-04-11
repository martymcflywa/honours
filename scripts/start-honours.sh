#!/bin/bash
set -e
trap 'last_command=$current_command; current_command=$BASH_COMMAND' DEBUG
trap 'echo "\"${last_command}\" command exit code $?."' EXIT

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
# 1) java -jar -Djava.security.policy=security.policy ./honours-server/target/honours-server-0.1.0-SNAPSHOT-jar-with-dependencies.jar &; # as background process
# 2) java -jar -Djava.security.policy=security.policy ./honours-client/target/honours-client-0.1.0-SNAPSHOT-jar-with-dependencies.jar

validatePath() {
  if [ ! -f "$1" ]; then
    echo "$1 does not exist, try 'mvn clean verify' first.";
    exit 1;
  fi
}

serverAssembly="./honours-server/target/honours-server-0.1.0-SNAPSHOT-jar-with-dependencies.jar";
clientAssembly="./honours-client/target/honours-client-0.1.0-SNAPSHOT-jar-with-dependencies.jar";
wait=20;

echo "Check java exists";
java -version;
echo "Check maven exists";
mvn -version;
echo "Building";
mvn clean verify;
echo "Check server executable exists at $serverAssembly";
validatePath $serverAssembly;
echo "Check client executable exists $clientAssembly";
validatePath $clientAssembly;

echo "Start honours server as background process";
java -jar -Djava.security.policy=security.policy -Djava.rmi.server.hostname=localhost $serverAssembly &
sleep 1

echo "Start honours client in foreground";
java -jar -Djava.security.policy=security.policy -Djava.rmi.server.hostname=localhost $clientAssembly

echo "Kill honours server";
pkill -f $serverAssembly;
echo "Done";