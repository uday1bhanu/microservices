FROM mongo:latest
MAINTAINER Uday Bhanu Prasad <uday1bhanu@gmail.com>
ENTRYPOINT []
CMD export MONGODB_PASSWORD=$(cat /etc/secrets/password); export MONGODB_USERNAME=$(cat /etc/secrets/username); /docker-entrypoint.sh mongod
