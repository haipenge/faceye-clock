#!/bin/bash
#DES:启动Eureka Server 双机集群
#作者：宋海鹏
#日期:2017.06.27
#准备工作：
#vi /etc/hosts
#ADD 2 lines
#127.0.0.1 cluster-server-1
#127.0.0.1 cluster-server-2
mvn clean compile package -D maven.test.skip=true
cd target
java -jar clock-provider-1.0.jar --spring.profiles.active=cluster-server-001 &
java -jar clock-provider-1.0.jar --spring.profiles.active=cluster-server-002 &