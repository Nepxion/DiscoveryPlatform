#!/bin/bash
Xms=2G
Xmx=2G
Xmn=256M
APPLICATION="@project.name@"
APPLICATION_JAR="@build.finalName@.jar"
VERSION="@project.version@"
LOG_DIR="/discovery/logs/platform/${APPLICATION}"
LOGS_HEAPDUMP="${LOG_DIR}/heapdump"
LOGS_GC="${LOG_DIR}/gc"

cd $(dirname $0)
cd ..
BASE_PATH=$(pwd)
CONFIG_DIR=${BASE_PATH}"/config/"

STARTUP_LOG="================================================ $(date +'%Y-%m-%m %H:%M:%S') ================================================\n"

if [[ ! -d "${LOG_DIR}" ]]; then
  mkdir -p "${LOG_DIR}"
fi
if [[ ! -d "${LOGS_HEAPDUMP}" ]]; then
  mkdir -p "${LOGS_HEAPDUMP}"
fi
if [[ ! -d "${LOGS_GC}" ]]; then
  mkdir -p "${LOGS_GC}"
fi

JAVA_OPT="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -XX:-OmitStackTraceInFastThrow "
JAVA_GC="-Xloggc:${LOGS_GC}/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M "
JAVA_MEM_OPT="-server -Xms${Xms} -Xmx${Xmx} -Xmn${Xmn} -XX:NewRatio=1 -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:MaxDirectMemorySize=512m -XX:+HeapDumpOnOutOfMemoryError "
JAVA_MEM_OPT="${JAVA_MEM_OPT} -XX:HeapDumpPath=${LOGS_HEAPDUMP}/ "
JAVA_PARAMETER_OPT="-Dversion=${VERSION} -Dwork.dir=${BASE_PATH}"

STARTUP_LOG="${STARTUP_LOG}application name: ${APPLICATION}\n"
STARTUP_LOG="${STARTUP_LOG}application jar name: ${APPLICATION_JAR}\n"
STARTUP_LOG="${STARTUP_LOG}application jar version: ${VERSION}\n"
STARTUP_LOG="${STARTUP_LOG}application root path: ${BASE_PATH}\n"
STARTUP_LOG="${STARTUP_LOG}application config path: ${CONFIG_DIR}\n"
STARTUP_LOG="${STARTUP_LOG}application startup command: java ${JAVA_OPT} ${JAVA_GC} ${JAVA_MEM_OPT} ${JAVA_PARAMETER_OPT} -jar ${BASE_PATH}/boot/${APPLICATION_JAR} --spring.config.location=${CONFIG_DIR}\n"

nohup java ${JAVA_OPT} ${JAVA_GC} ${JAVA_MEM_OPT} ${JAVA_PARAMETER_OPT} -jar ${BASE_PATH}/boot/${APPLICATION_JAR} --spring.config.location=${CONFIG_DIR} >/dev/null 2>&1 &

PID=$(ps -ef | grep "${APPLICATION_JAR}" | grep -v grep | awk '{ print $2 }')
STARTUP_LOG="${STARTUP_LOG}application pid: ${PID}\n"

echo -e ${STARTUP_LOG}
