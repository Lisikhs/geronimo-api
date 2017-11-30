FROM ubuntu:16.04

RUN apt-get update && apt-get upgrade -y

# Install Java 8
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  apt-get install -y software-properties-common && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer

# Set JAVA_HOME env variable
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Install Maven 3.5.0
RUN cd /opt/ && wget http://ftp.ps.pl/pub/apache/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.tar.gz && \
  tar -xvzf apache-maven-3.5.2-bin.tar.gz && \
  mv apache-maven-3.5.2 maven

# Set M2_HOME env variable, and put it into PATH, so that mvn is available anywhere
ENV M2_HOME /opt/maven
ENV PATH $PATH:$M2_HOME/bin

# Install mysql-client-5.7, perhaps we'll need it
RUN apt-get update && apt-get install -y mysql-client-5.7

# copy files from the project to the ubuntu docker image
ADD . /app

# set working directory, all commands will be executed against /app folder
WORKDIR /app

# tomcat will write to this directory
VOLUME /tmp

# expose 8080 port to the whole world
EXPOSE 8080
