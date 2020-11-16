#!/bin/bash
ORNG='\033[0;33m'
NC='\033[0m'
W='\033[1;37m'
LP='\033[1;35m'
YLW='\033[1;33m'
LBBLUE='\e[104m'
RED='\033[0;31m'
CG=$(pwd)
CS='/opt/sifter/modules/exmods/CS'
if [[ ! -d ${CS} ]]; then
	mkdir ${CS}
fi
if [[ ${CG} == */"cPlug" ]]; then
	cp ext/miko.py -t ${CS}
	if [[ ! -f '/opt/sifter/modules/exmods/CS/cstrike.sh' ]]; then
		cp ./plug-C.sh /opt/sifter/modules/exmods/CS/cstrike.sh
	fi
	if [[ -d 'AggressorScripts_CSv3' ]]; then
		sudo mv AggressorScripts_CSv3/* -t /opt
		rm -rf AggressorScripts_CSv3
		sudo chown $USER:$USER -R /opt/Arsenal /opt/cobalt_strike_extention_strike /opt/AggressorScripts
	fi
	if [[ -d 'AggressorScripts_CSv4' ]]; then
		sudo mv AggressorScripts_CSv4/* -t /opt
		rm -rf AggressorScripts_CSv4
		sudo chown $USER:$USER -R /opt/cobalt-arsenal /opt/aggressor-scripts
	fi
	rm -rf ${CG}
	sifter -e
fi
if [[ ! -f '/opt/sifter/modules/exmods/CS/miko.py' ]] || [[ ! -f '/opt/sifter/modules/exmods/CS/cmiko.py' ]]; then
	cd ${CS}
	wget https://raw.githubusercontent.com/Sifter-Ex/cPlug/master/ext/miko.py
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
echo -e "${W}Is your teamserver hosted on the (${YLW}l${W})ocal machine or (${YLW}r${W})emote server? (${YLW}l${W}/${YLW}r${W})${NC}"
read TSL
if [[ ${TSL} == "l" ]]; then
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
else	
	if [[ ! -f '/opt/sifter/modules/exmods/CS/cmiko.py' ]]; then
		echo -e "${W}Please enter the IP of the server hosting your teamserver"
		read RSSH
		sed -i "s/RSI/${RSSH}/g" /opt/sifter/modules/exmods/CS/miko.py
		echo -e "${W}Please enter the password for root login${NC}"
		read SSHP
		sed -i "s/RSP/${SSHP}/g" /opt/sifter/modules/exmods/CS/miko.py
		echo -e "${W}Please enter the full path/to/cobaltstrike dir on remote server${NC}"
		read TSL
		sed -i "s/TSDIR/${TSL}/g" /opt/sifter/modules/exmods/CS/miko.py
		echo -e "${W}Please enter the password to use for the teamserver${NC}"
		read PTS
		sed -i "s/TSP/${PTS}/g" /opt/sifter/modules/exmods/CS/miko.py
		mv /opt/sifter/modules/exmods/CS/miko.py /opt/sifter/modules/exmods/CS/cmiko.py
	fi
	python /opt/sifter/modules/exmods/CS/cmiko.py
	sleep 10
	echo -e "${LP}[*]${NC} Testing connection to server via curl at: http://${RSSH}:50050"
	output=`curl -s --retry-connrefused --retry 5 http://${RSSH}:50050 --insecure`
	if [ $? -ne 0 ]
	then
		echo -e "${RED}[-]${NC} Failed to hit CobaltStrike teamserver."
		echo -e "${RED}[-]${NC} Either teamserver dir is wrong or firewall is blocking connections\nPlease login in manually to start your teamserver"
	else
		echo -e "${YLW}[+]${NC} Successfully connected to CobaltStrike teamserver"
		echo -e "${YLW}[+]${NC} CobaltStrike teamserver is ready to go!"
	fi
fi
echo -e "${ORNG}Starting CobaltStrike${NC}"
xterm -e java -XX:ParallelGCThreads=4 -XX:+AggressiveHeap -XX:+UseParallelGC -Xms512M -Xmx1024M -jar cobaltstrike.jar &

##########################______________ VGhlIERlYWQgQnVubnkgQ2x1Yg== ______________##########################
