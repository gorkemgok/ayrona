#!/usr/bin/env bash
#export AMQ_URI=tcp://ata1:61616
#export MONGODB_HOST=ata1
export SYMBOLS=FX_EURUSD-EURUSD,FX_USDTRY-USDTRY,VOB_XAUTRY-F_XAUTRYM0816S0
java -jar target/ayrona-ate-swarm.jar
