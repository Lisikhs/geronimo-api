FROM ubuntu:16.04

RUN apt-get update

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
RUN cd /opt/ && wget http://www-eu.apache.org/dist/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.tar.gz && \
  tar -xvzf apache-maven-3.5.0-bin.tar.gz && \
  mv apache-maven-3.5.0 maven

# Set M2_HOME env variable, and put it into PATH, so that mvn is available anywhere
ENV M2_HOME /opt/maven
ENV PATH $PATH:$M2_HOME/bin

# Install mysql-server
RUN echo "mysql-server-5.7 mysql-server/root_password password 5472" | debconf-set-selections && \
  echo "mysql-server-5.7 mysql-server/root_password_again password 5472" | debconf-set-selections && \
  apt-get update && \
  apt-get install -y mysql-server-5.7

# setup root user for mysql
RUN service mysql start && mysql -uroot -p5472 -e 'CREATE DATABASE geronimo;'

# copy files from the project to the ubuntu docker image
ADD . /app

# set working directory, all commands will be executed against /app folder
WORKDIR /app

# Build the application
RUN mvn clean flyway:migrate install -Dskip.tests=true

# Run the application jar file
CMD ["java", "-jar", "target/geronimo-0.0.1-SNAPSHOT.jar"]
