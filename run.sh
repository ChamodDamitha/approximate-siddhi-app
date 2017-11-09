#!/bin/bash

CLASSPATH=.:siddhi-execution-approximate-1.0.3-SNAPSHOT.jar:approximateApp-1.0-SNAPSHOT-jar-with-dependencies.jar
JAVA_OPTS="-Xmx4g -Xms4g"
IS_APPROXIMATE="True"
RECORD_WINDOW="1000000"

java -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=settings=profile,duration=1800s,dumponexit=true,filename=approximate-app-test.jfr $JAVA_OPTS -DrecordWindow=${RECORD_WINDOW} -DisApproximate=${IS_APPROXIMATE} -cp $CLASSPATH Main
