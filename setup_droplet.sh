#!/bin/bash

# Step 0: You need to have copied the assembly JAR to rorschach/target/scala-2.10/rorschach-assembly-1.0.jar

# set to Pacific Time (for @cvrebert)
# ln -sf /usr/share/zoneinfo/America/Los_Angeles /etc/localtime

# remove useless crap
aptitude remove wpasupplicant wireless-tools
aptitude remove pppconfig pppoeconf ppp

# setup firewall
ufw default allow outgoing
ufw default deny incoming
ufw allow ssh
ufw allow www
ufw enable
ufw status verbose

# setup Docker; written against Docker v1.5.0
docker build . 2>&1 | tee docker.build.log
IMAGE_ID="$(tail -n 1 docker.build.log | cut -d ' ' -f 3)"
docker run --read-only -d -p 80:9090 --name rorschach $IMAGE_ID
