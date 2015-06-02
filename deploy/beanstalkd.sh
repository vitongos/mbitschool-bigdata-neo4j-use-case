#!/bin/bash

sudo yum install -y telnet telnet-server

sudo yum install -y beanstalkd

sudo service beanstalkd start
