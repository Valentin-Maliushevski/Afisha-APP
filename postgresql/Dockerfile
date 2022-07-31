FROM postgres:14.4-alpine
MAINTAINER ITAcademy

ADD /ClassifierService/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /ConcertsService/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /FilmsService/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /UsersService/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /UsersService/dml/*.sql /docker-entrypoint-initdb.d/
