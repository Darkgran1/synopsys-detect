#!/bin/bash

# Install dotnet
apt-get install -y software-properties-common
add-apt-repository "deb http://security.ubuntu.com/ubuntu xenial-security main"
apt-get -y update
wget -q https://packages.microsoft.com/config/ubuntu/16.04/packages-microsoft-prod.deb
dpkg -i packages-microsoft-prod.deb
apt-get install -y apt-transport-https
apt-get -y update
apt-get install -y libicu55
apt-get install -y dotnet-runtime-deps-2.2
apt-get install -y aspnetcore-runtime-2.2 dotnet-runtime-2.2
apt-get install -y dotnet-sdk-2.2

cd /opt/blackduck/detect
rm -rf detect.sh

# get detect
curl -O https://detect.synopsys.com/detect.sh
chmod +x detect.sh

# Create dotnet sample project
cd testprojects
mkdir dotnet-hello-world
cd dotnet-hello-world
dotnet new console

echo "====================="
echo "Next steps:"
echo "cd /opt/blackduck/detect/testprojects/blackduck-common/"
echo "../../detect.sh --blackduck.url=https://yourblackduckserver.yourdomain --blackduck.username=yourusername --blackduck.password=yourpassword --blackduck.trust.cert=true"
echo "cd ../common-maven-parent"
echo "<re-run the detect.sh command>"
echo "cd ../dotnet-hello-world"
echo "<re-run the detect.sh command>"
