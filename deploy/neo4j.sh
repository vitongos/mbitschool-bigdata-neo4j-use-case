#!/bin/bash

cd /tmp
wget http://debian.neo4j.org/neotechnology.gpg.key
rpm --import neotechnology.gpg.key

cat <<EOF>  /etc/yum.repos.d/neo4j.repo
[neo4j]
name=Neo4j Yum Repo
baseurl=http://yum.neo4j.org/stable
enabled=1
gpgcheck=1
EOF

yum install -y neo4j

sed -i -r 's/#dbms\.security\.auth_enabled=false/dbms\.security\.auth_enabled=false/' /etc/neo4j/neo4j.conf
sed -i -r 's/#dbms\.allow_upgrade=true/dbms\.allow_upgrade=true/' /etc/neo4j/neo4j.conf

systemctl daemon-reload

service neo4j restart

cd ~/use-case-src/
cypher-shell < data/insert.cyp
