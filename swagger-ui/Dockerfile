FROM swaggerapi/swagger-ui:latest
MAINTAINER ITAcademy

ADD /classifier-service/spec/classifier-service.yml /usr/share/nginx/html/doc/classifier-service.yml
ADD /concerts-service/spec/concerts-service.yml /usr/share/nginx/html/doc/concerts-service.yml
ADD /films-service/spec/films-service.yml /usr/share/nginx/html/doc/films-service.yml
ADD /users-service/spec/user-service.yml /usr/share/nginx/html/doc/users-service.yml

ENV URLS="[{ url: 'doc/classifier-service.yml', name: 'Classifier'},{ url: 'doc/concerts-service.yml', name: 'Concerts'},{ url: 'doc/films-service.yml', name: 'Films'},{ url: 'doc/users-service.yml', name: 'Users'}]"
ENV URLS_PRIMARY_NAME="Events"