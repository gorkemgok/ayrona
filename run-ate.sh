#!/usr/bin/env bash
java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 ayrona-ate/target/ayrona-ate-swarm.jar
