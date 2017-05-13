#!/bin/bash

cd /opt
wget http://apache.rediris.es/storm/apache-storm-1.1.0/apache-storm-1.1.0.tar.gz
tar zxf apache-storm-1.1.0.tar.gz
mv apache-storm-1.1.0 storm
rm apache-storm-1.1.0.tar.gz
ln -s /opt/storm/bin/storm /usr/bin/storm
chown centos:centos /opt/storm -R
