#!/usr/bin/env bash
#!/usr/bin/env bash
java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 target/ayrona-ate-swarm.jar