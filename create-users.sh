#!/bin/bash

./bin/add-user.sh jomarko User1234. -a -g admin,premium,standard
./bin/add-user.sh tlivora User1234. -a -g admin,premium,standard
./bin/add-user.sh mmacik User1234. -a -g admin,premium,standard
./bin/add-user.sh JMSUser User1234. -a -g admin,premium
./bin/add-user.sh adminuser User1234. -a -g admin
./bin/add-user.sh premiumuser User1234. -a -g premium
./bin/add-user.sh standarduser User1234. -a -g standard

# management user
./bin/add-user.sh management User1234.
