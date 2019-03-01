#!/bin/bash

wget http://ftp.osuosl.org/pub/eclipse/technology/epp/downloads/release/2018-12/R/eclipse-jee-2018-12-R-linux-gtk-x86_64.tar.gz
tar xzf eclipse-jee-2018-12-R-linux-gtk-x86_64.tar.gz
mv eclipse /opt/
ln -s /opt/eclipse/eclipse /usr/bin/eclipse
rm eclipse-jee-2018-12-R-linux-gtk-x86_64.tar.gz
chown centos:centos /opt/eclipse -R
