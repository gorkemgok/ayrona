FROM jboss/jdk-base:8

MAINTAINER Görkem Gök

RUN mkdir /space
COPY ayrona-data-swarm.jar /space

RUN ls /space
CMD java -jar /space/ayrona-data-swarm.jar
