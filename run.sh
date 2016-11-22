#!/bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

mkagentconf () {
	UTEAM=${0^^}
	LTEAM=${0,,}
	AGENT=$1

	mkdir team$UTEAM
	pushd team$UTEAM

	cat >eismassimconfig.xml <<TMP
<?xml version="1.0" encoding="UTF-8"?>
<interfaceConfig scenario="mars2013" host="localhost" port="12300" scheduling="yes" times="no" notifications="no" queued="no" statisticsFile="no" statisticsShell="no" submitStatistic="no">
<entities>
TMP
	for i in {1..10}; do
		cat >>eismassimconfig.xml "<entity name=\"connection${UTEAM}$i\" username=\"${LTEAM}$i\" password=\"1\" iilang=\"yes\" xml=\"yes\"/>"
	done
	cat >>eismassimconfig.xml <<TMP
  </entities>
</interfaceConfig>
TMP

	cat >javaagentsconfig.xml <<TMP
<?xml version="1.0" encoding="UTF-8"?>
<javaAgentsConfig>
  <agents>
TMP
	for i in {1..10}; do
		cat >>javaagentsconfig.xml "<agent name=\"agent${UTEAM}$i\" entity=\"connection${UTEAM}$i\" class=\"$AGENT\" team=\"$UTEAM\"/>"
	done
	cat >>javaagentsconfig.xml <<TMP
  </agents>
</javaAgentsConfig>
TMP
	popd
}

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
conf=${4:-$basedir/massim/scripts/conf/2013-cse431comp.xml}

if [ -f $output ]; then
	echo "$output already exists in system" >&2
	exit 1
fi

mkdir $output
cd $output

mkagentconf "A" $agenta
mkagentconf "B" $agentb

(cd teamA; java -ea -jar $agent_package) &
(cd teamB; java -ea -jar $agent_package) &

java -ea -Dcom.sun.management.jmxremote -Xss2000k -Xmx600M  -DentityExpansionLimit=1000000 -DelementAttributeLimit=1000000 -Djava.rmi.server.hostname=$(hostname -f)\
	-jar $server_package\
	--conf $conf

# TODO create dir for each agent containing eismassconfig.xml and javaagentsconfig.xml
# first just has connections/usernames/passwords
# second has name/connection/javaclass/team
