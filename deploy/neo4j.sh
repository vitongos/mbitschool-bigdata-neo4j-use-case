#!/bin/bash

cd /opt/
wget https://neo4j.com/artifact.php?name=neo4j-community-3.0.6-unix.tar.gz
tar xzf neo4j-community-3.0.6-unix.tar.gz
adduser neo4j -s /sbin/nologin
chown neo4j:centos /opt/neo4j-community-3.0.6 -R

cat << EOF > /lib/systemd/system/neo4j.service
[Unit] 
Description=Neo4j Management Service

[Service]
Type=forking
User=neo4j
ExecStart=/opt/neo4j-community-3.0.6/bin/neo4j start
ExecStop=/opt/neo4j-community-3.0.6/bin/neo4j stop
ExecReload=/opt/neo4j-community-3.0.6/bin/neo4j restart
RemainAfterExit=no
Restart=on-failure
PIDFile=/opt/neo4j-community-3.0.6/run/neo4j.pid
LimitNOFILE=60000
TimeoutSec=600

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl enable neo4j.service
service neo4j start

cd ~/use-case-src/
/opt/neo4j/bin/neo4j-shell -file data/insert.cyp
