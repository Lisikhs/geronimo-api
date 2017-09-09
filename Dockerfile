FROM ubuntu:16.04


RUN apt-get update

# Install Java.
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  apt-get install -y software-properties-common && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Install mysql-server and set up root/5472 user
RUN echo "mysql-server-5.7 mysql-server/root_password password 5472" | debconf-set-selections && \
  echo "mysql-server-5.7 mysql-server/root_password_again password 5472" | debconf-set-selections && \
  apt-get update && \
  apt-get install -y mysql-server-5.7

RUN service mysql start && \
  mysql -uroot -p5472 -e 'DROP DATABASE IF EXISTS geronimo_test;' && \
  mysql -uroot -p5472 -e 'CREATE DATABASE geronimo_test;'

ADD . /app
WORKDIR /app

# Build the application
RUN chmod +x mvnw && ./mvnw clean flyway:migrate install -Dskip.tests=true -P default

# Run the application
CMD ["java", "-jar", "target/geronimo-0.0.1-SNAPSHOT.jar"]