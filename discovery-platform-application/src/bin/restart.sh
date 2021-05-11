#!/bin/bash

APPLICATION="@project.name@"
APPLICATION_JAR="@build.finalName@.jar"

echo stop ${APPLICATION} Application...
sh shutdown.sh

echo start ${APPLICATION} Application...
sh startup.sh
