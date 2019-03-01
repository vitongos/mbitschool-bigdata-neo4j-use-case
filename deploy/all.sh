#!/bin/bash

yum install -y epel-release
yum install -y terminator
cd ~/use-case-src/
deploy/neo4j.sh
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
