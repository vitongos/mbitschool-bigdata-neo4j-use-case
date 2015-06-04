#!/bin/bash

sudo yum install -y libevent-devel

sudo yum install -y memcached

sudo service memcached start
