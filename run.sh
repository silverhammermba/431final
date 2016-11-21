#!/bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

# main project directory
basedir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# JARs
server_package=$basedir/massim/target/agentcontest-2013-1.4.jar
agent_package=$basedir/javaagents/target/javaagents-2.1.jar

if [ $# -lt 3 ] || [ $# -gt 4 ]; then
	echo "usage: $0 OUTDIR AGENTA AGENTB [SERVERCONF]" >&2
	exit 1
fi

output=$1
agenta=$2
agentb=$3
conf=${4:-$basedir/massim/scripts/conf/2013-3sims.xml}

if [ -f $output ]; then
	echo "$output already exists in system" >&2
	exit 1
fi

mkdir $output
cd $output

mkdir $agenta

#java -ea -Dcom.sun.management.jmxremote -Xss2000k -Xmx600M  -DentityExpansionLimit=1000000 -DelementAttributeLimit=1000000 -Djava.rmi.server.hostname=$(hostname -f)\
#	-jar $server_package\
#	--conf $conf

# TODO create dir for each agent containing eismassconfig.xml and eismassimconfig.xml

