#!/bin/bash
export CLASSPATH=$(pwd)/target/tp-pod-jar-with-dependencies.jar
java com.hazelcast.console.ConsoleApp
