#!/bin/bash

cd /opt
wget http://apache.uvigo.es/storm/apache-storm-1.2.1/apache-storm-1.2.1.tar.gz
tar zxf apache-storm-1.2.1.tar.gz
mv apache-storm-1.2.1 storm
rm apache-storm-1.2.1.tar.gz
ln -s /opt/storm/bin/storm /usr/bin/storm
chown centos:centos /opt/storm -R
