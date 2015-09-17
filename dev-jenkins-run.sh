#!/usr/bin/env bash

ID=`ps -ef | grep 'vayne-0.0.1-SNAPSHOT.jar' | awk '{print $2}'`
for id in $ID
do
        echo "kill process $id"
        kill -9 $id
done


cp /data/jenkins/workspace/Vayne/target/vayne-0.0.1-SNAPSHOT.jar /data/workspace/vayne/

# for log4j
cd /data/workspace/vayne/
nohup java -jar /data/workspace/vayne/vayne-0.0.1-SNAPSHOT.jar &