#!/usr/bin/env bash
mkdir /space
touch /space/ayrona.properties
echo "ayrn.mongodb.host=172.16.192.79" >> /space/ayrona.properties
echo "ayrn.amq.uri=172.16.192.77" >> /space/ayrona.properties
echo "ataonline.gtp.host=algo.ataonline.com.tr" >> /space/ayrona.properties
echo "ataonline.data.hplist=172.16.192.40:7000" >> /space/ayrona.properties
echo "ayrn.symbols=FX_EURUSD-EURUSD,FX_USDTRY-USDTRY,VOB_XAUTRY-F_XAUTRYM0816S0,VOB_X30-F_XU0300816S0" >> /space/ayrona.properties
echo "AYRN_CONF=/space/ayrona.properties" >> /etc/environment