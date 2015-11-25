#!/bin/bash
export CLASSPATH=$(pwd)/target/tp-pod-jar-with-dependencies.jar
java -Dquery=1 -DN=5 -Dpath=./jsons/imdb-500.json edu.itba.pod.hazel.tp.Main
#java -Dquery=2 -Dtope=1994 -Dpath=./jsons/imdb-500.json edu.itba.pod.hazel.tp.Main
#java -Dquery=3 -Dpath=./jsons/imdb-500.json edu.itba.pod.hazel.tp.Main
#java -Dquery=4 -Dpath=./jsons/imdb-500.json edu.itba.pod.hazel.tp.Main
