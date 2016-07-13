#!/usr/bin/env bash
export TERM_HOST=104.197.70.53
export TERM_PORT=7788
export JFX_HOST=85.101.76.42
export JFX_PORT=7790
export LSNR_BROKER=AtaOnline-Demo
export LSNR_LOGIN=1218368283
export LSNR_PASS=gm7xtnn
export AMQ_URI=tcp://ata1:61616
export MONGODB_HOST=ata1
export SYMBOLS=FX_EURUSD-EURUSD,FX_USDTRY-USDTRY,VOB_XAUTRY-F_XAUTRYM0816S0
java -jar target/ayrona-data-swarm.jar
