#!/bin/bash

yum install -y telnet telnet-server gcc

cd
git clone git://github.com/kr/beanstalkd.git
cd beanstalkd
make
make install
mkdir /var/lib/beanstalkd

cat << EOF > /etc/systemd/system/beanstalkd.service
[Unit]
Description=Beanstalkd is a simple, fast work queue

[Service]
User=root
ExecStart=/usr/local/bin/beanstalkd -z 512000 -b /var/lib/beanstalkd

[Install]
WantedBy=multi-user.target
EOF

systemctl enable beanstalkd 
systemctl start beanstalkd
