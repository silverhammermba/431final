#!/bin/bash

if [ $(uname -o) == "Cygwin" ]; then
	echo "CYGWIN"
fi

# strict mode
set -euo pipefail
IFS=$'\n\t'

# main project directory
basedir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# JARs
server_package=$basedir/massim/target/agentcontest-2013-1.4.jar
agent_packages=$basedir/redrovers/build/redrovers.jar:$basedir/javaagents/target/javaagents-2.1.jar

default_server_conf=2013-cse431comp.xml

if [ $# -lt 3 ] || [ $# -gt 4 ]; then
	cat >&2 <<USAGE
usage: $0 OUTDIR ACONF BCONF [SERVERCONF]

	Run the teams in directories ACONF and BCONF using server config in
	SERVERCONF. Store all output in the directory OUTDIR.

	SERVERCONF defaults to $default_server_conf.
USAGE
	exit 1
fi

output=$1
agenta=$2
agentb=$3
server_conf=${4:-$basedir/massim/scripts/conf/$default_server_conf}

mkdir $output
pushd $output
output=$(pwd)
mkdir backup

(popd; cd $agenta; java -ea -cp $agent_packages massim.javaagents.App >$output/teama.log 2>$output/teama_err.log) &
(popd; cd $agentb; java -ea -cp $agent_packages massim.javaagents.App >$output/teamb.log 2>$output/teamb_err.log) &
(java -Xss20000k -cp $server_package massim.competition2013.monitor.GraphMonitor -rmihost localhost -rmiport 1099 -savexmls >/dev/null 2>/dev/null) &

java -ea -Dcom.sun.management.jmxremote -Xss2000k -Xmx600M -DentityExpansionLimit=1000000 -DelementAttributeLimit=1000000 -Djava.rmi.server.hostname=$(hostname -f)\
	-jar $server_package\
	--conf $server_conf
