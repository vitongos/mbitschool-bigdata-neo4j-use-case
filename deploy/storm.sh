#!/bin/bash

cd ~/Downloads
wget http://apache.rediris.es/storm/apache-storm-0.10.0/apache-storm-0.10.0.tar.gz
sudo mv apache-storm-0.10.0.tar.gz /opt/
cd /opt
sudo tar zxf apache-storm-0.10.0.tar.gz
sudo mv apache-storm-0.10.0 storm
sudo rm apache-storm-0.10.0.tar.gz
sudo ln -s /opt/storm/bin/storm /usr/bin/storm
