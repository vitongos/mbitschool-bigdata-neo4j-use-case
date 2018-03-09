#!/bin/bash

sed -i -r 's/#dbms\.shell\.enabled=true/dbms\.shell\.enabled=true/' /etc/neo4j/neo4j.conf
cd ~/use-case-src/
deploy/eclipse.sh
cd ~/use-case-src/
deploy/storm.sh
cd ~/use-case-src/
deploy/beanstalkd.sh
cd ~/use-case-src/
deploy/memcached.sh
cd ~/use-case-src/
neo4j-shell -file data/insert.cyp
