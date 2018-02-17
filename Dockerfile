FROM alisiikh/alpine-oraclejdk8:slim

# copy files from the project to the ubuntu docker image
ADD . /app

# set working directory, all commands will be executed against /app folder
WORKDIR /app

# tomcat will write to this directory
VOLUME /tmp

# expose 8080 port to the whole world
EXPOSE 8080
