#!/usr/bin/env bash
export TERM_HOST=104.197.70.53
export TERM_PORT=7788
export JFX_HOST=85.101.76.42
export JFX_PORT=7790
export LSNR_BROKER=AtaOnline-Demo
export LSNR_LOGIN=1218368283
export LSNR_PASS=gm7xtnn
export AMQ_URI=tcp://ata1:61616
export ATA_DATA_HPLIST=172.16.192.40:7000
export MONGODB_HOST=localhost
export SYMBOLS=FX_EURUSD-EURUSD,FX_USDTRY-USDTRY,VOB_XAUTRY-F_XAUTRYM1016S0,VOB_X30-F_XU0301016S0
java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 target/ayrona-data-swarm.jar
