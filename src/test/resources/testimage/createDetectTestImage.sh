#!/bin/bash

imageorg=blackducksoftware
imagename=detecttest
imageversion=1.0
containername=${imagename}

# ignore errors on these:
docker stop ${containername} || true
docker rm ${containername} || true
docker rmi ${imageorg}/${imagename}:${imageversion} || true

cd image

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

echo "Next steps:"
echo "docker run -it -d --name ${containername} ${imageorg}/${imagename}:${imageversion}"
echo "docker attach ${containername}"
echo "cd /opt/blackduck/detect/"
echo "./setup.sh"
echo "cd testprojects/blackduck-common/"
echo "../../detect.sh --blackduck.url=https://yourblackduckserver.yourdomain --blackduck.username=yourusername --blackduck.password=yourpassword --blackduck.trust.cert=true"
