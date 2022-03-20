#!/bin/sh


compilerOption=""
port=""

if [ "$1" ] && [ "$1" == "debug" ]
then
  compilerOption=-jvm-debug
  port=8500
  echo "Debug"
fi

echo "Starting API at port 8500 with debugger"
sbt run $compilerOption $port