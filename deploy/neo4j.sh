#!/bin/bash

cd /opt/
wget https://neo4j.com/artifact.php?name=neo4j-community-3.2.0-unix.tar.gz
mv "artifact.php?name=neo4j-community-3.2.0-unix.tar.gz" neo4j-community-3.2.0-unix.tar.gz
tar xzf neo4j-community-3.2.0-unix.tar.gz
adduser neo4j -s /sbin/nologin
chown neo4j:centos /opt/neo4j-community-3.2.0 -R
rm neo4j-community-3.2.0-unix.tar.gz

cat << EOF > /lib/systemd/system/neo4j.service
[Unit] 
Description=Neo4j Management Service

[Service]
Type=forking
User=neo4j
ExecStart=/opt/neo4j-community-3.2.0/bin/neo4j start
ExecStop=/opt/neo4j-community-3.2.0/bin/neo4j stop
ExecReload=/opt/neo4j-community-3.2.0/bin/neo4j restart
RemainAfterExit=no
Restart=on-failure
PIDFile=/opt/neo4j-community-3.2.0/run/neo4j.pid
LimitNOFILE=60000
TimeoutSec=600

[Install]
WantedBy=multi-user.target
EOF

sed -i "s/#dbms.security.auth_enabled=false/dbms.security.auth_enabled=false/" /opt/neo4j-community-3.2.0/conf/neo4j.conf
sed -i "s/#dbms.shell.enabled=true/dbms.shell.enabled=true/" /opt/neo4j-community-3.2.0/conf/neo4j.conf
systemctl daemon-reload
systemctl enable neo4j.service
systemctl start neo4j.service
sleep 5

cd ~/use-case-src/
/opt/neo4j-community-3.2.0/bin/neo4j-shell -file data/insert.cyp
