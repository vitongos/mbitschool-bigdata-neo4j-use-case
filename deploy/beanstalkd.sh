#!/bin/bash

yum install -y telnet telnet-server beanstalkd
service beanstalkd start
