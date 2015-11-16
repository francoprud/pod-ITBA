#!/bin/bash
export CLASSPATH=$(pwd)/target/tp-pod-jar-with-dependencies.jar
#java -Dquery=1 -DN=5 -Dpath=/home/prudi/Desktop/imdb-20K.json edu.itba.pod.hazel.tp.Main
#java -Dquery=2 -Dtope=1994 -Dpath=/home/prudi/Desktop/imdb-20K.json edu.itba.pod.hazel.tp.Main
#java -Dquery=3 -Dpath=/home/prudi/Desktop/imdb-40.json edu.itba.pod.hazel.tp.Main
java -Dquery=4 -Dpath=/home/prudi/Desktop/imdb-40.json edu.itba.pod.hazel.tp.Main
