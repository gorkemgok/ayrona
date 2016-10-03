#!/usr/bin/env bash
scp ayrona-data/target/ayrona-data-swarm.jar root@ata1:/space/ayrona-data
scp ayrona-ate/target/ayrona-ate-swarm.jar  root@ata1:/space/ayrona-ate
scp ayrona-rest/target/ayrona-rest-swarm.jar root@ata1:/space/ayrona-rest
scp ayrona-gpe/target/ayrona-gpe-1.0-SNAPSHOT-jar-with-dependencies.jar  root@ata2:/space/ayrona-gpe