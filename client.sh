#!/bin/bash
export CLASSPATH=$(pwd)/target/tp-pod-jar-with-dependencies.jar
java -Dquery=3 -Dpath=//home/prudi/Desktop/imdb-40.json edu.itba.pod.hazel.tp.Main
