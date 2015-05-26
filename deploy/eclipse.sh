#!/bin/bash

USR=`whoami`
cd
wget http://mirror.ibcp.fr/pub/eclipse//technology/epp/downloads/release/luna/SR2/eclipse-java-luna-SR2-linux-gtk-x86_64.tar.gz
sudo mv eclipse-java-luna-SR2-linux-gtk-x86_64.tar.gz /opt/
cd /opt
sudo tar xzf eclipse-java-luna-SR2-linux-gtk-x86_64.tar.gz
sudo chown $USR:$USR eclipse -R
sudo ln -s /opt/eclipse/eclipse /usr/bin/eclipse








