# 基础镜像
#FROM bus.ga/java:8
FROM baseimage
# 维护者
MAINTAINER dockerMainainer
# 挂载数据卷
VOLUME /tmp
# 添加build jar到容器中
ENV ARTIFACT=pip-north-server
COPY *.jar /$ARTIFACT.jar
# 配置JMX 方便jvisualvm连接监控
ENV JAVA_OPTS="\
-Xms1024m \
-Xmx1024m \
-Xss512K \
-Deureka.client.serviceUrl.defaultZone=http://ms-discovery-eureka:8761/eureka/ "
# 暴露端口
EXPOSE 9999
# JMX
EXPOSE 19094
# 入口点
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /$ARTIFACT.jar
