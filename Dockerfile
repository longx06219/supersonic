#FROM openjdk:8u201-jdk-alpine3.9
#FROM registry.cn-beijing.aliyuncs.com/caspe-prod/openjdk:8u201-jdk-alpine3.9
#FROM registry.cn-beijing.aliyuncs.com/caspe-prod/centos:centos7
#
#RUN yum install -y fontconfig
#RUN fc-cache --force

FROM registry.cn-beijing.aliyuncs.com/caspe-prod/centos:centos7java8factory

WORKDIR /app
COPY target/platform-factory-project-1.0.0-SNAPSHOT.jar .
ENV JAVA_OPTS=""
ENV JAVA_OPT_EXT=""
ENV APP_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS $JAVA_OPT_EXT -jar platform-factory-project-1.0.0-SNAPSHOT.jar $APP_OPTS" ]