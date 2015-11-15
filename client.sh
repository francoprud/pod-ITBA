#!/bin/bash
export CLASSPATH=$(pwd)/target/tp-pod-jar-with-dependencies.jar
java -Dquery=2 -Dtope=1995 -Dpath=//home/prudi/Desktop/imdb-40.json edu.itba.pod.hazel.tp.Main
