#!/bin/bash
ORNG='\033[0;33m'
NC='\033[0m'
W='\033[1;37m'
LP='\033[1;35m'
YLW='\033[1;33m'
LBBLUE='\e[104m'
RED='\033[0;31m'
CG=$(pwd)
if [[ ${CG} == */"cPlug" ]]; then
	if [[ ! -f '/opt/sifter/modules/exmods/cstrike.sh' ]]; then
		cp ./plug-C.sh /opt/sifter/modules/exmods/cstrike.sh
	fi
	if [[ -d 'AggressorScripts_CSv3' ]]; then
		sudo mv AggressorScripts_CSv3/* -t /opt
		rm -rf AggressorScripts_CSv3
		sudo chown $USER:$USER -R /opt/Arsenal /opt/cobalt_strike_extention_strike /opt/AggressorScripts
	fi
	if [[ -d 'AggressorScripts_CSv4' ]]; then
		sudo mv AggressorScripts_CSv4/* -t /opt
		rm -rf AggressorScripts_CSv4
		sudo chown $USER:$USER -R /opt/cobalt-arsenal
	fi
	rm -rf ${CG}
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
