#!/bin/bash

cd ~/Downloads
wget -O neo4j.tar.gz http://neo4j.com/artifact.php?name=neo4j-community-2.2.2-unix.tar.gz
tar zxf neo4j.tar.gz
rm -rf neo4j.tar.gz
sudo mv neo4j-community-2.2.2/ /opt/neo4j/
sudo ln -s /opt/neo4j/bin/neo4j /usr/bin/neo4j
cd ~/use-case-src/
sudo mkdir /opt/neo4j/data/graph.db/
sudo mv data/apps.db.tar.gz /opt/neo4j/data/graph.db/
cd /opt/neo4j/data/graph.db/
sudo tar zxf apps.db.tar.gz
sudo rm -rf apps.db.tar.gz
sudo sed -i -r 's/dbms\.security\.auth_enabled=true/dbms\.security\.auth_enabled=false/' /opt/neo4j/conf/neo4j-server.properties
neo4j start
/usr/bin/neo4j
cd ~/use-case-src/
/opt/neo4j/bin/neo4j-shell -file data/insert.cyp
