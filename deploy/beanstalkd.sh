#!/bin/bash

yum install -y telnet telnet-server beanstalkd
sed -i "s/#MAX_JOB_SIZE=-z 65535/MAX_JOB_SIZE=-z 512000/" /etc/sysconfig/beanstalkd
service beanstalkd start
