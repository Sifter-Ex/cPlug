#!/bin/bash
ORNG='\033[0;33m'
NC='\033[0m'
W='\033[1;37m'
LP='\033[1;35m'
YLW='\033[1;33m'
LBBLUE='\e[104m'
RED='\033[0;31m'

if [[ ! -f '/opt/sifter/modules/exmods/cstrike.sh' ]]; then
	cp ./plug-C.sh /opt/sifter/modules/exmods/cstrike.sh
	CG=$(pwd)
	if [[ ${CG} == */"cPlug" ]]; then
		rm -rf ${CG}
	fi
	sifter -e
fi
# CobaltStrike Directory Location
if [[ -f '/opt/sifter/extras/.cstrike' ]]; then
	CDIR=$(cat /opt/sifter/extras/.cstrike)
else
	echo -e "${ORNG}Please enter the full path/to/CobaltStrike_directory${NC}"
	read CS
	echo CS > /opt/sifter/extras/.cstrike
	CDIR=$(/opt/sifter/extras/.cstrike)
fi
cd ${CDIR}
if [[ -f '.tserver' ]]; then
	TEAM=$(cat .tserver)
else
	echo -e "${RED}Please enter the IP and password to use for your teamserver (separated by a space)${NC}"
	read TS
	echo ${TS} > .tserver
	TEAM=$(.tserver)
fi
xterm -e sudo ./teamserver ${TEAM} &
xterm -e java -XX:ParallelGCThreads=4 -XX:+AggressiveHeap -XX:+UseParallelGC -Xms512M -Xmx1024M -jar cobaltstrike.jar &

##########################______________ VGhlIERlYWQgQnVubnkgQ2x1Yg== ______________##########################