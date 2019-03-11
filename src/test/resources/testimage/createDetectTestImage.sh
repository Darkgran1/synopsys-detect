#!/bin/bash
#
# Run this from an empty directory from a Linux machine that:
# - Has Docker installed
# - Has git installed
# - Is configured with a certificate for accessing github

imageorg=blackducksoftware
imagename=detecttest
imageversion=1.0
containername=${imagename}
gitbranch=sb_testContainer # normally master

# ignore errors on these:
docker stop ${containername} || true
docker rm ${containername} || true
docker rmi ${imageorg}/${imagename}:${imageversion} || true

rm -rf image
mkdir image
cd image
curl -O https://raw.githubusercontent.com/blackducksoftware/synopsys-detect/${gitbranch}/src/test/resources/testimage/image/Dockerfile
curl -O https://raw.githubusercontent.com/blackducksoftware/synopsys-detect/${gitbranch}/src/test/resources/testimage/image/setup.sh
chmod a+x setup.sh

# Get blackduck-common
rm -rf blackduck-common
git clone git@github.com:blackducksoftware/blackduck-common.git

# Get common-maven-parent
rm -rf common-maven-parent
git clone git@github.com:blackducksoftware/common-maven-parent.git

# Build docker image
docker build -t ${imageorg}/${imagename}:${imageversion} .

# Cleanup
rm -rf blackduck-common
rm -rf common-maven-parent

echo "====================="
echo "Next steps:"
echo "docker run -it -d --name ${containername} ${imageorg}/${imagename}:${imageversion}; docker attach ${containername}"
echo "cd /opt/blackduck/detect/"
echo "./setup.sh"
