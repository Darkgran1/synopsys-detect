FROM ubuntu:latest
RUN apt-get update -y && \
                apt install -y openjdk-8-jdk vim curl dnsutils maven && \
                apt-get -y clean
RUN mkdir -p /opt/blackduck/detect/testprojects
COPY setup.sh /opt/blackduck/detect
COPY blackduck-common /opt/blackduck/detect/testprojects/blackduck-common
COPY common-maven-parent /opt/blackduck/detect/testprojects/common-maven-parent
