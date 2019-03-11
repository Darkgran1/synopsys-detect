#!/bin/bash
cd /opt/blackduck/detect
rm -rf detect.sh

# get detect
curl -O https://detect.synopsys.com/detect.sh
chmod +x detect.sh
