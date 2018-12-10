#!/bin/bash
  
if [ "$JAVA_HOME" = "" ]; then
  echo "Error: JAVA_HOME is not set."
  exit 1
fi
 
bin=`dirname "$1"`
 
export GUN_HOME=`cd $bin/../; pwd`
 
GUN_CONF_DIR=$GUN_HOME/conf
CLASSPATH="${GUN_CONF_DIR}"
 
for f in $GUN_HOME/lib/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done
 
LOG_DIR=${GUN_HOME}/logs
 
CLASS=com.tct.server.WuJingGunLocationMQServer
nohup ${JAVA_HOME}/bin/java -classpath "$CLASSPATH" $CLASS > ${LOG_DIR}/wujinggunlocation.log 2>&1 < /dev/null &
