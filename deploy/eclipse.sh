#!/bin/bash

wget http://ftp.fau.de/eclipse/technology/epp/downloads/release/mars/2/eclipse-jee-mars-2-linux-gtk-x86_64.tar.gz
tar xzf eclipse-jee-mars-2-linux-gtk-x86_64.tar.gz
ln -s /opt/eclipse/eclipse /usr/bin/eclipse
chown centos:centos /opt/eclipse -R
